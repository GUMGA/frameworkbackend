package io.gumga.application.sharedmodel;

import io.gumga.application.Bus;
import io.gumga.application.BusRepository;
import io.gumga.application.BusService;
import io.gumga.application.SpringConfig;
import io.gumga.core.GumgaThreadScope;
import io.gumga.core.QueryObject;
import io.gumga.core.SearchResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
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

    @Before
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

    @Test(expected = Exception.class)
    @Transactional
    public void removeListBus() {
        GumgaThreadScope.organizationCode.set("9.");
        GumgaThreadScope.login.set("munif@gumga.com.br");

        QueryObject qo = new QueryObject();
        qo.setAq("obj.line like '%'");
        SearchResult<Bus> pesquisa = busService.pesquisa(qo);
        for (Bus b : pesquisa.getValues()) {
            busService.delete(b);
        }
    }

}
