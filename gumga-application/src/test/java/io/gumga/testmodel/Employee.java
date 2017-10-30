package io.gumga.testmodel;

import javax.persistence.Entity;

@Entity
public class Employee extends Person {

    public Employee() {
    }

    public Employee(String name) {
        super(name);
    }
}
