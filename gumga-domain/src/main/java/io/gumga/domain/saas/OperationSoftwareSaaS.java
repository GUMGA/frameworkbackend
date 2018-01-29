package io.gumga.domain.saas;

/**
 * Classe modelo para criação da operação de software
 */
public class OperationSoftwareSaaS {

    /**
     * Chave da operação
     */
    private String key;

    public OperationSoftwareSaaS() {}

    public OperationSoftwareSaaS(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
