package io.gumga.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Representa dados do do usuário. Útil para armazenar configurações específicas
 * do usuário, como consultas, parâmetros, etc..
 *
 * @author munif
 */
@Entity
@SequenceGenerator(name = GumgaModel.SEQ_NAME, sequenceName = "SEQ_GUSER_GDATA")
@Table(name = "guser_gdata")
@GumgaMultitenancy
public class GumgaUserData extends GumgaModel<Long> {

    private String userLogin;
    @Column(name = "data_key")
    private String key;
    private String description;
    @Column(name = "data_value", length = 4096)
    private String value;

    public GumgaUserData() {
    }

    public GumgaUserData(String description, String key, String value) {
        this.key = key;
        this.value = value;
        this.description = description;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "GumgaUserData{" + "userLogin=" + userLogin + ", key=" + key + ", value=" + value + '}';
    }

}
