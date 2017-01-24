package io.gumga.alldatabases;

import io.gumga.core.GumgaThreadScope;
import io.gumga.core.QueryObject;
import io.gumga.testmodel.Company;
import io.gumga.testmodel.CompanyService;
import java.util.Calendar;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public abstract class AllDatabasesTest {

    @Autowired
    protected CompanyService service;

    @Before
    @Transactional
    public void setUp() {
        Calendar dia = Calendar.getInstance();
        dia.set(1975, 8, 18, 10, 0, 0);
        GumgaThreadScope.organizationCode.set("1.");
        service.save(new Company("João", dia.getTime(), (true)));
        dia.set(1975, 8, 18, 11, 0, 0);
        service.save(new Company("José", dia.getTime(), (false)));
        dia.set(1985, 8, 18, 11, 0, 0);
        service.save(new Company("Maria", dia.getTime(), (true)));
        dia.set(1985, 8, 18, 11, 0, 0);
        service.save(new Company("Jose", dia.getTime(), (false)));
        dia.set(1985, 8, 18, 11, 0, 0);
        service.save(new Company("JOAO", dia.getTime(), (false)));

    }

    @Test
    @Transactional
    public void maiusculasBuscaSimples() {
        GumgaThreadScope.organizationCode.set("1.");
        QueryObject query = new QueryObject();
        query.setQ("joao");
        query.setSearchFields("name");
        query.setSortField("name");
        List<Company> result = service.pesquisa(query).getValues();
        System.out.println("acentosEMaiusculasBuscaSimples---->" + result);
        assertEquals(2, result.size());
    }

    @Test
    @Transactional
    public void maiusculasBuscaAvancada() {
        GumgaThreadScope.organizationCode.set("1.");
        QueryObject query = new QueryObject();
        query.setAq("obj.name like '%joao%'");
        List<Company> result = service.pesquisa(query).getValues();
        System.out.println("acentosEMaiusculasBuscaSimples---->" + result);
        assertEquals(2, result.size());
    }

    @Test
    @Transactional
    public void acentosEMaiusculasBuscaSimples() {
        GumgaThreadScope.organizationCode.set("1.");
        QueryObject query = new QueryObject();
        query.setQ("joao");
        query.setSearchFields("name");
        query.setSortField("name");
        List<Company> result = service.pesquisa(query).getValues();
        System.out.println("acentosEMaiusculasBuscaSimples---->" + result);
        assertEquals(2, result.size());
    }

    @Test
    @Transactional
    public void acentosEMaiusculasBuscaAvancada() {
        GumgaThreadScope.organizationCode.set("1.");
        QueryObject query = new QueryObject();
        query.setAq("obj.name like '%joao%'");
        List<Company> result = service.pesquisa(query).getValues();
        System.out.println("acentosEMaiusculasBuscaAvancada---->" + result);
        assertEquals(2, result.size());
    }

    @Test
    @Transactional
    public void booleanBuscaSimples() {
        GumgaThreadScope.organizationCode.set("1.");
        QueryObject query = new QueryObject();
        query.setQ("TRUE");
        query.setSearchFields("ativo");
        List<Company> result = service.pesquisa(query).getValues();
        System.out.println("\nbooleanSimples---->" + result + "\n");
        assertEquals(2, result.size());
    }

    @Test
    @Transactional
    public void booleanBuscaAvancado() {
        GumgaThreadScope.organizationCode.set("1.");
        QueryObject query = new QueryObject();
        query.setAq("obj.ativo='false'");
        List<Company> result = service.pesquisa(query).getValues();
        System.out.println("\nbooleanSimples---->" + result + "\n");
        assertEquals(3, result.size());
    }

    @Test
    @Transactional
    @Ignore
    public void dataBuscaSimples() { //Não FUNCIONA
        GumgaThreadScope.organizationCode.set("1.");
        QueryObject query = new QueryObject();
        query.setQ("");
        query.setSearchFields("date");
        List<Company> result = service.pesquisa(query).getValues();
        System.out.println("\ndataBuscaSimples---->" + result + "\n");
        assertEquals(2, result.size());
    }

    @Test
    @Transactional
    public void dataBuscaAvancada() {
        GumgaThreadScope.organizationCode.set("1.");
        QueryObject query = new QueryObject();
        query.setAq("obj.date = '1975-09-18 10:00:00'");
        List<Company> result = service.pesquisa(query).getValues();
        System.out.println("\ndataBuscaSimples---->" + result + "\n");
        assertEquals(1, result.size());
    }

    @Test
    @Transactional
    public void dataBuscaAvancada2() {
        GumgaThreadScope.organizationCode.set("1.");
        QueryObject query = new QueryObject();
        query.setAq("obj.date > '1980-01-01'");
        List<Company> result = service.pesquisa(query).getValues();
        System.out.println("\ndataBuscaSimples---->" + result + "\n");
        assertEquals(3, result.size());
    }

}
