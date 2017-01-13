package io.gumga.core;

import java.util.Arrays;
import java.util.Collections;

/**
 * Classe para representar os parâmetros de uma pesquisa enviada ao Framework
 *
 * @author Equipe Gumga
 */
public class QueryObject {

    public static final String SIMPLE = "SIMPLE";
    public static final String EMPTY = "[]";

    /**
     * Objeto JSON que representa uma pequisa avançada
     */
    private String aqo = EMPTY;
    /**
     * Trecho HQL que representa uma pequisa avançada FROM Classe obj WHERE ....
     */
    private String aq = SIMPLE;
    /**
     * Critério da pesquisa simples
     */
    private String q = "";
    /**
     * Posição inicial esperada no retorno
     */
    private int start = 0;
    /**
     * Tamanho da página
     */
    private int pageSize = 10;
    /**
     * Atributo de ordenaçao
     */
    private String sortField = "";
    /**
     * Ordem de ordenação ascendente ou descendente
     */
    private String sortDir = "asc";
    /**
     * Atributos para pesquisa simples
     */
        private String[] searchFields;

    /**
     * Indica pesquisa fonética ou não
     */
    private boolean phonetic = false;

    /**
     * Apenas conta, sem trazer os resultados
     */
    private boolean countOnly = false;

    public boolean isCountOnly() {
        return countOnly;
    }

    public void setCountOnly(boolean countOnly) {
        this.countOnly = countOnly;
    }

    public boolean isPhonetic() {
        return phonetic;
    }

    public void setPhonetic(boolean phonetic) {
        this.phonetic = phonetic;
    }

    public String getAqo() {
        return aqo;
    }

    public void setAqo(String aqo) {
        this.aqo = aqo;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getSortDir() {
        return sortDir;
    }

    public void setSortDir(String sortDir) {
        if (Arrays.asList(new String[]{"asc", "desc"}).contains(sortDir)) {
            this.sortDir = sortDir;
        }
    }

    public String[] getSearchFields() {
        return searchFields;
    }

    public void setSearchFields(String searchFields) {
        this.searchFields = searchFields.split(",");
    }

    public void setSearchFields(String... fields) {
        this.searchFields = fields;
    }

    public int getCurrentPage() {
        if (start == 0) {
            return 1;
        }
        return start / pageSize + 1;
    }

    public boolean isValid() {
        return searchFields != null && searchFields.length > 0
                && q != null && !q.isEmpty();
    }

    public String getAq() {
        return aq;
    }

    public void setAq(String aq) {
        this.aq = aq;
    }

    public boolean isAdvanced() {
        return !SIMPLE.equals(aq);
    }

    @Override
    public String toString() {
        return "QueryObject{ phonetic=" + phonetic + ", aq=" + aq + ", q=" + q + ", start=" + start + ", pageSize=" + pageSize + ", sortField=" + sortField + ", sortDir=" + sortDir + ", searchFields=" + Arrays.asList(searchFields == null ? Collections.EMPTY_LIST : searchFields) + '}';
    }

}
