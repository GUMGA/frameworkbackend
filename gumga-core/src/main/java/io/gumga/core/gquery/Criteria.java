package io.gumga.core.gquery;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Critério de busca utilizado no {@link GQuery}
 */
public class Criteria implements Serializable {

    public static final String SOURCE_CHARS = "'âàãáÁÂÀÃéêÉÊíÍóôõÓÔÕüúÜÚÇç'";
    public static final String TARGET_CHARS = "'AAAAAAAAEEEEIIOOOOOOUUUUCC'";
    public static final String TO_TIMESTAMP_S_YYYY_MM_DD_HH24_MI_SS = "to_timestamp(:%s, 'yyyy/MM/dd HH24:mi:ss')";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String PARAM1_AND_PARAM2 = ":%s and :%s";
    public static final String START_TIME = " 00:00:00";
    public static final String END_TIME = " 23:59:59";
    public final Map<String, Object> fieldValue = new HashMap<>();


    public static boolean doTranslate = true;
    /**
     * Campo a ser levado em consideração no critério de busca
     */
    private Object field;
    /**
     * Operador de comparação
     */
    private ComparisonOperator comparisonOperator;
    /**
     * Valor a ser levado em consideração no critério de busca
     */
    private Object value;
    /**
     * Valores a ser levado em consideração no critério de busca
     */
    private Object[] values;
    /**
     * Função de formatação do campo, exemplo: %s
     */
    private String fieldFunction;
    /**
     * Função de formatação do valor, exemplo: %s
     */
    private String valueFunction;

    private void init() {
        comparisonOperator = ComparisonOperator.EQUAL;
        fieldFunction = "%s";
        valueFunction = "%s";
    }

    public Criteria() {
        field = 1;
        value = 1;
        init();
    }

    public Criteria(Object field, ComparisonOperator comparisonOperator, Object value) {
        init();
        this.field = field;

        this.comparisonOperator = comparisonOperator;
        this.value = value;
    }

    public Object getField() {
        return field;
    }

    public void setField(Object field) {
        this.field = field;
    }

    public ComparisonOperator getComparisonOperator() {
        return comparisonOperator;
    }

