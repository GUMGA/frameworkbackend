package io.gumga.testmodelv2;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.gumga.domain.GumgaModelUUID;

import javax.persistence.*;

@Entity
@Table
@Inheritance(strategy = InheritanceType.JOINED)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = Employee.class, name = "Empregado"),
                @JsonSubTypes.Type(value = Supplier.class, name = "Fornecedor")
        }
)
public class Person extends GumgaModelUUID {

    @Column
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonGetter
    public String getType() {
        return this.getClass().getSimpleName();
    }
}
