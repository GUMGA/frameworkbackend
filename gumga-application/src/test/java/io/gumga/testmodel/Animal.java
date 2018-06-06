package io.gumga.testmodel;

import io.gumga.domain.GumgaModelUUIDComposite;
import io.gumga.domain.GumgaMultitenancy;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@GumgaMultitenancy
public class Animal extends GumgaModelUUIDComposite {

    @Column(name = "nome")
    private String name;

    protected Animal() {}

    public Animal(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
