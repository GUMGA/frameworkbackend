package io.gumga.sharedModel;

import io.gumga.application.SpringConfig;
import io.gumga.core.GumgaThreadScope;
import io.gumga.testmodel.PersonSharedModel;
import io.gumga.testmodel.PersonSharedRepositoryModel;
import io.gumga.testmodel.PersonSharedServiceModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfig.class})
public class GumgaSharedModelTest {

    @Autowired
    PersonSharedServiceModel service;
    @Autowired
    PersonSharedRepositoryModel repository;

    @BeforeEach
    @Transactional
    public void setUp() throws Exception {
        GumgaThreadScope.organizationCode.set("1.");
        PersonSharedModel person1 = new PersonSharedModel("Caito Mango Evergreen");
        person1.addOrganization("2.");
        person1.addOrganization("3.");
        service.save(person1);
        GumgaThreadScope.organizationCode.set("1.");
        PersonSharedModel person2 = new PersonSharedModel("Luska McGinos Henry");
        service.save(person2);
    }

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
    }
}
