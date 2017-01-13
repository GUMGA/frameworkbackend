/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.application.spring.config;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.hibernate.cfg.NamingStrategy;
import org.hibernate.internal.util.StringHelper;

/**
 *
 * @author felipe e munif
 */
public class GumgaNamingStrategy implements NamingStrategy, Serializable {

    public static final String[] RESERVED_WORDS = {
        //ORACLE
        "ACCESS", "ADD", "ALL", "ALTER", "AND", "ANY", "AS", "ASC", "AUDIT", "BETWEEN", "BY", "CHAR", "CHECK", "CLUSTER", "COLUMN", "COMMENT",
        "COMPRESS", "CONNECT", "CREATE", "CURRENT", "DATE", "DECIMAL", "DEFAULT", "DELETE", "DESC", "DISTINCT", "DROP", "ELSE", "EXCLUSIVE",
        "EXISTS", "FILE", "FLOAT", "FOR", "FROM", "GRANT", "GROUP", "HAVING", "IDENTIFIED", "IMMEDIATE", "IN", "INCREMENT", "INDEX", "INITIAL",
        "INSERT", "INTEGER", "INTERSECT", "INTO", "IS", "LEVEL", "LIKE", "LOCK", "LONG", "MAXEXTENTS", "MINUS", "MLSLABEL", "MODE", "MODIFY",
        "NOAUDIT", "NOCOMPRESS", "NOT", "NOWAIT", "NULL", "NUMBER", "OF", "OFFLINE", "ON", "ONLINE", "OPTION", "OR", "ORDER", "PCTFREE", "PRIOR",
        "PRIVILEGES", "PUBLIC", "RAW", "RENAME", "RESOURCE", "REVOKE", "ROW", "ROWID", "ROWNUM", "ROWS", "SELECT", "SESSION", "SET", "SHARE", "SIZE",
        "SMALLINT", "START", "SUCCESSFUL", "SYNONYM", "SYSDATE", "TABLE", "THEN", "TO", "TRIGGER", "UID", "UNION", "UNIQUE", "UPDATE", "USER",
        "VALIDATE", "VALUES", "VARCHAR", "VARCHAR2", "VIEW", "WHENEVER", "WHERE", "WITH",
        //MYSQL
        "KEY", "PASSWORD"

    };

    public static final int ORACLE_MAX_SIZE = 30;
    private final List<String> reservedWords;

    public GumgaNamingStrategy() {
        System.out.println("-----------------GumgaNamingStrategy BETA -----------------------------");
        reservedWords = Arrays.asList(RESERVED_WORDS);
    }

    @Override
    public String classToTableName(String string) {
        String table = StringHelper.unqualify(string);
        return oracleSafe(table).toUpperCase();
    }

    @Override
    public String collectionTableName(String ownerEntity, String ownerEntityTable, String associatedEntity, String associatedEntityTable,
            String propertyName) {
        return tableName(
                new StringBuilder(ownerEntityTable).append("_")
                .append(
                        associatedEntityTable != null
                                ? associatedEntityTable
                                : StringHelper.unqualify(propertyName)
                ).toString()
        );
    }

    @Override
    public String columnName(String string) {
        return oracleSafe(string);
    }

    @Override
    public String foreignKeyColumnName(String propertyName, String propertyEntityName, String propertyTableName, String referencedColumnName) {
        String header = propertyName != null ? StringHelper.unqualify(propertyName) : propertyTableName;
        Objects.requireNonNull(header, "GumgaNamingStrategy not properly filled on foreignKeyColumnName");
        return columnName(header + "_" + referencedColumnName);
    }

    @Override
    public String joinKeyColumnName(String joinedColumn, String joinedTable) {
        return columnName(joinedColumn);
    }

    @Override
    public String logicalCollectionColumnName(String columnName, String propertyName, String referencedColumn) {
        return oracleSafe(StringHelper.isNotEmpty(columnName) ? columnName : StringHelper.unqualify(propertyName) + "_" + referencedColumn);
    }

    @Override
    public String logicalCollectionTableName(String tableName, String ownerEntityTable, String associatedEntityTable, String propertyName) {
        if (tableName != null) {
            return oracleSafe(tableName);
        } else {
            //use of a stringbuffer to workaround a JDK bug
            return oracleSafe(new StringBuffer(ownerEntityTable).append("_").append(associatedEntityTable != null ? associatedEntityTable : StringHelper.unqualify(propertyName)).toString());
        }
    }

    @Override
    public String logicalColumnName(String columnName, String propertyName) {
        return oracleSafe(StringHelper.isNotEmpty(columnName) ? columnName : StringHelper.unqualify(propertyName));
    }

    @Override
    public String propertyToColumnName(String propertyName) {
        return oracleSafe(StringHelper.unqualify(propertyName));
    }

    @Override
    public String tableName(String string) {
        return oracleSafe(string);
    }

    private String oracleSafe(String name) {
        boolean loga = false;
        String originalName = name;

        if (name.length() >= ORACLE_MAX_SIZE) {
            loga = true;
            String initials = "";
            int lastPos = 0;
            int i = 0;
            for (char c : name.toCharArray()) {
                i++;
                if (Character.isUpperCase(c) || i == 1) {
                    initials += c;
                    lastPos = i;
                }

            }
            initials += name.substring(lastPos);
            name = initials;
        }
        name = name.toUpperCase();
        if (reservedWords.contains(name)) {
            loga = true;
            name = "G_" + name;
        }
        if (name.length() >= ORACLE_MAX_SIZE) {
            loga = true;
        }
        name = name.substring(0, name.length() >= ORACLE_MAX_SIZE ? ORACLE_MAX_SIZE : name.length());

        if (loga) {
            System.out.println("--- GumgaNamingStrategy -->" + originalName + "=>" + name);
        }

        return name;
    }

}
