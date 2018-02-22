package io.gumga.sharedModel;

import io.gumga.application.SpringConfig;
import io.gumga.core.GumgaThreadScope;
import io.gumga.testmodel.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringConfig.class})
public class GumgaSharedUUIDModelTest {

    @Autowired
    PersonSharedUUIDServiceModel service;
    @Autowired
    PersonSharedUUIDRepositoryModel repository;

    @Before
    @Transactional
    public void setUp() throws Exception {
        GumgaThreadScope.organizationCode.set("1.");
        PersonSharedUUIDModel person1 = new PersonSharedUUIDModel("Caito Mango Evergreen");
        person1.addOrganization("2.");
        person1.addOrganization("3.");
        service.save(person1);
    }
    /**
     * Teste de compartilhamento da GumgaSharedModelUUID
     * @see io.gumga.domain.GumgaSharedModelUUID
     * @throws Exception
     */
    @Test
    @Transactional
    public void sharedModel() throws Exception {
        GumgaThreadScope.organizationCode.set("1.");
        assertNotEquals(1, repository.findAll().get(0).getId());

        GumgaThreadScope.organizationCode.set("2.");
        assertEquals(1, repository.findAll().size());
        GumgaThreadScope.organizationCode.set("3.");
        assertEquals(1, repository.findAll().size());
    }
}
