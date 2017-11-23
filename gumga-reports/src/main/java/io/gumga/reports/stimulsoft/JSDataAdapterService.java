package io.gumga.reports.stimulsoft;

import com.stimulsoft.base.json.JSONException;
import com.stimulsoft.base.json.JSONObject;
import io.gumga.core.GumgaThreadScope;
import io.gumga.core.GumgaValues;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gelatti on 29/06/17. Updated by Munif
 */
@Service
public class JSDataAdapterService {

    private final static Logger LOG = LoggerFactory.getLogger(JSDataAdapterService.class);

    @Autowired
    public JSDataAdapterService(GumgaValues gumgaValues) {
        this.gumgaValues = gumgaValues;
    }

    private GumgaValues gumgaValues;

    private Properties properties;
    public static String urlProperties;

    private Properties getProperties() {
        if (properties == null) {
            properties = gumgaValues.getCustomFileProperties();
        }
        return properties;
    }

    private String getProperty(String value) {
        if (!getProperties().contains(value)) {
            properties = gumgaValues.getCustomFileProperties();
        }
        return properties.getProperty(value);
    }

    private final List<String> USERS_KEYS = Arrays.asList(new String[]{"jdbc.username", "username", "uid", "user", "user id", "userId", "connection.username"});
    private final List<String> PASSWORD_KEYS = Arrays.asList(new String[]{"jdbc.password", "pwd", "password", "connection.password"});
    private final List<String> HOST_KEY = Arrays.asList(new String[]{"host", "server", "location"});
    private final List<String> PORT_KEY = Arrays.asList(new String[]{"port"});
    private final List<String> DATABASE_KEY = Arrays.asList(new String[]{"database", "database name", "databaseName", "data source"});
    protected final List<String> URL_KEYS = Arrays.asList(new String[]{"jdbc.url", "connectionurl", "url", "connection.url"});

