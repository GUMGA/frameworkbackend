package io.gumga.gumgamodelv2;

import io.gumga.domain.GumgaModelV2;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
public class Bolsa extends GumgaModelV2 {

    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
