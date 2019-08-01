package io.gumga.application;

import io.gumga.core.GumgaThreadScope;
import io.gumga.domain.domains.GumgaBoolean;
import io.gumga.testmodel.Lamp;
import io.gumga.testmodel.LampService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfig.class})
public class GumgaDomainsTest {

    @Autowired
    private LampService lampService;

    public GumgaDomainsTest() {
        GumgaThreadScope.organizationCode.set("1.");
        GumgaThreadScope.login.set("gumga@gumga.com.br");
    }

    @Test
    @Transactional
    public void gumgaBooleanVersion() {
        Lamp l=new Lamp("lampada 1", new GumgaBoolean(true));
        l=lampService.save(l);
        assertEquals(0, l.getVersion().intValue());
        l=lampService.view(l.getId());
        l.setIson(new GumgaBoolean(true));
        l=lampService.save(l);
        assertEquals(0, l.getVersion().intValue());
    }
    

}
