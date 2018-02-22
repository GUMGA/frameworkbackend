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
public class GumgaModelTest {

    @Autowired
    PersonServiceModel service;
    @Autowired
    PersonRepositoryModel repository;

    @Before
    @Transactional
    public void setUp() throws Exception {

        GumgaThreadScope.organizationCode.set("1.");
        service.save(new PersonModel("Caito Mango Evergreen"));
        GumgaThreadScope.organizationCode.set("2.");
        service.save(new PersonModel("Luska McGinos Henry"));
        GumgaThreadScope.organizationCode.set("2.1.");
        service.save(new PersonModel("Philip Passoca Vraw"));
    }


    @Test
    @Transactional
    public void sharedModel() throws Exception {
        GumgaThreadScope.organizationCode.set("2.");
        assertEquals(2, repository.findAll().size());
    }
}
