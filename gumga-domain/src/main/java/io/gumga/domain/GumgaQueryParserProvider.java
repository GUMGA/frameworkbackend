package io.gumga.domain;

import br.com.insula.opes.CpfCnpj;
import io.gumga.domain.domains.usertypes.*;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.util.concurrent.TimeUnit.DAYS;
import static org.hibernate.criterion.Restrictions.eq;

/**
 * Classe utilizada para especificar como cada tipo de dados será pequisado em
 * cada SGBD
 *
 * @author munif
 */
public class GumgaQueryParserProvider {

    public static Map<Class<?>, CriterionParser> defaultMap = null;

    private GumgaQueryParserProvider() {
    }

    public static final Map<Class<?>, CriterionParser> getH2LikeMap() {
        return getBaseMap();
    }

    public static final Map<Class<?>, CriterionParser> getOracleLikeMap() {
        Map<Class<?>, CriterionParser> oracleMap = getBaseMap();
        oracleMap.put(String.class, AbstractStringCriterionParser.ORACLE_STRING_CRITERION_PARSER);
        return oracleMap;
    }

    public static final Map<Class<?>, CriterionParser> getMySqlLikeMap() {
        Map<Class<?>, CriterionParser> mySqlMap = getBaseMap();
        mySqlMap.put(String.class, AbstractStringCriterionParser.MYSQL_STRING_CRITERION_PARSER);
        return mySqlMap;
    }

    public static final Map<Class<?>, CriterionParser> getPostgreSqlLikeMap() {
        Map<Class<?>, CriterionParser> mySqlMap = getBaseMap();
        mySqlMap.put(String.class, AbstractStringCriterionParser.POSTGRESQL_STRING_CRITERION_PARSER);
        return mySqlMap;
    }

