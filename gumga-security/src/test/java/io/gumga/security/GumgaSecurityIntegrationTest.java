package io.gumga.security;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringConfigForTest.class,WebConfigForTest.class})
public class GumgaSecurityIntegrationTest {

    @Autowired
    private GumgaSecurityIntegration gumgaSecurityIntegration;

    public GumgaSecurityIntegrationTest() {
    }

    @Before
    public void setUp() {
    }

    @Test
    public void testVerifyAndCreateOperations() {
        gumgaSecurityIntegration.verifyAndCreateOperations(new GumgaOperationTO[]{
            new GumgaOperationTO("RESET_INSTANCE", false),
            new GumgaOperationTO("VERIFY_INSTANCE", true),
            new GumgaOperationTO("RESET_ORGANIZATION", false),
            new GumgaOperationTO("VERIFY_ORGANIZATION", false)
        });
        assertTrue(true);
    }

}
