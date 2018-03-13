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
            Object field = cast.get("field");
            value = new CriteriaField(field.toString());
        }
        return value;
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
        FormatGQuery format = new FormatterGQuery();

        return format.execute(field, comparisonOperator, value, fieldFunction, valueFunction);

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
    private final String[] formats = {
            "yyyy-MM-dd'T'HH:mm:ss'Z'",   "yyyy-MM-dd'T'HH:mm:ssZ",
            "yyyy-MM-dd'T'HH:mm:ss",      "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            "yyyy-MM-dd'T'HH:mm:ss.SSSZ", "yyyy-MM-dd HH:mm:ss",
            "MM/dd/yyyy HH:mm:ss",        "MM/dd/yyyy'T'HH:mm:ss.SSS'Z'",
            "MM/dd/yyyy'T'HH:mm:ss.SSSZ", "MM/dd/yyyy'T'HH:mm:ss.SSS",
            "MM/dd/yyyy'T'HH:mm:ssZ",     "MM/dd/yyyy'T'HH:mm:ss",
            "yyyy:MM:dd HH:mm:ss",
            "yyyy-MM-dd", "yyyy/MM/dd"
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
//                    System.out.println("Trying to converter date to pattern: " + parse);
                }
            }
        }
        return null;
    }

    private Boolean isDate(Object value, Date parse) {
        return value instanceof Date || value instanceof LocalDate || value instanceof LocalDateTime || parse != null;
    }

    public static void main(String[] args) {
//        GQuery unconcilied = new GQuery(new Criteria("obj.conciliation.id", ComparisonOperator.EQUAL, 1))
//                .and(new Criteria("obj.situation", ComparisonOperator.EQUAL, "UNCONCILIED"))
//                .and(new Criteria("obj.justified", ComparisonOperator.EQUAL, false));
//        GQuery join = new GQuery(new Criteria("acc.type", ComparisonOperator.NOT_EQUAL, "NOT_FOUND"))
//                .join(new Join("obj.accusations as acc", JoinType.LEFT));
//        GQuery and = unconcilied.and(join);
//        System.out.println(and.toString());
//        System.out.println(and.getJoins());

        Number value = 10l;

        System.out.println(String.format("prod.nome = '%s'", value.toString().replaceAll("\'", "''")));

    }
}


interface FormatGQuery extends FormatWhereGQuery {

    @Override
    default String execute(Object field, ComparisonOperator operator, Object value, String fieldFunction, String valueFunction) {
        return get().get(0).execute(field, operator, value, fieldFunction, valueFunction);
    }

    default List<FormatNumberWhereGQuery> get() {
        return Arrays.asList(new FormatNumberWhereGQuery(new FormatStringWhereGQuery()));
    }
}

class FormatterGQuery implements FormatGQuery {

}

interface FormatWhereGQuery {
    String execute(Object field, ComparisonOperator operator, Object value, String fieldFunction, String valueFunction);
}

class FormatNumberWhereGQuery implements FormatWhereGQuery {
    private FormatWhereGQuery delegate;

    public FormatNumberWhereGQuery(FormatWhereGQuery delegate) {
        this.delegate = delegate;
    }

    @Override
    public String execute(Object field, ComparisonOperator operator, Object value, String fieldFunction, String valueFunction) {
        if(value instanceof Number) {

        }

        return delegate.execute(field, operator, value, fieldFunction, valueFunction);
    }
}

class FormatStringWhereGQuery implements FormatWhereGQuery {

    @Override
    public String execute(Object field, ComparisonOperator operator, Object value, String fieldFunction, String valueFunction) {
        return String.format(fieldFunction, field) + operator.hql + String.format(valueFunction, "\'" + value.toString().replaceAll("\'", "''") + "\'");
    }
}











