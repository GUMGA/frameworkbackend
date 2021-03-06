package io.gumga.application.sharedmodel;

import io.gumga.application.SpringConfig;
import io.gumga.core.GumgaThreadScope;
import io.gumga.core.QueryObject;
import io.gumga.core.SearchResult;
import io.gumga.testmodel.Bus;
import io.gumga.testmodel.BusRepository;
import io.gumga.testmodel.BusService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfig.class})
public class SharedModelTest {

    @Autowired
    private BusService busService;

    @Autowired
    private BusRepository busRepository;

    @Test
    public void injectionSanityCheck() {
        assertNotNull(busService);
        assertNotNull(busRepository);
    }

    @BeforeEach
    @Transactional
    public void insertData() {
        GumgaThreadScope.organizationCode.set("1.");
        GumgaThreadScope.login.set("munif@gumga.com.br");

        if (busRepository.findAll().size() > 0) {
            return;
        }

        Bus linha103 = new Bus("103");
        linha103.addOrganization("3.1.");
        linha103.addUser("munif@gumga.com.br");
        Bus linha104 = new Bus("104");
        linha104.addOrganization("2.1.");
        linha104.addOrganization("5.");
        busRepository.save(linha103);
        busRepository.save(linha104);
        GumgaThreadScope.organizationCode.set("2.");
        Bus linha503 = new Bus("503");
        linha503.addOrganization("5.");
        linha503.addUser("munif@gumga.com.br");
        busRepository.save(linha503);
    }

    @Test
    @Transactional
    public void listBusQ() {
        GumgaThreadScope.organizationCode.set("2.1.");
        QueryObject qo = new QueryObject();
        SearchResult<Bus> pesquisa = busService.pesquisa(qo);
        assertEquals(3l, pesquisa.getCount().longValue());
    }

    @Test
    @Transactional
    public void listBusAQ() {
        GumgaThreadScope.organizationCode.set("2.1.");

        QueryObject qo = new QueryObject();
        qo.setAq("obj.line like '%'");
        SearchResult<Bus> pesquisa = busService.pesquisa(qo);
        assertEquals(3l, pesquisa.getCount().longValue());
    }

    @Test
    @Transactional
    public void viewListBusAQ() {
        GumgaThreadScope.organizationCode.set("9.");
        GumgaThreadScope.login.set("munif@gumga.com.br");

        QueryObject qo = new QueryObject();
        qo.setAq("obj.line like '%'");
        SearchResult<Bus> pesquisa = busService.pesquisa(qo);
        for (Bus b : pesquisa.getValues()) {
            assertNotNull(busService.view(b.getId()));
        }
    }

    @Test
    @Transactional
    public void removeListBus() {
        Assertions.assertThrows(Exception.class, () -> {
            GumgaThreadScope.organizationCode.set("9.");
            GumgaThreadScope.login.set("munif@gumga.com.br");

            QueryObject qo = new QueryObject();
            qo.setAq("obj.line like '%'");
            SearchResult<Bus> pesquisa = busService.pesquisa(qo);
            for (Bus b : pesquisa.getValues()) {
                busService.delete(b);
            }
        });

    }

}
