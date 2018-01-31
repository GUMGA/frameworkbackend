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
    private Boolean createRoleShared;
    /**
     * Cria token eterno
     */
    private Boolean createEternalToken;

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
}
