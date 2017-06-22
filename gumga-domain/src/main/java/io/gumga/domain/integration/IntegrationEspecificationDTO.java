package io.gumga.domain.integration;

public class IntegrationEspecificationDTO {

    public String login;
    public long endTime;
    public String name;
    public String software;
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
