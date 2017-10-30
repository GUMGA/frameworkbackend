package io.gumga.testmodel;


import javax.persistence.Entity;

@Entity
public class Supplier extends Person {

    public Supplier() {
    }

    public Supplier(String name) {
        super(name);
    }
}
