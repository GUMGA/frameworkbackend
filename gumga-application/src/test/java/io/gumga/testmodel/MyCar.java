package io.gumga.testmodel;

import io.gumga.domain.GumgaModel;
import io.gumga.domain.GumgaMultitenancy;
import io.gumga.domain.customfields.GumgaCustomizableModel;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name = GumgaModel.SEQ_NAME, sequenceName = "SEQ_CAR")
@GumgaMultitenancy
public class MyCar extends Car {

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
            Logger.getLogger(MyCar.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "MyCar{" + "licensePlate=" + licensePlate + "} NO CUSTOM FIELDS";
    }

}
