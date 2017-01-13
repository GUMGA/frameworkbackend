/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.domain;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Representa a transação para auditoria, armazena dados como horário, ip da
 * requisição e usuário.
 *
 * @author munif
 */
@Entity
@SequenceGenerator(name = "SEQREV", sequenceName = "SEQREV")
@RevisionEntity(GumgaRevisionListener.class)
public class GumgaRevisionEntity implements Serializable {
    public static final String SEQREV = "SEQREV";
    private static final long serialVersionUID = 1L;
    @RevisionNumber
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = SEQREV)
    private Long id;
    @RevisionTimestamp
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date moment;
    private String ip;
    private String userLogin;

    public GumgaRevisionEntity() {
        moment = new Date();
        userLogin = "unknown";
        ip = "unknown";

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getMoment() {
        return moment;
    }

    public void setMoment(Date moment) {
        this.moment = moment;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GumgaRevisionEntity other = (GumgaRevisionEntity) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "id:" + id + " " + userLogin + "@" + ip + " (" + moment + ")";
    }

}
