package io.gumga.core.gquery;

public class CriteriaJoin {
    private Criteria criteria;
    private CriteriaJoinType type;

    protected CriteriaJoin(){}

    public CriteriaJoin(Criteria criteria, CriteriaJoinType type) {
        this.criteria = criteria;
        this.type = type;
    }

    @Override
    public String toString() {
        return type.getName() + criteria.toString();
    }
}
enum CriteriaJoinType {
    ON(" on "), AND(" and "), OR(" or ");

    private String name;

    CriteriaJoinType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
