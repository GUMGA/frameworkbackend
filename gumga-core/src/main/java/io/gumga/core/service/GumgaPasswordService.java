/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.core.service;

/**
 * Interface that describes the password handling service.
 *
 * @author gyowannyqueiroz
 * @since jul/2015
 */
public interface GumgaPasswordService {

    /**
     * Returns an encrypted version of the given password
     *
     * @param rawPassword The password to be encrypted
     * @return Encrypted string
     */
    public String encryptPassword(String rawPassword);

    /**
     * Verifies whether the informed password is valid or not.
     *
     * @param informedPassword The password to be checked
     * @param encryptedPassword The valid password
     * @return True if the password is correct
     */
    public Boolean isPasswordCorrect(String informedPassword, String encryptedPassword);

}
