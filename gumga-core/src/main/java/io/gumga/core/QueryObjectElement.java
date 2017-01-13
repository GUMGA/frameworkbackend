package io.gumga.core;

/**
 * Representa um elemento do AQO
 *
 * @author munif
 */
public class QueryObjectElement {

    private String attribute;
    private String hql;
    private String value;

    public QueryObjectElement() {
        attribute = "NO_ATTRIBUTE";
        hql = "NO_HQL";
        value = "NO_VALUE";
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

    @Override
    public String toString() {
        return "QueryObjectElement{" + "attribute=" + attribute + ", hql=" + hql + ", value=" + value + '}';
    }

}
