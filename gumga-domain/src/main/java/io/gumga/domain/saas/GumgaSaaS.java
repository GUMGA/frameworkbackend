package io.gumga.domain.saas;

/**
 * Classe modelo para auxiliar a criação de uma aplicação SaaS.
 */
public class GumgaSaaS {

    /**
     * Nome da organização
     */
    private String organizationName;
    /**
     * Codigo interno da organização
     */
    private String internalCode;
    /**
     * IDCliente da organização
     */
    private Long idCliente;

    /**
     * Nome do software
     */
    private String softwareName;
    /**
     * Usuário a ser criado
     */
    private UserSaaS user;
    /**
     * Instância a ser criada (Venda do software)
     */
    private InstanceSaaS instance;
    /**
     * Cria Perfil compartilhado
     */
    private Boolean createRoleShared = Boolean.FALSE;
    /**
     * Cria token eterno
     */
    private Boolean createEternalToken = Boolean.FALSE;

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public UserSaaS getUser() {
        return user;
    }

    public void setUser(UserSaaS user) {
        this.user = user;
    }

    public InstanceSaaS getInstance() {
        return instance;
    }

    public void setInstance(InstanceSaaS instance) {
        this.instance = instance;
    }

    public Boolean getCreateRoleShared() {
        return createRoleShared;
    }

    public void setCreateRoleShared(Boolean createRoleShared) {
        this.createRoleShared = createRoleShared;
    }

    public Boolean getCreateEternalToken() {
        return createEternalToken;
    }

    public void setCreateEternalToken(Boolean createEternalToken) {
        this.createEternalToken = createEternalToken;
    }

    public String getSoftwareName() {
        return softwareName;
    }

    public void setSoftwareName(String softwareName) {
        this.softwareName = softwareName;
    }

    public String getInternalCode() {
        return internalCode;
    }

    public void setInternalCode(String internalCode) {
        this.internalCode = internalCode;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }
}
