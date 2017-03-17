package io.gumga.application;

import io.gumga.core.GumgaThreadScope;
import io.gumga.domain.domains.GumgaBoolean;
import io.gumga.testmodel.Lamp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.Assert.*;
import org.junit.Ignore;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringConfig.class})
@Ignore
public class GumgaDomainsTest {

    public GumgaDomainsTest() {
        GumgaThreadScope.organizationCode.set("1.");
        GumgaThreadScope.login.set("gumga@gumga.com.br");
    }

    @Test
    public void gumgaBooleanVersion() {
        Lamp l=new Lamp("lampada 1", new GumgaBoolean(true));
    }

}
