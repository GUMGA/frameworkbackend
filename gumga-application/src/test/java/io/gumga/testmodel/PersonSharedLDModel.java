package io.gumga.testmodel;

import io.gumga.domain.GumgaModel;
import io.gumga.domain.GumgaMultitenancy;
import io.gumga.domain.logicaldelete.GumgaSharedLDModel;
import io.gumga.domain.shared.GumgaSharedModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;

@Entity
@GumgaMultitenancy

public class PersonSharedLDModel extends GumgaSharedModel<Long> {

    @Column
    private String nome;

    public PersonSharedLDModel(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
