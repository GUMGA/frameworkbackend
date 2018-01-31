package io.gumga.domain.integration;

/**
 * Classe DTO para integração da aplicação com o segurança
 */
public class IntegrationEspecificationDTO {

    /**
     * E-mail do usuário
     */
    public String login;
    /**
     * Tempo limite de acesso do usuário
     */
    public long endTime;
    /**
     * Nome da especificação
     */
    public String name;
    /**
     * Nome do software
     */
    public String software;
    /**
     * Operações
     */
    public String operations;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSoftware() {
        return software;
    }

    public void setSoftware(String software) {
        this.software = software;
    }

    public String getOperations() {
        return operations;
    }

    public void setOperations(String operations) {
        this.operations = operations;
    }

    @Override
    public String toString() {
        return "IntegrationEspecificationDTO{" + "login=" + login + ", endTime=" + endTime + ", name=" + name + ", software=" + software + ", operations=" + operations + '}';
    }

}
