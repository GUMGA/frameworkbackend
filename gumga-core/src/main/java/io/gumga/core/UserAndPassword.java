/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.core;

/**
 * Claasse utilizada para troca de senhas em várias camadas do Framework
 *
 * @author munif
 */
public class UserAndPassword {

    private String user;
    private String password;
    private String newPassword;
    private String softwareName;
    private String realm;

    public UserAndPassword() {
        this.user = "empty";
        this.password = null;
        this.newPassword = null;
        this.softwareName = null;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public String toString() {
        return "UserAndPassword{" + "user=" + user + ", password=" + password + '}';
    }

    public String getSoftwareName() {
        return softwareName;
    }

    public void setSoftwareName(String softwareName) {
        this.softwareName = softwareName;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }
}
