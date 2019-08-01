package io.gumga.sharedModel;

import io.gumga.application.SpringConfig;
import io.gumga.core.GumgaThreadScope;
import io.gumga.testmodel.PersonSharedUUIDModel;
import io.gumga.testmodel.PersonSharedUUIDRepositoryModel;
import io.gumga.testmodel.PersonSharedUUIDServiceModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfig.class})
public class GumgaSharedUUIDModelTest {

    @Autowired
    PersonSharedUUIDServiceModel service;
    @Autowired
    PersonSharedUUIDRepositoryModel repository;

    @BeforeEach
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
