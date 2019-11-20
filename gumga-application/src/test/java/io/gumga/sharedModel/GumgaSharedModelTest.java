package io.gumga.sharedModel;

import io.gumga.application.SpringConfig;
import io.gumga.core.GumgaThreadScope;
import io.gumga.domain.shared.GumgaSharedModel;
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

        GumgaThreadScope.organizationCode.set("1000.");
        PersonSharedModel base = new PersonSharedModel("Base");
        service.save(base);

        PersonSharedModel sharedBase = new PersonSharedModel("SharedBase");
        sharedBase.addOrganization(String.format(GumgaSharedModel.SHARED_BASE, "1000"));
        service.save(sharedBase);

        GumgaThreadScope.organizationCode.set("1000.1001.");
        PersonSharedModel matrix = new PersonSharedModel("matrix");
        service.save(matrix);

        PersonSharedModel sharedMatrix = new PersonSharedModel("SharedMatrix");
        sharedMatrix.addOrganization(String.format(GumgaSharedModel.SHARED_MATRIX, "1000", "1001"));
        service.save(sharedMatrix);


        GumgaThreadScope.organizationCode.set("1000.1001.1003.");
        PersonSharedModel bmo1 = new PersonSharedModel("BMO 1");
        service.save(bmo1);

        PersonSharedModel bmo1Shared = new PersonSharedModel("BMO 1 Shared");
        bmo1Shared.addOrganization(String.format(GumgaSharedModel.SHARED_MATRIX, "1000", "1001"));
        service.save(bmo1Shared);

        GumgaThreadScope.organizationCode.set("1000.1001.1004.");
        PersonSharedModel bmo2 = new PersonSharedModel("BMO 2");
        service.save(bmo2);
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

    @Test
    @Transactional
    public void sharedBase() {
        GumgaThreadScope.organizationCode.set("1000.");
        assertEquals(7, repository.findAll().size());
    }

    @Test
    @Transactional
    public void sharedMatrix() {
        GumgaThreadScope.organizationCode.set("1000.1001.");
        assertEquals(6, repository.findAll().size());
    }

    @Test
    @Transactional
    public void sharedBmo1() {
        GumgaThreadScope.organizationCode.set("1000.1001.1003.");
        assertEquals(4, repository.findAll().size());
    }

    @Test
    @Transactional
    public void sharedBmo2() {
        GumgaThreadScope.organizationCode.set("1000.1001.1004.");
        assertEquals(4, repository.findAll().size());
    }
}
