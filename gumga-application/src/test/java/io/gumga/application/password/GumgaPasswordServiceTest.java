/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.application.password;

import io.gumga.application.AbstractTest;
import io.gumga.core.service.GumgaPasswordService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertNotNull;
import static org.springframework.test.util.AssertionErrors.assertTrue;

/**
 *
 * @author gyowannyqueiroz
 */
public class GumgaPasswordServiceTest extends AbstractTest {

    @Autowired
    private GumgaPasswordService passwordService;

    public GumgaPasswordServiceTest() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testEncryptPassword() throws Exception {
        assertNotNull("Password service has not been set", passwordService);
        String password = "123";
        String encryptedPassword = "PaJOMOYNku8Mwv/JFX7YH9pb5zeMWSQmQGqRAA==";//passwordService.encryptPassword(password);
        System.out.println(String.format("### Password: %s - Encrypted password: %s", password, encryptedPassword));
        assertFalse(encryptedPassword == null || encryptedPassword.isEmpty(), "The encrypted password is empty");
        assertTrue("Password doesn't match", passwordService.isPasswordCorrect(password, encryptedPassword));
    }

    @Test
    public void testEncryptPasswordWithSpecialCharacters() throws Exception {
        assertNotNull("Password service has not been set", passwordService);
        String password = "!@#$%Gumga@1234!";
        String encryptedPassword = "Ujh6iHDMM+Fbln09zbYPIY6cNfRAjqyTSz1PNg==";//passwordService.encryptPassword(password);
        System.out.println(String.format("### Password: %s - Encrypted password: %s", password, encryptedPassword));
        assertFalse(encryptedPassword == null || encryptedPassword.isEmpty(), "The encrypted password is empty");
        assertTrue("Password doesn't match", passwordService.isPasswordCorrect(password, encryptedPassword));
    }

}
