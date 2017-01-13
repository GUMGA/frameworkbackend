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

    public UserAndPassword() {
        this.user = "empty";
        this.password = null;
        this.newPassword = null;
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

}
