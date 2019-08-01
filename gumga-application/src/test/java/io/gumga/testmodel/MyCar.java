package io.gumga.testmodel;

import io.gumga.domain.GumgaModel;
import io.gumga.domain.GumgaMultitenancy;
import io.gumga.domain.customfields.GumgaCustomizableModel;
import java.lang.reflect.Field;


import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity

@GumgaMultitenancy
public class MyCar extends Car {
    
    private static final Logger log = LoggerFactory.getLogger(MyCar.class);

    private String licensePlate;

    public MyCar() {
        

    }

    public MyCar(String licensePlate, String color) {
        super(color);
        this.licensePlate = licensePlate;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    @Override
    public String toString() {
        Field gumgaCustomFields = null;
        try {
            gumgaCustomFields = GumgaCustomizableModel.class.getDeclaredField("gumgaCustomFields");
            gumgaCustomFields.setAccessible(true);
            return "MyCar{" + "licensePlate=" + licensePlate + '}' + gumgaCustomFields.get(this);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return "MyCar{" + "licensePlate=" + licensePlate + "} NO CUSTOM FIELDS";
    }

}
