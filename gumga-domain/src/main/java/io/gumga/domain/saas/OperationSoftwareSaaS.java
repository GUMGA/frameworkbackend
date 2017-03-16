package io.gumga.domain.saas;

public class OperationSoftwareSaaS {

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
