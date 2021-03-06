package io.gumga.testmodel;

import io.gumga.domain.GumgaModel;
import io.gumga.domain.GumgaMultitenancy;
import io.gumga.domain.customfields.GumgaCustomizableModel;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;


@Entity

@GumgaMultitenancy
public class Car extends GumgaCustomizableModel<Long> {
	
	public Car() {
		
	}
	
	public Car(String color) {
		this.color = color;
	}
	
	private String color;

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	

}
