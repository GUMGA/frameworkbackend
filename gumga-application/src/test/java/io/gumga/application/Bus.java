package io.gumga.application;

import io.gumga.domain.GumgaModel;
import io.gumga.domain.GumgaMultitenancy;
import io.gumga.domain.shared.GumgaSharedModel;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name = GumgaModel.SEQ_NAME, sequenceName = "SEQ_BUS")
@GumgaMultitenancy
public class Bus extends GumgaSharedModel<Long> {

    public Bus() {

    }

    public Bus(String line) {
        this.line = line;
    }

    private String line;

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

}
