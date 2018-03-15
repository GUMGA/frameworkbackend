package io.gumga.core.gquery;

import java.io.Serializable;
import java.util.*;

/**
 * Classe para criação de objetos de pesquisa orientada a objeto (consulta no banco de dados)
 * @author munif
 */
public class GQuery implements Serializable {

    /**
     * Operador Lógico de consulta
     */
    private LogicalOperator logicalOperator;
    /**
     * Critério de busca
     */
    private Criteria criteria;
    /**
     * Sub-consultas
     */
    private List<GQuery> subQuerys;
    /**
     * Junções de pesquisa, exemplo: inner join, left join...
     */
    private List<Join> joins = new LinkedList<>();
    /**
     * Indica se adicionara DISTINCT na consulta para evitar repetição de registros
     */
    private Boolean useDistinct = Boolean.FALSE;

    public final Map<String, Object> fieldValue = new HashMap<>();

    private Set<String> selects = new LinkedHashSet<>();


    /**
     * Construtor da classe que iniciará uma modelo de consulta simples
     */
    public GQuery() {
        this.logicalOperator = LogicalOperator.SIMPLE;
        this.criteria = new Criteria();
    }
    /**
     * Construtor da classe que iniciará um modelo com determinado operador lógico, critério e uma lista de sub-consultas
     * @param logicalOperator Operador lógico
     * @param criteria Critério
     * @param subQuerys Sub-consultas
     */
    public GQuery(LogicalOperator logicalOperator, Criteria criteria, List<GQuery> subQuerys) {
        this.logicalOperator = logicalOperator;
        this.criteria = criteria;
        this.subQuerys = subQuerys;
    }
    /**
     * Construtor da classe que iniciará um modelo com determinado operador lógico e critério de busca
     * @param logicalOperator Operador lógico
     * @param criteria Critério de busca
     */
    public GQuery(LogicalOperator logicalOperator, Criteria criteria) {
        this.logicalOperator = logicalOperator;
        this.criteria = criteria;
    }
    /**
     * Construtor da classe que iniciará um modelo com critério de busca
     * @param criteria Critério de busca
     */
    public GQuery(Criteria criteria) {
        this.logicalOperator = logicalOperator.SIMPLE;
        this.criteria = criteria;
    }
    /**
     * Construtor da classe que iniciará um modelo com determinado operador lógico e sub-consultas
     * @param logicalOperator Operador lógico
     * @param subQuerys Sub-consultas
     */
    public GQuery(LogicalOperator logicalOperator, List<GQuery> subQuerys) {
        this.logicalOperator = logicalOperator;
        this.subQuerys = subQuerys;
        y(this);
        startSelects(this);
    }

    /**
     * @return Operador lógico
     */
    public LogicalOperator getLogicalOperator() {
        return logicalOperator;
    }
    /**
     * @param logicalOperator Operador lógico
     */
    public void setLogicalOperator(LogicalOperator logicalOperator) {
        this.logicalOperator = logicalOperator;
    }
    /**
     * @return Critério de busca
     */
    public Criteria getCriteria() {
        return criteria;
    }
    /**
     * @param criteria Critério de busca
     */
    public void setCriteria(Criteria criteria) {
        this.criteria = criteria;
    }
    /**
     * @return Sub-consultas
     */
    public List<GQuery> getSubQuerys() {
        return subQuerys;
    }
    /**
     * @param subQuerys Sub-consultas
     */
    public void setSubQuerys(List<GQuery> subQuerys) {
        this.subQuerys = subQuerys;
    }

    /**
     * Adiciona nova consulta à atual com operador lógico AND
     * @param other GQuery
     * @return GQuery
     */
    public GQuery and(GQuery other) {
        if (LogicalOperator.AND.equals(this.logicalOperator)) {
            this.subQuerys.add(other);
            return this;
        }
        return new GQuery(LogicalOperator.AND, Arrays.asList(new GQuery[]{this, other}));
    }

    /**
     * Adiciona nova consulta à atual com operador lógico OR
     * @param other GQuery
     * @return GQuery
     */
    public GQuery or(GQuery other) {
        if (LogicalOperator.OR.equals(this.logicalOperator)) {
            this.subQuerys = new LinkedList<>(subQuerys);
            this.subQuerys.add(other);
            return this;
        }
        return new GQuery(LogicalOperator.OR, Arrays.asList(new GQuery[]{this, other}));
    }

    /**
     * Adiciona novo critério à consulta atual com operador lógico AND
     * @param criteria Criteria
     * @return GQuery
     */
    public GQuery and(Criteria criteria) {
        GQuery other = new GQuery(criteria);
        if (LogicalOperator.AND.equals(this.logicalOperator)) {
            this.subQuerys = new LinkedList<>(subQuerys);
            this.subQuerys.add(other);
            return this;
        }
        return new GQuery(LogicalOperator.AND, Arrays.asList(new GQuery[]{this, other}));
    }

    /**
     * Adiciona novo critério à consulta atual com operador lógico OR
     * @param criteria Criteria
     * @return GQuery
     */
    public GQuery or(Criteria criteria) {
        GQuery other = new GQuery(criteria);
        if (LogicalOperator.OR.equals(this.logicalOperator)) {
            this.subQuerys = new LinkedList<>(subQuerys);
            this.subQuerys.add(other);
            return this;
        }
        return new GQuery(LogicalOperator.OR, Arrays.asList(new GQuery[]{this, other}));
    }

