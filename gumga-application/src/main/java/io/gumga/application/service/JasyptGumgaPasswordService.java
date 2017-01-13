/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.application.service;

import io.gumga.core.service.GumgaPasswordService;
import org.jasypt.digest.StandardStringDigester;
import org.springframework.stereotype.Service;

/**
 * Password service implementations which uses Jasypt framework
 * (http://www.jasypt.org)
 *
 * @author gyowannyqueiroz
 * @since jul/2015
 */
@Service(value = "gumgaPasswordService")
public class JasyptGumgaPasswordService implements GumgaPasswordService {

    /*
     * This is the digester used to encript and verify passwords
     */
    private static StandardStringDigester encryptor;

    {
        encryptor = new StandardStringDigester();
        encryptor.setAlgorithm("SHA-1");
        encryptor.setIterations(10000);

    }

    @Override
    public String encryptPassword(String rawPassword) {
        return encryptor.digest(rawPassword);
    }

    @Override
    public Boolean isPasswordCorrect(String informedPassword, String encryptedPassword) {
        return encryptor.matches(informedPassword, encryptedPassword);
    }

}
