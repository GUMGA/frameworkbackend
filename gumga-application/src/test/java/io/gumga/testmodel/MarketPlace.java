package io.gumga.testmodel;

import io.gumga.domain.GumgaModel;
import io.gumga.domain.GumgaMultitenancy;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@GumgaMultitenancy
@Entity

public class MarketPlace extends GumgaModel<Long> {

    @ElementCollection
    @Column(name = "mp_value", nullable = false)
    @MapKeyColumn(name = "mp_key")
    @CollectionTable(name = "MARKETPLACE_FIELD",joinColumns = @JoinColumn(name = "id"))
    private Map<String, String> fields = new HashMap<>();

    @Column(name = "nome")
    private String nome;
    @ManyToOne
    private Stock stock;
    public Map<String, String> getFields() {
        return fields;
    }

    public void setFields(Map<String, String> fields) {
        this.fields = fields;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }
}
