package io.gumga.core.gquery;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Junção utilizada no {@link GQuery}
 */
public class Join implements Serializable {

    /**
     * Tipo de Junção
     */
    private JoinType type;
    /**
     * Tabela
     */
    private String table;
    /**
     * Critérios de Junção
     */
    private List<CriteriaJoin> subQuerys = new LinkedList<>();

    protected Join(){}

    public Join(String table, JoinType type) {
        this.table = table;
        this.type = type;
    }

    public Join on(Criteria criteria) {
        this.subQuerys.add(new CriteriaJoin(criteria, CriteriaJoinType.ON));
        return this;
    }

    public Join and(Criteria criteria) {
        this.subQuerys.add(new CriteriaJoin(criteria, CriteriaJoinType.AND));
        return this;
    }

    public Join or(Criteria criteria) {
        this.subQuerys.add(new CriteriaJoin(criteria, CriteriaJoinType.OR));
        return this;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(type.getName() + table);
        this.subQuerys.forEach(criteriaJoin -> {
            stringBuilder.append(criteriaJoin.toString());
        });
        return stringBuilder.toString();
    }

//    public static void main(String[] args) {
//        GQuery join = new GQuery(new Criteria("nome", ComparisonOperator.EQUAL, "felipe"))
//                .join(
//                        new Join("endereco", JoinType.INNER)
//                                .on(new Criteria("id", ComparisonOperator.EQUAL, 2))
//                ).join(new Join("endereco", JoinType.INNER)
//                        .on(new Criteria("id", ComparisonOperator.EQUAL, 3)));
//
//        System.out.println(join+join.getJoins());
//
//    }

}
