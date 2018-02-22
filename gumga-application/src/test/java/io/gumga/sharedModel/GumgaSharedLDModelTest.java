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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringConfig.class})
public class GumgaSharedLDModelTest {

    @Autowired
    PersonSharedLDServiceModel service;
    @Autowired
    PersonSharedLDRepositoryModel repository;

    @Before
    @Transactional
    public void setUp() throws Exception {
        GumgaThreadScope.organizationCode.set("1.");
        PersonSharedLDModel person1 = new PersonSharedLDModel("Caito Mango Evergreen");
        person1.addOrganization("2.");
        person1.addOrganization("3.");
        service.save(person1);
        GumgaThreadScope.organizationCode.set("1.");
        PersonSharedLDModel person2 = new PersonSharedLDModel("Luska McGinos Henry");
        service.save(person2);
    }

    /**
     * Teste de compartilhamento de entidades GumgaSharedLDModel
     * @see io.gumga.domain.logicaldelete.GumgaSharedLDModel
     * @throws Exception
     */
    @Test
    @Transactional
    public void sharedModel() throws Exception {
        GumgaThreadScope.organizationCode.set("1.");
        assertEquals(2, repository.findAll().size());

        GumgaThreadScope.organizationCode.set("2.");
        assertEquals(1, repository.findAll().size());
        assertEquals("Caito Mango Evergreen", repository.findAll().get(0).getNome());

        GumgaThreadScope.organizationCode.set("3.");
        assertEquals(1, repository.findAll().size());
        assertEquals("Caito Mango Evergreen", repository.findAll().get(0).getNome());
    }
}
