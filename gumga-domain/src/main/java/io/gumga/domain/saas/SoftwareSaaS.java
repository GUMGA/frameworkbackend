package io.gumga.domain.saas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe modelo para criação de software
 */
public class SoftwareSaaS {

    /**
     * Nome do software
     */
    private String name;
    /**
     * Lista de operações do software
     */
    private List<OperationSoftwareSaaS> operations;

    public SoftwareSaaS() {
        this.operations = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<OperationSoftwareSaaS> getOperations() {
        return Collections.unmodifiableList(operations);
    }

    public void setOperations(List<OperationSoftwareSaaS> operations) {
        this.operations = operations;
    }

    public void addOperationSoftwareSaaS(OperationSoftwareSaaS operation) {
        this.operations.add(operation);
    }
}
