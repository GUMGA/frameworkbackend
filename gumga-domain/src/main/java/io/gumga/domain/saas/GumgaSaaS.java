package io.gumga.domain.saas;

public class GumgaSaaS {

    private String organizationName;
    private String softwareName;
    private UserSaaS user;
    private InstanceSaaS instance;
    private Boolean createRoleShared;
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