    /**
     * Adiciona Junção à consulta atual
     * @param join Junção
     * @return GQuery
     */
    public GQuery join(Join join) {
        this.joins.add(join);
        return this;
    }

    public GQuery select(String field) {
        selects.add(field);
        return this;
    }

    public Map<String, Object> getParams() {
        Map<String, Object> result = new HashMap<>();
        return getParams(this, result);
    }

    private Map<String, Object> getParams(GQuery gQuery, Map<String, Object> map) {
        map.putAll(fieldValue);
        if(gQuery.getCriteria() != null) {
            map.putAll(gQuery.getCriteria().fieldValue);
        }

        if(gQuery.getSubQuerys() != null) {
            gQuery.getSubQuerys().stream().filter(g -> g.getCriteria() != null).forEach(s -> getParams(s, map));
        }
        return map;
    }

    public String getSelects() {
        StringBuilder builder = new StringBuilder();
        searchSelect(this, builder);
        return builder.toString().length() > 0 ? builder.substring(0, builder.toString().lastIndexOf(",")) : "";
    }

    private void searchSelect(GQuery gQuery, StringBuilder builder) {
        gQuery.selects.forEach(field -> builder.append(field.contains(",") ? field : field.concat(",")));
        if(gQuery.getSubQuerys() != null) {
            gQuery.getSubQuerys().forEach(s -> searchSelect(s, builder));
        }
    }

    /**
     * @return Parte da hql onde se encontram as junções
     */
    public String getJoins() {
        StringBuilder builder = new StringBuilder();
        x(this, builder);
        return builder.toString();
    }

    /**
     * Na busca de joins, este método monta as junções de consultas e sub-consultas em uma String
     * @param gQuery Consulta
     * @param builder String
     */
    private void x(GQuery gQuery, StringBuilder builder) {
        gQuery.joins.forEach(builder::append);
        if(gQuery.getSubQuerys() != null) {
            gQuery.getSubQuerys().forEach(s -> x(s, builder));
        }
    }

    /**
     * Na busca de joins, este método monta as junções de consultas e sub-consultas
     * @param gQuery Consulta
     */
    private void y(GQuery gQuery) {
        this.joins.addAll(gQuery.joins);
        gQuery.joins = new LinkedList<>();
        if(gQuery.getSubQuerys() != null) {
            gQuery.getSubQuerys().forEach(s -> y(s));
        }
    }

    private void startSelects(GQuery gQuery) {
        this.selects.addAll(gQuery.selects);
        gQuery.selects = new LinkedHashSet<>();
        if(gQuery.getSubQuerys() != null) {
            gQuery.getSubQuerys().forEach(s -> startSelects(s));
        }
    }

    /**
     * @return Utilização do distinct
     */
    public Boolean useDistinct() {
        Map<String, Boolean> result = new HashMap();
        result.put("useDistinct", Boolean.FALSE);
        this.searchUseDistinct(this, result);
        return result.get("useDistinct");
    }

    /**
     * Mapeia todas as consultas e sub-consultas para verificar utilização do distinct
     * @param gQuery Consulta
     * @param map Mapa de utilização do distinct
     */
    private void searchUseDistinct(GQuery gQuery, Map<String, Boolean> map) {
        if(!map.get("useDistinct")) {
            map.put("useDistinct", gQuery.getUseDistinct());
        }

        if(gQuery.getSubQuerys() != null) {
            for (GQuery query : gQuery.getSubQuerys()) {
                if(!map.get("useDistinct")) {
                    map.put("useDistinct", query.getUseDistinct());
                }
                searchUseDistinct(query, map);
            }
        }
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

    public Boolean getUseDistinct() {
        return useDistinct;
    }

    public void setUseDistinct(Boolean useDistinct) {
        this.useDistinct = useDistinct;
    }

    public static void main(String[] args) {
//        GQuery disticnt = new GQuery(new Criteria("cpf", ComparisonOperator.NOT_EQUAL, "129312312"));
////        disticnt.setUseDistinct(true);
//        GQuery gQuery = new GQuery(new Criteria("name", ComparisonOperator.CONTAINS, "Mat"))
//                .or(new Criteria("idade", ComparisonOperator.GREATER, 3))
//                .and(new Criteria("valor", ComparisonOperator.GREATER, 5))
//                .or(disticnt);
//        gQuery.setUseDistinct(true);

        GQuery gQuery = new GQuery(new Criteria("obj.name", ComparisonOperator.EQUAL, "Felipe"));

        GQuery and = gQuery.and(new Criteria("obj.idade", ComparisonOperator.GREATER, 18)).select("obj.name").select("obj.name");

//        GQuery select = gQuery.select("obj.idade");
//        System.out.println(select.getSelects());
//        GQuery and = select.and(new Criteria("obj.idade", ComparisonOperator.GREATER, 18));
//        GQuery select1 = and.select("obj.name");


        System.out.println(and.getSelects());



    }
}
