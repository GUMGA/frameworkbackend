package io.gumga.testmodel;

import io.gumga.domain.GumgaModel;
import io.gumga.domain.GumgaMultitenancy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import java.util.List;

@GumgaMultitenancy
@Entity
@SequenceGenerator(name = "SEQ", sequenceName = "SEQ_Stock")
public class Stock extends GumgaModel<Long> {

    @Column(name = "nome")
    private String nome;
    @OneToMany
    private List<MarketPlace> marketPlaces;

    public List<MarketPlace> getMarketPlaces() {

        return marketPlaces;
    }

    public void setMarketPlaces(List<MarketPlace> marketPlaces) {
        this.marketPlaces = marketPlaces;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
