package io.gumga.domain;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import java.util.Date;

/**
 * Representa um mensagem entre dois usuários.
 *
 * @author munif
 */
@Entity
@SequenceGenerator(name = GumgaModel.SEQ_NAME, sequenceName = "SEQ_GUMGA_MSG")
@Table(name = "gumga_msg")
@GumgaMultitenancy
public class GumgaMessage extends GumgaModel<Long> {

    private String senderLogin;
    private String destinationLogin;
    private String message;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date viewedIn;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date visibleOn;

    public GumgaMessage() {
        visibleOn = new Date();
        viewedIn = null;
        senderLogin = null;
        destinationLogin = null;
        message = null;
    }

    public GumgaMessage(Long id) {
        this.id = id;
    }

    public String getSenderLogin() {
        return senderLogin;
    }

    public void setSenderLogin(String senderLogin) {
        this.senderLogin = senderLogin;
    }

    public String getDestinationLogin() {
        return destinationLogin;
    }

    public void setDestinationLogin(String destinationLogin) {
        this.destinationLogin = destinationLogin;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getViewedIn() {
        return viewedIn;
    }

    public void setViewedIn(Date viewedIn) {
        this.viewedIn = viewedIn;
    }

    public Date getVisibleOn() {
        return visibleOn;
    }

    public void setVisibleOn(Date visibleOn) {
        this.visibleOn = visibleOn;
    }

    @Override
    public String toString() {
        return "{  \"senderLogin\":\"" + senderLogin + "\", "
                + "\"destinationLogin\":\"" + destinationLogin + "\", "
                + "\"message\":\"" + message + "\", "
                + "\"viewedIn\":\"" + viewedIn + "\", "
                + "\"visibleOn\":\"" + visibleOn + "\"}";
    }

}
