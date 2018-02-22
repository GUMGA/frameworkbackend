package io.gumga.testmodel;

import io.gumga.domain.GumgaModel;
import io.gumga.domain.GumgaMultitenancy;
import io.gumga.domain.GumgaSharedModelUUID;
import io.gumga.domain.shared.GumgaSharedModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;

@Entity
@GumgaMultitenancy
public class PersonSharedUUIDModel extends GumgaSharedModelUUID {

    @Column
    private String nome;

    public PersonSharedUUIDModel(String nome) {
        this.nome = nome;
    }

    public PersonSharedUUIDModel() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
