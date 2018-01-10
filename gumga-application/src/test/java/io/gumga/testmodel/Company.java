package io.gumga.testmodel;

import io.gumga.domain.GumgaModel;
import io.gumga.domain.GumgaMultitenancy;
import io.gumga.domain.GumgaMultitenancyPolicy;
import io.gumga.domain.domains.GumgaBoolean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

import io.gumga.domain.domains.GumgaOi;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;

@GumgaMultitenancy
@Entity
@Audited
@SequenceGenerator(name = GumgaModel.SEQ_NAME, sequenceName = "SEQ_COMPANY")
public class Company extends GumgaModel<Long> {

    private String name;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "g_data")
    private Date date;
    private Boolean ativo;
    private Long quantidade;
    private BigDecimal valor;
    @Enumerated(EnumType.STRING)
    private Tipo tipo;
    private GumgaBoolean gumgaBoolean = new GumgaBoolean(true);

    public Company() {
    }

    public Company(String name) {
        this.name = name;
    }

    public Company(String name, Date date, Boolean ativo) {
        this.name = name;
        this.date = date;
        this.ativo = ativo;
        this.tipo = Tipo.SIMPLES;
        this.quantidade = 0L;
        this.valor = BigDecimal.ZERO;
        this.tipo = Tipo.SIMPLES;
    }

    public Company(Long quantidade, String name, Date date, Boolean ativo) {
        this.quantidade = quantidade;
        this.name = name;
        this.date = date;
        this.ativo = ativo;
        this.valor = BigDecimal.ZERO;
        this.tipo = Tipo.SIMPLES;
    }

    public Company(Long quantidade, BigDecimal valor,Tipo tipo, String name, Date date, Boolean ativo) {
        this.valor = valor;
        this.quantidade = quantidade;
        this.name = name;
        this.date = date;
        this.ativo = ativo;
        this.tipo = tipo;
    }

    public GumgaBoolean getGumgaBoolean() {
        return gumgaBoolean;
    }

    public void setGumgaBoolean(GumgaBoolean gumgaBoolean) {
        this.gumgaBoolean = gumgaBoolean;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return "Company{ id=" + id + " ,oi=" + oi + ",name=" + name + ", date=" + date + ", ativo=" + ativo + '}';
    }

    public Long getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Long quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public void setOi(GumgaOi oi) {
        this.oi = oi;
    }

}
