/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.application;

import io.gumga.domain.GumgaModel;
import io.gumga.domain.GumgaMultitenancy;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import java.io.Serializable;

/**
 *
 * @author wlademir
 */
@Entity
@SequenceGenerator(name = GumgaModel.SEQ_NAME, sequenceName = "SEQ_CAR")
@GumgaMultitenancy
public class Task extends GumgaModel<Long> implements Serializable{
    
    String name;

    public Task() {
    }

    public Task(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
    
    
    
    
}
