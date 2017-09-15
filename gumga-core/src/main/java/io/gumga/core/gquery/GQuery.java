package io.gumga.core.gquery;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author munif
 */
public class GQuery implements Serializable {

    private LogicalOperator logicalOperator;
    private Criteria criteria;
    private List<GQuery> subQuerys;

    public GQuery() {
        this.logicalOperator = LogicalOperator.SIMPLE;
        this.criteria = new Criteria();
    }

    public GQuery(LogicalOperator logicalOperator, Criteria criteria, List<GQuery> subQuerys) {
        this.logicalOperator = logicalOperator;
        this.criteria = criteria;
        this.subQuerys = subQuerys;
    }

    public GQuery(LogicalOperator logicalOperator, Criteria criteria) {
        this.logicalOperator = logicalOperator;
        this.criteria = criteria;
    }

    public GQuery(Criteria criteria) {
        this.logicalOperator = logicalOperator.SIMPLE;
        this.criteria = criteria;
    }

    public GQuery(LogicalOperator logicalOperator, List<GQuery> subQuerys) {
        this.logicalOperator = logicalOperator;
        this.subQuerys = subQuerys;
    }

    public LogicalOperator getLogicalOperator() {
        return logicalOperator;
    }

    public void setLogicalOperator(LogicalOperator logicalOperator) {
        this.logicalOperator = logicalOperator;
    }

    public Criteria getCriteria() {
        return criteria;
    }

    public void setCriteria(Criteria criteria) {
        this.criteria = criteria;
    }

    public List<GQuery> getSubQuerys() {
        return subQuerys;
    }

    public void setSubQuerys(List<GQuery> subQuerys) {
        this.subQuerys = subQuerys;
    }

    public GQuery and(GQuery other) {
        if (LogicalOperator.AND.equals(this.logicalOperator)) {
            this.subQuerys.add(other);
            return this;
        }
        return new GQuery(LogicalOperator.AND, Arrays.asList(new GQuery[]{this, other}));
    }

    public GQuery or(GQuery other) {
        if (LogicalOperator.OR.equals(this.logicalOperator)) {
            this.subQuerys = new LinkedList<>(subQuerys);
            this.subQuerys.add(other);
            return this;
        }
        return new GQuery(LogicalOperator.OR, Arrays.asList(new GQuery[]{this, other}));
    }

    public GQuery and(Criteria criteria) {
        GQuery other = new GQuery(criteria);
        if (LogicalOperator.AND.equals(this.logicalOperator)) {
            this.subQuerys = new LinkedList<>(subQuerys);
            this.subQuerys.add(other);
            return this;
        }
        return new GQuery(LogicalOperator.AND, Arrays.asList(new GQuery[]{this, other}));
    }

    public GQuery or(Criteria criteria) {
        GQuery other = new GQuery(criteria);
        if (LogicalOperator.OR.equals(this.logicalOperator)) {
            this.subQuerys = new LinkedList<>(subQuerys);
            this.subQuerys.add(other);
            return this;
        }
        return new GQuery(LogicalOperator.OR, Arrays.asList(new GQuery[]{this, other}));
    }

    @Override
    public String toString() {
        if (null != logicalOperator) {
            switch (logicalOperator) {
                case SIMPLE:
                    return "(" + criteria.toString() + ")";
                case NOT:
                    return "(!" + criteria.toString() + ")";
                case AND:
                case OR:
                    if (subQuerys == null || subQuerys.isEmpty()) {
                        return "1 = 1";
                    }
                    String r = "(" + subQuerys.get(0);
                    for (int i = 1; i < subQuerys.size(); i++) {
                        r += " " + logicalOperator.toString() + " " + subQuerys.get(i).toString();
                    }
                    r += ")";
                    return r;
                default:
                    break;
            }
        }
        return "(" + criteria.toString() + ")";
    }

    public void addIgnoreCase() {
        if (criteria != null) {
            criteria.addIgnoreCase();
        }
        if (subQuerys != null) {
            subQuerys.stream().forEach((gq) -> gq.addIgnoreCase());
        }
    }
//
//    public static void main(String[] args) {
//        GQuery gQuery = new GQuery(new Criteria("name", ComparisonOperator.CONTAINS, "Mat"))
//                .or(new Criteria("idade", ComparisonOperator.GREATER, 3))
//                .and(new Criteria("valor", ComparisonOperator.GREATER, 5))
//                .or(new Criteria("cpf", ComparisonOperator.NOT_EQUAL, "129312312"));
//
//        System.out.println(gQuery.toString());
//
//
//
//    }
}
