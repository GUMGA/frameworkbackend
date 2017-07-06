package io.gumga.core;

/**
 * Representa um elemento do AQO
 *
 * @author munif
 */
public class QueryObjectElement {

    public static final String NO_ATTRIBUTE = "NO_ATTRIBUTE";
    public static final String NO_HQL = "NO_HQL";
    public static final String NO_VALUE = "NO_VALUE";
    public static final String NO_TYPE = "NO_TYPE";

    private String attribute;
    private String attributeType;
    private String hql;
    private String value;

    public QueryObjectElement() {
        attribute = NO_ATTRIBUTE;
        attributeType = NO_TYPE;
        hql = NO_HQL;
        value = NO_VALUE;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getHql() {
        return hql;
    }

    public void setHql(String hql) {
        this.hql = hql;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isLogical() {
        return this.attribute.equals(NO_ATTRIBUTE) && this.hql.equals("NO_HQL");
    }

    public boolean isLogical2() {
        return this.attribute.equals(NO_ATTRIBUTE) && this.hql.equals("NO_HQL");
    }

    public String getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(String attributeType) {
        this.attributeType = attributeType;
    }

    public String toString() {
        if (isLogical()) {
            return " "+value+" ";
        }
        return "QueryObjectElement{" + "attribute=" + attribute + ":" + attributeType + ", hql=" + hql + ", value=" + value + '}';
    }

    
    

}