    protected static final CriterionParser STRING_CRITERION_PARSER_WITHOUT_TRANSLATE = (field, value) -> {

        value = Normalizer.normalize(value, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");

        String[] chain = field.split("\\.");

        if (chain.length > 1) {
            return Restrictions.like(field, value, MatchMode.ANYWHERE).ignoreCase();
        }
        String caseInsensitive = "upper({alias}." + field + ") like (?)";

        return Restrictions.sqlRestriction(caseInsensitive, "%" + value + "%", StandardBasicTypes.STRING);
    };

    /**
     * Use <code>gumga.framework.domain.AbstractStringCriterionParser</code>
     * instead
     *
     * @deprecated
     */
    @Deprecated
    private static final CriterionParser STRING_CRITERION_PARSER = (field, value) -> {

        value = Normalizer.normalize(value, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");

        String[] chain = field.split("\\.");

        if (chain.length > 1) {
            return Restrictions.like(field, value, MatchMode.ANYWHERE).ignoreCase();
        }
        String ignoraAcentos = "upper({alias}." + field + ") like (?)";

        ignoraAcentos = "upper(translate({alias}." + field + ",'" + AbstractStringCriterionParser.SOURCE_CHARS + "','" + AbstractStringCriterionParser.TARGET_CHARS + "')) like (?)"; //NAO FUNCIONA NO MYSQL

        return Restrictions.sqlRestriction(ignoraAcentos, "%" + value + "%", StandardBasicTypes.STRING);
    };

    private static final CriterionParser CHARACTER_CRITERION_PARSER = (field, value) -> {
        if (value.length() == 1) {
            return eq(field, new Character(value.charAt(0)));
        }

        throw new IllegalArgumentException(value);
    };

    private static final CriterionParser BOOLEAN_CRITERION_PARSER = (field, value) -> {
        value = value.toLowerCase();

        if (value.equals("sim")) {
            value = "true";
        } else if (value.equals("não") || value.equals("nao")) {
            value = "false";
        }

        if (value.equals("true") || value.equals("false")) {
            return eq(field, new Boolean(value));
        }

        throw new IllegalArgumentException(value);
    };

    private static final CriterionParser SHORT_CRITERION_PARSER = (field, value) -> eq(field, new Short(value));

    private static final CriterionParser INTEGER_CRITERION_PARSER = (field, value) -> eq(field, new Integer(value));

    private static final CriterionParser LONG_CRITERION_PARSER = (field, value) -> eq(field, new Long(value));

    private static final CriterionParser FLOAT_CRITERION_PARSER = (field, value) -> eq(field, new Float(value));

    private static final CriterionParser DOUBLE_CRITERION_PARSER = (field, value) -> eq(field, new Double(value));

    private static final CriterionParser BIGINTEGER_CRITERION_PARSER = (field, value) -> eq(field, new BigInteger(value));

    private static final CriterionParser BIGDECIMAL_CRITERION_PARSER = (field, value) -> eq(field, new BigDecimal(value));

    private static final CriterionParser DATE_CRITERION_PARSER = (field, value) -> {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

            Date minDate = formatter.parse(value);
            Date maxDate = new Date(minDate.getTime() + DAYS.toMillis(1));

            Conjunction and = Restrictions.conjunction();
            and.add(Restrictions.ge(field, minDate));
            and.add(Restrictions.lt(field, maxDate));

            return and;
        } catch (ParseException e) {
            throw new IllegalArgumentException(value);
        }
    };

    private static final Map<Class<?>, CriterionParser> getBaseMap() {
        Map<Class<?>, CriterionParser> parsers = new HashMap<>();
        parsers.put(String.class, STRING_CRITERION_PARSER_WITHOUT_TRANSLATE);
        parsers.put(Character.class, CHARACTER_CRITERION_PARSER);
        parsers.put(char.class, CHARACTER_CRITERION_PARSER);
        parsers.put(Boolean.class, BOOLEAN_CRITERION_PARSER);
        parsers.put(boolean.class, BOOLEAN_CRITERION_PARSER);
        parsers.put(Short.class, SHORT_CRITERION_PARSER);
        parsers.put(short.class, SHORT_CRITERION_PARSER);
        parsers.put(Integer.class, INTEGER_CRITERION_PARSER);
        parsers.put(int.class, INTEGER_CRITERION_PARSER);
        parsers.put(Long.class, LONG_CRITERION_PARSER);
        parsers.put(long.class, LONG_CRITERION_PARSER);
        parsers.put(Float.class, FLOAT_CRITERION_PARSER);
        parsers.put(float.class, FLOAT_CRITERION_PARSER);
        parsers.put(Double.class, DOUBLE_CRITERION_PARSER);
        parsers.put(double.class, DOUBLE_CRITERION_PARSER);
        parsers.put(BigInteger.class, BIGINTEGER_CRITERION_PARSER);
        parsers.put(BigDecimal.class, BIGDECIMAL_CRITERION_PARSER);
        parsers.put(Date.class, DATE_CRITERION_PARSER);
        parsers.put(CpfCnpj.class, STRING_CRITERION_PARSER_WITHOUT_TRANSLATE); //Domínio da Insula utilizado na DB1
        parsers.put(GumgaAddressUserType.class, STRING_CRITERION_PARSER_WITHOUT_TRANSLATE);
        parsers.put(GumgaBooleanUserType.class, BOOLEAN_CRITERION_PARSER);
        parsers.put(GumgaBarCodeUserType.class, STRING_CRITERION_PARSER_WITHOUT_TRANSLATE);
        parsers.put(GumgaCEPUserType.class, STRING_CRITERION_PARSER_WITHOUT_TRANSLATE);
        parsers.put(GumgaCNPJUserType.class, STRING_CRITERION_PARSER_WITHOUT_TRANSLATE);
        parsers.put(GumgaCPFUserType.class, STRING_CRITERION_PARSER_WITHOUT_TRANSLATE);
        parsers.put(GumgaEMailUserType.class, STRING_CRITERION_PARSER_WITHOUT_TRANSLATE);
        parsers.put(GumgaGeoLocationUserType.class, STRING_CRITERION_PARSER_WITHOUT_TRANSLATE);
        parsers.put(GumgaIP4UserType.class, STRING_CRITERION_PARSER_WITHOUT_TRANSLATE);
        parsers.put(GumgaIP6UserType.class, STRING_CRITERION_PARSER_WITHOUT_TRANSLATE);
        parsers.put(GumgaMoneyUserType.class, BIGDECIMAL_CRITERION_PARSER);
        parsers.put(GumgaMultiLineStringUserType.class, STRING_CRITERION_PARSER_WITHOUT_TRANSLATE);
        parsers.put(GumgaPhoneNumberUserType.class, STRING_CRITERION_PARSER_WITHOUT_TRANSLATE);
        parsers.put(GumgaTimeUserType.class, STRING_CRITERION_PARSER_WITHOUT_TRANSLATE);
        parsers.put(GumgaOiUserType.class, STRING_CRITERION_PARSER_WITHOUT_TRANSLATE);
        parsers.put(GumgaURLUserType.class, STRING_CRITERION_PARSER_WITHOUT_TRANSLATE);

        return parsers;
    }

}
