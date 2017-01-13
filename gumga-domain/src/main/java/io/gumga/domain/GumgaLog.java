package io.gumga.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Entidade utilizada para armazenar log no banco de dados.
 *
 * @author munif
 */
@Entity
@SequenceGenerator(name = GumgaModel.SEQ_NAME, sequenceName = "SEQ_GUMGA_GLOG")
@Table(name = "gumga_glog")
public class GumgaLog extends GumgaModel<Long> {

    @Column(name = "gumga_login")
    private String login;
    @Column(name = "gumga_ip")
    private String ip;
    @Column(name = "gumga_organization_code")
    private String organizationCode;
    @Column(name = "gumga_organization")
    private String organization;
    @Column(name = "gumga_software")
    private String software;
    @Column(name = "gumga_operation")
    private String operarationKey;
    @Column(name = "gumga_endpoint")
    private String endPoint;
    @Column(name = "gumga_method")
    private String method;
    @Column(name = "was_allowed")
    private boolean allowed;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "gumga_moment")
    private Date quando;

    public GumgaLog() {
        this.quando = new Date();
        allowed = false;
    }

    public GumgaLog(String login, String ip, String organizationCode, String organization, String software, String operarationKey, String endPoint, String method, boolean a) {
        this.login = login;
        this.ip = ip;
        this.organizationCode = organizationCode;
        this.organization = organization;
        this.software = software;
        this.operarationKey = operarationKey;
        this.endPoint = endPoint;
        this.method = method;
        this.quando = new Date();
        this.allowed = a;
    }    

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getSoftware() {
        return software;
    }

    public void setSoftware(String software) {
        this.software = software;
    }

    public String getOperarationKey() {
        return operarationKey;
    }

    public void setOperarationKey(String operarationKey) {
        this.operarationKey = operarationKey;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Date getQuando() {
        return quando;
    }

    public void setQuando(Date quando) {
        this.quando = quando;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }

    @Override
    public String toString() {
        return "GumgaLog{" + "id=" + id + ", login=" + login + ", ip=" + ip + ", organizationCode=" + organizationCode + ", organization=" + organization + ", software=" + software + ", operarationKey=" + operarationKey + ", endPoint=" + endPoint + ", method=" + method + '}';
    }

}