    public void setComparisonOperator(ComparisonOperator comparisonOperator) {
        this.comparisonOperator = comparisonOperator;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Object[] getValues() {
        return values;
    }

    public void setValues(Object[] values) {
        this.values = values;
    }

    public String getFieldFunction() {
        return fieldFunction;
    }

    public void setFieldFunction(String fieldFunction) {
        this.fieldFunction = fieldFunction;
    }

    public String getValueFunction() {
        return valueFunction;
    }

    public void setValueFunction(String valueFunction) {
        this.valueFunction = valueFunction;
    }

    /**
     * Converte Mapa de campos (Map<field, algumvalor>) para Campos utilizados no critério de busca
     * @param value
     * @return
     */
    private Object convertMapInCriteriaField(Object value) {
        Map cast = Map.class.cast(value);
        if(cast.containsKey("field")) {
            return new CriteriaField(cast.get("field").toString());
        }
        return value;
    }

    public static String generateHash(Object field) {
        return "h".concat(Integer.toString(Math.abs(field.toString().hashCode())));
    }

    @Override
    public String toString() {
        Object value = this.value;

        if(value == null) {
            return field + comparisonOperator.hql + " null";
        }

        if(value instanceof Map) {
            Object result = convertMapInCriteriaField(value);
            if(result instanceof CriteriaField) {
                this.value = result;
            }
            value = result;
        }

        if (ComparisonOperator.STARTS_WITH.equals(this.comparisonOperator)) {
            value = value + "%";
        } else if (ComparisonOperator.ENDS_WITH.equals(this.comparisonOperator)) {
            value = "%" + value;
        } else if (ComparisonOperator.CONTAINS.equals(this.comparisonOperator) || ComparisonOperator.NOT_CONTAINS.equals(this.comparisonOperator)) {
            value = "%" + value + "%";
        }

        if(ComparisonOperator.IN.equals(this.comparisonOperator)) {
            return resolveIn(value);
        }

        if(ComparisonOperator.BETWEEN.equals(this.comparisonOperator)) {
            return resolveBetween(value);
        }

        return resolveDefault(value);

        /*
//        ComparisonOperatorProcess comparisonOperatorProcess = new ComparisionOperatorProcessEqual();
//        return comparisonOperatorProcess.process(field, comparisonOperator, value, fieldFunction, valueFunction);

        if (ComparisonOperator.STARTS_WITH.equals(this.comparisonOperator)) {
            value = value + "%";
        } else if (ComparisonOperator.ENDS_WITH.equals(this.comparisonOperator)) {
            value = "%" + value;
        } else if (ComparisonOperator.CONTAINS.equals(this.comparisonOperator) || ComparisonOperator.NOT_CONTAINS.equals(this.comparisonOperator)) {
            value = "%" + value + "%";
        }

        if(ComparisonOperator.IN.equals(this.comparisonOperator)) {
            if(value instanceof Collection) {
                Collection values = (Collection) value;
                String v = "";
                for (Object object :values) {
                    if(object instanceof Number || object instanceof CriteriaField) {
                        v += object + ",";
                    } else {
                        v += "'"+ object.toString() + "',";
                    }
                }
                v = v.substring(0, v.length()-1);
                return field + comparisonOperator.hql + "(" + v + ")";
            } else {
                if(value instanceof Number || value instanceof CriteriaField) {
                    return  field + comparisonOperator.hql + "(" + value.toString() +")";
                }
            }

            return  field + comparisonOperator.hql + "('" + value.toString() +"')";
        }

        if(value instanceof CriteriaField || value instanceof Boolean) {
            return field + comparisonOperator.hql + value;
        }

        if(ComparisonOperator.IN_ELEMENTS.equals(this.comparisonOperator)) {
            if(value instanceof Number) {
                return value + comparisonOperator.hql + "(" + field + ")";
            }
            return "'"+value+"'" + comparisonOperator.hql + "(" + field + ")";
        }


        if(ComparisonOperator.BETWEEN.equals(this.comparisonOperator)) {
            if(value instanceof Collection) {
                Collection values = (Collection) value;
                Object[] objects = values.toArray();
                if(values.size() >= 2) {

                    if(objects[0] instanceof Number) {
                        return field + comparisonOperator.hql +  objects[0] + " AND " + objects[1];
                    } else {
                        Date parse = parse(String.valueOf(objects[0]));
                        if(isDate(objects[0], parse)) {
                            Date parse2 = parse(String.valueOf(objects[1]));
                            String format1 = new SimpleDateFormat("yyyy-MM-dd").format(parse != null ? parse : objects[0]);
                            String format2 = new SimpleDateFormat("yyyy-MM-dd").format(parse2 != null ? parse2 : objects[1]);

                            return field + comparisonOperator.hql + String.format("to_timestamp('%s 00:00:00','yyyy/MM/dd HH24:mi:ss')", format1) + " AND " + String.format("to_timestamp('%s 23:59:59','yyyy/MM/dd HH24:mi:ss')", format2);
                        }
                    }
                    return field + comparisonOperator.hql + "'" + objects[0] + "' AND '" + objects[1] + "'";
                }

                if(objects[0] instanceof Number) {
                    return field + comparisonOperator.hql +  objects[0] + " AND " + objects[0];
                } else {
                    Date parse = parse(String.valueOf(objects[0]));
                    if(isDate(objects[0], parse)) {
                        String format1 = new SimpleDateFormat("yyyy-MM-dd").format(parse != null ? parse : objects[0]);
                        return field + comparisonOperator.hql + String.format("to_timestamp('%s 00:00:00','yyyy/MM/dd HH24:mi:ss')", format1) + " AND " + String.format("to_timestamp('%s 23:59:59','yyyy/MM/dd HH24:mi:ss')", format1);
                    }
                }

                return field + comparisonOperator.hql + "'" + objects[0] + "' AND '" + objects[0] + "'";
            }

            if(value instanceof Number) {
                return field + comparisonOperator.hql +  value + " AND " + value;
            }  else {
                Date parse = parse(String.valueOf(value));
                if(isDate(value, parse)) {
                    String format1 = new SimpleDateFormat("yyyy-MM-dd").format(parse != null ? parse : value);
                    return field + comparisonOperator.hql + String.format("to_timestamp('%s 00:00:00','yyyy/MM/dd HH24:mi:ss')", format1) + " AND " + String.format("to_timestamp('%s 23:59:59','yyyy/MM/dd HH24:mi:ss')", format1);
                }
            }

            return field + comparisonOperator.hql + "'" + value + "' AND '" + value + "'";
        }



        if(value instanceof Number) {
            return field + comparisonOperator.hql + value;

        } else {
            Date parse = parse(String.valueOf(value));
            if(isDate(value, parse)) {
                String format = new SimpleDateFormat("yyyy-MM-dd").format(parse != null ? parse : value);

                switch (this.comparisonOperator) {
                    case EQUAL:
                        return field + ComparisonOperator.GREATER_EQUAL.hql + String.format("to_timestamp('%s 00:00:00','yyyy/MM/dd HH24:mi:ss')", format) + " AND " +
                                field + ComparisonOperator.LOWER_EQUAL.hql + String.format("to_timestamp('%s 23:59:59','yyyy/MM/dd HH24:mi:ss')", format);
                    case GREATER_EQUAL:
                        return field + ComparisonOperator.GREATER_EQUAL.hql + String.format("to_timestamp('%s 00:00:00','yyyy/MM/dd HH24:mi:ss')", format);
                    case GREATER:
                        return field + ComparisonOperator.GREATER.hql + String.format("to_timestamp('%s 00:00:00','yyyy/MM/dd HH24:mi:ss')", format);
                    case LOWER_EQUAL:
                        return field + ComparisonOperator.LOWER_EQUAL.hql + String.format("to_timestamp('%s 23:59:59','yyyy/MM/dd HH24:mi:ss')", format);
                    case LOWER:
                        return field + ComparisonOperator.LOWER.hql + String.format("to_timestamp('%s 23:59:59','yyyy/MM/dd HH24:mi:ss')", format);

                }
            }
        }


        return String.format(fieldFunction, field) + comparisonOperator.hql + String.format(valueFunction, "\'" + value.toString().replaceAll("\'", "''") + "\'");
        */
    }

    private String resolveIn(Object value) {
        if(value instanceof Collection) {
            Collection values = (Collection) value;
            String v = "";
            int i = 0;
            for (Object object :values) {
                i++;
                if(object instanceof CriteriaField) {
                    v += object + ",";
                } else {
                    String s = generateHash(field) + i;
                    fieldValue.put(s, object);

                    v += ":" + s + ",";
                }
            }
            v = v.substring(0, v.length()-1);
            return field + comparisonOperator.hql + "(" + v + ")";
        }

        if(value instanceof CriteriaField) {
            return  field + comparisonOperator.hql + "(" + value.toString() +")";
        }

        String param = generateHash(field) ;
        fieldValue.put(param, value);

        return  field + comparisonOperator.hql + String.format("(:%s)", param);
    }

    private String resolveDefault(Object value) {
        if(value instanceof Boolean) {
            fieldValue.put(generateHash(field), value);
        } else {
            Date parse = parse(String.valueOf(value));
            if(isDate(value, parse)) {
                String format1 = new SimpleDateFormat(YYYY_MM_DD).format(parse != null ? parse : value).concat(START_TIME);
                String format2 = new SimpleDateFormat(YYYY_MM_DD).format(parse != null ? parse : value).concat(END_TIME);
                String param1 = generateHash(field) + "A";
                String param2 = generateHash(field) + "B";
                fieldValue.put(param1, format1);
                fieldValue.put(param2, format2);
                switch (this.comparisonOperator) {
                    case EQUAL:
                        return field + ComparisonOperator.GREATER_EQUAL.hql + String.format(TO_TIMESTAMP_S_YYYY_MM_DD_HH24_MI_SS, param1) + " AND " +
                                field + ComparisonOperator.LOWER_EQUAL.hql + String.format(TO_TIMESTAMP_S_YYYY_MM_DD_HH24_MI_SS, param2);
                    case GREATER_EQUAL:
                        return field + ComparisonOperator.GREATER_EQUAL.hql + String.format(TO_TIMESTAMP_S_YYYY_MM_DD_HH24_MI_SS, param1);
                    case GREATER:
                        return field + ComparisonOperator.GREATER.hql + String.format(TO_TIMESTAMP_S_YYYY_MM_DD_HH24_MI_SS, param1);
                    case LOWER_EQUAL:
                        return field + ComparisonOperator.LOWER_EQUAL.hql + String.format(TO_TIMESTAMP_S_YYYY_MM_DD_HH24_MI_SS, param2);
                    case LOWER:
                        return field + ComparisonOperator.LOWER.hql + String.format(TO_TIMESTAMP_S_YYYY_MM_DD_HH24_MI_SS, param1);
                    default:
                        return field + ComparisonOperator.EQUAL.hql + String.format(TO_TIMESTAMP_S_YYYY_MM_DD_HH24_MI_SS, param1);
                }
            } else if(value instanceof CriteriaField) {
                return field + comparisonOperator.hql + value;
            } else {
                fieldValue.put(generateHash(field), value);
            }
        }

        String parameter = String.format(":%s", generateHash(field));

        return String.format(fieldFunction, field) + comparisonOperator.hql + String.format(valueFunction, parameter);
    }

    private String resolveBetween(Object value) {
        if(value instanceof Collection) {
            Collection values = (Collection) value;
            Object[] objects = values.toArray();
            if(values.size() >= 2) {

                if(objects[0] instanceof Number) {
                    String paramNumber1 = generateHash(field) + "A";
                    String paramNumber2 = generateHash(field) + "B";
                    fieldValue.put(paramNumber1, objects[0]);
                    fieldValue.put(paramNumber2, objects[1]);

                    return field + comparisonOperator.hql +  String.format(PARAM1_AND_PARAM2, paramNumber1, paramNumber2);
                } else {
                    Date parse = parse(String.valueOf(objects[0]));
                    if(isDate(objects[0], parse)) {
                        Date parse2 = parse(String.valueOf(objects[1]));
                        String format1 = createDateStart(objects, 0, parse);
                        String format2 = createDateEnd(objects, 1, parse2);
                        String paramDate1 = generateHash(field) + "A";
                        String paramDate2 = generateHash(field) + "B";
                        fieldValue.put(paramDate1, format1);
                        fieldValue.put(paramDate2, format2);

                        return field + comparisonOperator.hql + String.format(TO_TIMESTAMP_S_YYYY_MM_DD_HH24_MI_SS, paramDate1) + " AND " + String.format(TO_TIMESTAMP_S_YYYY_MM_DD_HH24_MI_SS, paramDate2);
                    }
                }

                String param1 = generateHash(field) + "A";
                String param2 = generateHash(field) + "B";
                fieldValue.put(param1, objects[0]);
                fieldValue.put(param2, objects[1]);
                return field + comparisonOperator.hql + String.format(PARAM1_AND_PARAM2, param1, param2);
            }

            if(objects[0] instanceof Number) {
                String param1 = generateHash(field) + "A";
                fieldValue.put(param1, objects[0]);
                return field + comparisonOperator.hql + String.format(PARAM1_AND_PARAM2, param1, param1);
            } else {
                Date parse = parse(String.valueOf(objects[0]));
                if(isDate(objects[0], parse)) {
                    String format1 = createDateStart(objects, 0, parse);
                    String format2 = createDateEnd(objects, 0, parse);
                    String param1 = generateHash(field) + "A";
                    String param2 = generateHash(field) + "B";
                    fieldValue.put(param1, format1);
                    fieldValue.put(param2, format2);
                    return field + comparisonOperator.hql + String.format(TO_TIMESTAMP_S_YYYY_MM_DD_HH24_MI_SS, param1) + " AND " + String.format(TO_TIMESTAMP_S_YYYY_MM_DD_HH24_MI_SS, param2);
                }
            }

            String param1 = generateHash(field) + "A";
            fieldValue.put(param1, objects[0]);
            return field + comparisonOperator.hql + String.format(PARAM1_AND_PARAM2, param1, param1);
        }

        if(value instanceof Number) {
            return field + comparisonOperator.hql +  value + " AND " + value;
        }  else {
            Date parse = parse(String.valueOf(value));
            if(isDate(value, parse)) {
                String format1 = new SimpleDateFormat(YYYY_MM_DD).format(parse != null ? parse : value).concat(START_TIME);
                String format2 = new SimpleDateFormat(YYYY_MM_DD).format(parse != null ? parse : value).concat(END_TIME);
                String param1 = generateHash(field) + "A";
                String param2 = generateHash(field) + "B";
                fieldValue.put(param1, format1);
                fieldValue.put(param2, format2);

                return field + comparisonOperator.hql + String.format(TO_TIMESTAMP_S_YYYY_MM_DD_HH24_MI_SS, param1) + " AND " + String.format(TO_TIMESTAMP_S_YYYY_MM_DD_HH24_MI_SS, param2);
            }
        }

        String param1 = generateHash(field);
        fieldValue.put(param1, value);
        return field + comparisonOperator.hql + String.format(PARAM1_AND_PARAM2, param1, param1);
    }

    private String createDateEnd(Object[] objects, int index, Date parse2) {
        return new SimpleDateFormat(YYYY_MM_DD).format(parse2 != null ? parse2 : objects[index]).concat(END_TIME);
    }

    private String createDateStart(Object[] objects, int index, Date parse) {
        return new SimpleDateFormat(YYYY_MM_DD).format(parse != null ? parse : objects[index]).concat(START_TIME);
    }

    public Criteria addIgnoreCase() {
        fieldFunction = String.format(fieldFunction, "lower(%s)");
        valueFunction = String.format(valueFunction, "lower(%s)");
        return this;
    }

    /**
     * Adiciona translate no critério (ignora caracteres especiais)
     * @return Critério de busca
     */
    public Criteria addTranslate() {
        if (doTranslate) {
            fieldFunction = String.format(fieldFunction, "translate(%s," + SOURCE_CHARS + "," + TARGET_CHARS + ")");
            valueFunction = String.format(fieldFunction, "translate(%s," + SOURCE_CHARS + "," + TARGET_CHARS + ")");
        }
        return this;
    }

    /**
     * Formatos de datas utilizados na conversão das mesmas no critério de busca
     */
    private static final String[] formats = {
            "yyyy-MM-dd'T'HH:mm:ss'Z'",   "yyyy-MM-dd'T'HH:mm:ssZ",
            "yyyy-MM-dd'T'HH:mm:ss",      "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            "yyyy-MM-dd'T'HH:mm:ss.SSSZ", "yyyy-MM-dd HH:mm:ss",
            "MM/dd/yyyy HH:mm:ss",        "MM/dd/yyyy'T'HH:mm:ss.SSS'Z'",
            "MM/dd/yyyy'T'HH:mm:ss.SSSZ", "MM/dd/yyyy'T'HH:mm:ss.SSS",
            "MM/dd/yyyy'T'HH:mm:ssZ",     "MM/dd/yyyy'T'HH:mm:ss",
            "yyyy:MM:dd HH:mm:ss",
            YYYY_MM_DD, "yyyy/MM/dd"
    };

    /**
     * Cria data a partir de uma String levando em consideração os formatos possíveis
     * @param d String
     * @return Data
     */
    private Date parse(String d) {
        SimpleDateFormat sdf;
        if (d != null) {
            for (String parse : formats) {
                sdf = new SimpleDateFormat(parse);
                try {
                    return sdf.parse(d);
                } catch (ParseException e) {
                }
            }
        }
        return null;
    }

    private Boolean isDate(Object value, Date parse) {
        return value instanceof Date || value instanceof LocalDate || value instanceof LocalDateTime || parse != null;
    }

}






