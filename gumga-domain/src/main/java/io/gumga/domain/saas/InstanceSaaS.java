package io.gumga.domain.saas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe modelo para criação de uma instância
 */
public class InstanceSaaS {

    /**
     * Nome da instância
     */
    private String name;
    /**
     * Lista de softwares
     */
    private List<SoftwareSaaS> softwares;
    /**
     * Número de dias para expiração da instância
     */
    private Integer numberOfDayForExpiration;

    public InstanceSaaS() {
        this.softwares = new ArrayList<>();
    }

    public List<SoftwareSaaS> getSoftwares() {
        return Collections.unmodifiableList(softwares);
    }

    public void addSoftware(SoftwareSaaS software) {
        this.softwares.add(software);
    }

    public void setSoftwares(List<SoftwareSaaS> softwares) {
        this.softwares = softwares;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumberOfDayForExpiration() {
        return numberOfDayForExpiration;
    }

    public void setNumberOfDayForExpiration(Integer numberOfDayForExpiration) {
        this.numberOfDayForExpiration = numberOfDayForExpiration;
    }
}
