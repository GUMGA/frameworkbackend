package io.gumga.core.gquery;

import com.sun.javafx.binding.StringFormatter;
import io.gumga.core.GumgaThreadScope;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

public class Criteria implements Serializable {

    public static final String SOURCE_CHARS = "'âàãáÁÂÀÃéêÉÊíÍóôõÓÔÕüúÜÚÇç'";
    public static final String TARGET_CHARS = "'AAAAAAAAEEEEIIOOOOOOUUUUCC'";

    public static boolean doTranslate = true;

    private Object field;
    private ComparisonOperator comparisonOperator;
    private Object value;
    private Object[] values;
    private String fieldFunction;
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


    @Override
    public String toString() {
        Object value = this.value;
//        ComparisonOperatorProcess comparisonOperatorProcess = new ComparisionOperatorProcessEqual();
//        return comparisonOperatorProcess.process(field, comparisonOperator, value, fieldFunction, valueFunction);

        if (ComparisonOperator.STARTS_WITH.equals(this.comparisonOperator)) {
            value = value + "%";
        } else if (ComparisonOperator.ENDS_WITH.equals(this.comparisonOperator)) {
            value = "%" + value;
        } else if (ComparisonOperator.CONTAINS.equals(this.comparisonOperator) || ComparisonOperator.NOT_CONTAINS.equals(this.comparisonOperator)) {
            value = "%" + value + "%";
        }

        if(value instanceof CriteriaField || value instanceof Boolean) {
            return field + comparisonOperator.hql + value;
        }

        if(ComparisonOperator.IN.equals(this.comparisonOperator)) {
            if(value instanceof Collection) {
                Collection values = (Collection) value;
                String v = "";
                for (Object object :values) {
                    if(object instanceof Number) {
                        v += object + ",";
                    } else {
                        v += "'"+ object.toString() + "',";
                    }
                }
                v = v.substring(0, v.length()-1);
                return field + comparisonOperator.hql + "(" + v + ")";
            }
            return  field + comparisonOperator.hql + "('" + value.toString() +"')";
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
                    } else if(objects[0] instanceof Date) {
                        String format1 = new SimpleDateFormat("yyyy-MM-dd").format(objects[0]);
                        String format2 = new SimpleDateFormat("yyyy-MM-dd").format(objects[1]);

                        return field + comparisonOperator.hql +  String.format("to_timestamp('%s 00:00:00','yyyy/MM/dd HH24:mi:ss')", format1) + " AND " + String.format("to_timestamp('%s 23:59:59','yyyy/MM/dd HH24:mi:ss')", format2);
                    }
                    return field + comparisonOperator.hql + "'" + objects[0] + "' AND '" + objects[1] + "'";
                }

                if(objects[0] instanceof Number) {
                    return field + comparisonOperator.hql +  objects[0] + " AND " + objects[0];
                } else if(objects[0] instanceof Date) {
                    String format1 = new SimpleDateFormat("yyyy-MM-dd").format(objects[0]);
                    return field + comparisonOperator.hql +  String.format("to_timestamp('%s 00:00:00','yyyy/MM/dd HH24:mi:ss')", format1) + " AND " + String.format("to_timestamp('%s 23:59:59','yyyy/MM/dd HH24:mi:ss')", format1);
                }

                return field + comparisonOperator.hql + "'" + objects[0] + "' AND '" + objects[0] + "'";
            }

            if(value instanceof Number) {
                return field + comparisonOperator.hql +  value + " AND " + value;
            } else if(value instanceof Date) {
                String format1 = new SimpleDateFormat("yyyy-MM-dd").format(value);
                return field + comparisonOperator.hql +  String.format("to_timestamp('%s 00:00:00','yyyy/MM/dd HH24:mi:ss')", format1) + " AND " + String.format("to_timestamp('%s 23:59:59','yyyy/MM/dd HH24:mi:ss')", format1);
            }

            return field + comparisonOperator.hql + "'" + value + "' AND '" + value + "'";
        }




        if(value instanceof Number) {
            return field + comparisonOperator.hql + value;

        } else if(value instanceof Date) {
            String format = new SimpleDateFormat("yyyy-MM-dd").format(value);

            switch (this.comparisonOperator) {
                case EQUAL:
                    return field + ComparisonOperator.GREATER_EQUAL.hql + String.format("to_timestamp('%s 00:00:00','yyyy/MM/dd HH24:mi:ss')", format) + " AND " +
                            field + ComparisonOperator.LOWER_EQUAL.hql + String.format("to_timestamp('%s 23:59:59','yyyy/MM/dd HH24:mi:ss')", format);
                case GREATER_EQUAL:
                    return field + ComparisonOperator.GREATER_EQUAL.hql + String.format("to_timestamp('%s 00:00:00','yyyy/MM/dd HH24:mi:ss')", format);
                case GREATER:
                    return field + ComparisonOperator.GREATER.hql + String.format("to_timestamp('%s 00:00:00','yyyy/MM/dd HH24:mi:ss')", format);
                case LOWER_EQUAL:
                    return field + ComparisonOperator.LOWER_EQUAL.hql + String.format("to_timestamp('%s 00:00:00','yyyy/MM/dd HH24:mi:ss')", format);
                case LOWER:
                    return field + ComparisonOperator.LOWER.hql + String.format("to_timestamp('%s 00:00:00','yyyy/MM/dd HH24:mi:ss')", format);

            }
        }

        return String.format(fieldFunction, field) + comparisonOperator.hql + String.format(valueFunction, "\'" + value + "\'");
    }




    public Criteria addIgnoreCase() {
        fieldFunction = String.format(fieldFunction, "lower(%s)");
        valueFunction = String.format(valueFunction, "lower(%s)");
        return this;
    }

    public Criteria addTranslate() {
        if (doTranslate) {
            fieldFunction = String.format(fieldFunction, "translate(%s," + SOURCE_CHARS + "," + TARGET_CHARS + ")");
            valueFunction = String.format(fieldFunction, "translate(%s," + SOURCE_CHARS + "," + TARGET_CHARS + ")");
        }
        return this;
    }


}