    private String onError(Exception e) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("success", false);
        result.put("notice", e.getMessage() + "<br>" + e.getStackTrace()[0]);
        e.printStackTrace();
        return new JSONObject(result).toString();
    }

    private String connect(JSONObject command) throws SQLException { //TODO_COLOCAR ORACLE
        Connection con = null;
        try {
            String dbName = command.getString("database");
            String connectionString = command.getString("connectionString");
            Map<String, String> params = parseParams(connectionString);
            Properties info = new Properties();
            info.setProperty("user", getUser(params));
            info.setProperty("password", getPassword(params));
            if (!(connectionString.contains("Encoding") || connectionString.contains("encoding")) || params.containsKey("characterEncoding")) {
                info.setProperty("useUnicode", "true");
                info.setProperty("characterEncoding", "UTF-8");
            }
            info.putAll(params);
            if ("MySQL".equals(dbName)) {
                Class.forName("com.mysql.jdbc.Driver");
                // connectionURL = String.format("jdbc:mysql://%s:%s/%s", getHost(params),
                // getPort(params), getDatabase(params));
            } else if ("MS SQL".equals(dbName)) {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                // connectionURL = String.format("jdbc:sqlserver://%s:%s;databaseName=%s",
                // getHost(params), getPort(params), getDatabase(params));
            } else if ("PostgreSQL".equals(dbName)) {
                Class.forName("org.postgresql.Driver");
                // connectionURL = String.format("jdbc:postgresql://%s:%s/%s", getHost(params),
                // getPort(params), getDatabase(params));
            } else if ("Oracle".equals(dbName)) {
                Class.forName("oracle.jdbc.OracleDriver");
                // connectionURL = String.format("jdbc:postgresql://%s:%s/%s", getHost(params),
                // getPort(params), getDatabase(params));
            }

            con = DriverManager.getConnection(getUrl(params), info);
            return onConnect(command, con);
        } catch (Exception e) {
            return onError(e);
        } finally {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        }
    }

    private boolean isCreatingDataSourceOracle(JSONObject command) throws JSONException {
        return (command.getString("queryString").contains("OWNER")
                || command.getString("queryString").contains("TABLE_NAME")
                || command.getString("queryString").contains("ALL_TAB_COLS"))
                && command.getString("database").equals("Oracle");
    }

    private String onConnect(JSONObject command, Connection con) throws JSONException {
        if (command.has("queryString")) {
            return query(command, con);
        } else {
            HashMap<String, Object> result = new HashMap<String, Object>();
            result.put("success", true);
            return new JSONObject(result).toString();
        }
    }

    private String query(JSONObject command, Connection con) {
        try {
            String filteredQuery = addFilterQuery(command);
            PreparedStatement pstmt = con.prepareStatement(filteredQuery);
            ResultSet rs = pstmt.executeQuery();
            return onQuery(rs);
        } catch (Exception e) {
            return onError(e);
        }
    }

    private String addFilterQuery(JSONObject command) throws JSONException {
        String queryString = command.getString("queryString");
        queryString = queryString.replaceAll("\\[\\[oi\\]\\]", GumgaThreadScope.organizationCode.get());
        queryString = queryString.replaceAll("\\[\\[login\\]\\]", GumgaThreadScope.login.get());
        queryString = queryString.replaceAll("\\[\\[gumgaToken\\]\\]", GumgaThreadScope.gumgaToken.get());
        queryString = queryString.replaceAll("\\[\\[ip\\]\\]", GumgaThreadScope.ip.get());
        queryString = queryString.replaceAll("\\[\\[organization\\]\\]", GumgaThreadScope.organization.get());
        queryString = queryString.replaceAll("\\[\\[instanceOi\\]\\]", GumgaThreadScope.instanceOi.get());
        queryString = queryString.replaceAll("\\[\\[softwareName\\]\\]", GumgaThreadScope.softwareName.get());

        if (StringUtils.containsIgnoreCase(queryString, "INFORMATION_SCHEMA")) {
            return queryString;
        } else if (isCreatingDataSourceOracle(command)) {
            String limitPresentationTables = getProperties().getProperty("stimulsoft.datasource.limitPresentationTables", "20");
            return "ALL".equals(limitPresentationTables) ? queryString : queryString.concat(" AND ROWNUM <= " + limitPresentationTables);
        }

        if (BooleanUtils.toBooleanObject(getProperties().getProperty("stimulsoft.search.usesObj", "false")) && GumgaThreadScope.organizationCode.get() != null) {
            if (!StringUtils.containsIgnoreCase(queryString, " obj")) {
                throw new RuntimeException("report01;;That SQL must contains on the primary table the alias obj");
            } else {
                return oorganizationFilterQuery("obj", queryString);
            }
        }

        if (StringUtils.containsIgnoreCase(queryString, "[[filterOi:") && GumgaThreadScope.organizationCode.get() != null) {
            return filterOi(queryString);
        }

        return queryString;
    }

    public static String filterOi(String select) {
        String newSelect = select;
        Matcher m = Pattern.compile("\\[\\[filterOi:\\w+\\]\\]").matcher(select);
        while (m.find()) {
            String substring = select.substring(m.start(), m.end());
            String[] split = substring.split(":");
            String alias = split[1].substring(0, split[1].length() - 2);
            String oiFilter = ("(" + alias + ".oi is null or " + alias + ".oi like '" + GumgaThreadScope.organizationCode.get() + "%')");
            newSelect = newSelect.replace(substring, oiFilter);
        }
        return newSelect;
    }

    private String oorganizationFilterQuery(String alias, String queryString) {
        if (StringUtils.containsIgnoreCase(queryString, "WHERE")) {
            String queryBegin = queryString.substring(0, queryString.toLowerCase().indexOf("WHERE".toLowerCase()) + 5);
            String queryEnd = queryString.substring(queryString.toLowerCase().indexOf("WHERE".toLowerCase()) + 5, queryString.length());
            return queryBegin.concat(" (" + alias + ".oi is null or " + alias + ".oi like '" + GumgaThreadScope.organizationCode.get() + "%') and ").concat(queryEnd);
        } else if (StringUtils.containsIgnoreCase(queryString, "GROUP BY")) {
            String queryBegin = queryString.substring(0, queryString.toLowerCase().indexOf("GROUP BY".toLowerCase()));
            String queryEnd = queryString.substring(queryString.toLowerCase().indexOf("GROUP BY".toLowerCase()), queryString.length());
            return queryBegin.concat(" WHERE (" + alias + ".oi is null or " + alias + ".oi like '" + GumgaThreadScope.organizationCode.get() + "%') ").concat(queryEnd);
        } else if (StringUtils.containsIgnoreCase(queryString, "ORDER BY")) {
            String queryBegin = queryString.substring(0, queryString.toLowerCase().indexOf("ORDER BY".toLowerCase()));
            String queryEnd = queryString.substring(queryString.toLowerCase().indexOf("ORDER BY".toLowerCase()), queryString.length());
            return queryBegin.concat(" WHERE (" + alias + ".oi is null or " + alias + ".oi like '" + GumgaThreadScope.organizationCode.get() + "%') ").concat(queryEnd);
        } else {
            return queryString.concat(" WHERE (" + alias + ".oi is null or " + alias + ".oi like '" + GumgaThreadScope.organizationCode.get() + "%')");
        }
    }

    private String onQuery(ResultSet rs) throws SQLException {
        List<String> columns = new ArrayList<String>();
        List<List<String>> rows = new ArrayList<List<String>>();
        boolean isColumnsFill = false;
        while (rs.next()) {
            List<String> row = new ArrayList<String>();
            for (int index = 1; index <= rs.getMetaData().getColumnCount(); index++) {
                if (!isColumnsFill) {
                    columns.add(rs.getMetaData().getColumnName(index));
                }
                String value = rs.getString(index) != null ? rs.getString(index) : "";
                row.add(value);
            }
            rows.add(row);
            isColumnsFill = true;
        }
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        result.put("columns", columns);
        result.put("rows", rows);
        return new JSONObject(result).toString();
    }

    public String process(InputStream is) throws IOException, SQLException, JSONException {
        BufferedReader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        StringBuilder command = new StringBuilder();
        String str = null;
        while ((str = r.readLine()) != null) {
            command.append(str);
        }
        command = mountConnection(command);
        return connect(new JSONObject(command.toString()));
    }


    private String replaceProperties(String str) {
        String newStr = str;
        Matcher m = Pattern.compile("\\[\\[[\\w\\.]+\\]\\]").matcher(str);
        while (m.find()) {
            String substring = str.substring(m.start(), m.end());
            String property = "stimulsoft." + substring.substring(2, substring.length() - 2);
            newStr = newStr.replace(substring, getProperty(property));
        }
        return newStr;
    }

    private StringBuilder mountConnection(StringBuilder command) {
        StrBuilder conn = new StrBuilder();
        conn.append(command.toString());
        conn.replaceFirst("%address", getProperties().getProperty("stimulsoft.database.url"));
        conn.replaceFirst("%db", getProperties().getProperty("stimulsoft.database.name"));
        conn.replaceFirst("%schema", getProperties().getProperty("stimulsoft.schema.name"));
        conn.replaceFirst("%user", getProperties().getProperty("stimulsoft.database.user"));
        conn.replaceFirst("%pass", getProperties().getProperty("stimulsoft.database.password"));
        StringBuilder comm = new StringBuilder();
        comm.append(replaceProperties(conn.toString()));
        return comm;
    }

    private Map<String, String> parseParams(String string) {
        String[] keyValues = string.split(";");
        Map<String, String> result = new HashMap<String, String>();
        for (String element : keyValues) {
            String[] keyValue = element.split("=", 2);
            String originalKey = keyValue[0];
            String lowerKey = originalKey.trim().toLowerCase();
            String value = keyValue.length > 1 ? keyValue[1].trim() : "";
            result.put(lowerKey, value);
        }
        return result;
    }

    private String getUrl(Map<String, String> params) {
        return getLeastOne(params, URL_KEYS);
    }

    private String getDatabase(Map<String, String> params) {
        return getLeastOne(params, DATABASE_KEY);
    }

    private String getPort(Map<String, String> params) {
        return getLeastOne(params, PORT_KEY);
    }

    private String getHost(Map<String, String> params) {
        return getLeastOne(params, HOST_KEY);
    }

    private String getUser(Map<String, String> params) {
        return getLeastOne(params, USERS_KEYS);
    }

    private String getPassword(Map<String, String> params) {
//        System.out.println("---->getPassword--->" + params);
        return getLeastOne(params, PASSWORD_KEYS);
    }

    private String getLeastOne(Map<String, String> params, List<String> keys) {
        for (final String key : keys) {
            final String value = params.get(key);
            if (value != null) {
                params.remove(key);
                return value;
            }
        }
        return null;
    }

}
