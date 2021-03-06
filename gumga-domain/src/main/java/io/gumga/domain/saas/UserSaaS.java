package io.gumga.domain.saas;

/**
 * Classe modelo para criação de um usuário
 */
public class UserSaaS {

    /**
     * Nome do usuário
     */
    private String name;
    /**
     * E-mail do usuário
     */
    private String email;
    /**
     * Senha do usuário
     */
    private String password;
    /**
     * Codigo interno do usuário
     */
    private String internalCode;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInternalCode() {
        return internalCode;
    }

    public void setInternalCode(String internalCode) {
        this.internalCode = internalCode;
    }
}
