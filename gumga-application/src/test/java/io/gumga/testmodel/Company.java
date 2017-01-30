package io.gumga.testmodel;

import io.gumga.domain.GumgaModel;
import io.gumga.domain.GumgaMultitenancy;
import io.gumga.domain.domains.GumgaBoolean;
import java.util.Date;
import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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

    public Company() {
    }

    public Company(String name) {
        this.name = name;
    }

    public Company(String name, Date date, Boolean ativo) {
        this.name = name;
        this.date = date;
        this.ativo = ativo;
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
        return "Company{" + "name=" + name + ", date=" + date + ", ativo=" + ativo + '}';
    }

}
