package io.gumga.core.gquery;

import com.sun.javafx.binding.StringFormatter;
import io.gumga.core.GumgaThreadScope;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class Criteria implements Serializable {

    public static final String SOURCE_CHARS = "'âàãáÁÂÀÃéêÉÊíÍóôõÓÔÕüúÜÚÇç'";
    public static final String TARGET_CHARS = "'AAAAAAAAEEEEIIOOOOOOUUUUCC'";

    public static boolean doTranslate = true;

    private String field;
    private ComparisonOperator comparisonOperator;
    private Object value;
    private String[] values;
    private String fieldFunction;
    private String valueFunction;

    private void init() {
        comparisonOperator = ComparisonOperator.EQUAL;
        fieldFunction = "%s";
        valueFunction = "%s";
    }

    public Criteria() {
        field = "1";
        value = "1";
        init();
    }

    public Criteria(String field, ComparisonOperator comparisonOperator, Object value) {
        init();
        this.field = field;
        this.comparisonOperator = comparisonOperator;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
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

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
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
        if (ComparisonOperator.STARTS_WITH.equals(this.comparisonOperator)) {
            value = value + "%";
        } else if (ComparisonOperator.ENDS_WITH.equals(this.comparisonOperator)) {
            value = "%" + value;
        } else if (ComparisonOperator.CONTAINS.equals(this.comparisonOperator)) {
            value = "%" + value + "%";
        }



        if(value instanceof Number) {
            return field + comparisonOperator.hql + value;
        } else if(value instanceof Date) {
            String format = new SimpleDateFormat("yyyy-MM-dd").format(value);

            switch (this.comparisonOperator) {
                case EQUAL:
                    return field + " " + ComparisonOperator.GREATER_EQUAL.hql + " " + String.format("to_timestamp('%s 00:00:00','yyyy/MM/dd HH24:mi:ss')", format) + " AND " +
                            field + " " + ComparisonOperator.LOWER_EQUAL.hql + " " + String.format("to_timestamp('%s 23:59:59','yyyy/MM/dd HH24:mi:ss')", format);
                case GREATER_EQUAL:
                    return field + " " + ComparisonOperator.GREATER_EQUAL.hql + " " + String.format("to_timestamp('%s 00:00:00','yyyy/MM/dd HH24:mi:ss')", format);
                case GREATER:
                    return field + " " + ComparisonOperator.GREATER.hql + " " + String.format("to_timestamp('%s 00:00:00','yyyy/MM/dd HH24:mi:ss')", format);
                case LOWER_EQUAL:
                    return field + " " + ComparisonOperator.LOWER_EQUAL.hql + " " + String.format("to_timestamp('%s 00:00:00','yyyy/MM/dd HH24:mi:ss')", format);
                case LOWER:
                    return field + " " + ComparisonOperator.LOWER.hql + " " + String.format("to_timestamp('%s 00:00:00','yyyy/MM/dd HH24:mi:ss')", format);

            }
        }

        String s = String.format(fieldFunction, field) + comparisonOperator.hql + String.format(valueFunction, '\'' + value.toString() + '\'');
        return s;

    }


//    @Override
//    public String toString() {
//        return "Criteria{" +
//                "field='" + field + '\'' +
//                ", comparisonOperator=" + comparisonOperator +
//                ", value=" + value +
//                ", values=" + Arrays.toString(values) +
//                ", fieldFunction='" + fieldFunction + '\'' +
//                ", valueFunction='" + valueFunction + '\'' +
//                '}';
//    }

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
