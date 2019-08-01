package io.gumga.application;

import io.gumga.core.GumgaThreadScope;
import io.gumga.core.QueryObject;
import io.gumga.core.SearchResult;
import io.gumga.testmodel.Company;
import io.gumga.testmodel.CompanyRepository;
import io.gumga.testmodel.CompanyService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfig.class})
public class GumgaFinderTest {

    public GumgaFinderTest() {
        GumgaThreadScope.organizationCode.set("1.");
        GumgaThreadScope.login.set("gumga@gumga.com.br");
    }

    @Autowired
    private CompanyService service;

    @Autowired
    private CompanyRepository companyRespository;


    @Test
    public void injectionSanityCheck() {
        assertNotNull(service);
    }

    @Test
    @Transactional
    public void criaEmpresaePesquisaViaQueryObject() {
        GumgaThreadScope.organizationCode.set("1.");
        Company empresa = new Company();
        empresa.setName("Gumga");
        service.save(empresa);
        empresa = new Company();
        empresa.setName("Agmug");
        service.save(empresa);
        GumgaThreadScope.organizationCode.set("2.");
        empresa = new Company();
        empresa.setName("Other");
        service.save(empresa);
        GumgaThreadScope.organizationCode.set("1.");
        QueryObject query = new QueryObject();
        query.setQ("Gumga");
        query.setSearchFields("name");
        query.setSortField("name");
        List<Company> result = service.pesquisa(query).getValues();
        assert (!result.isEmpty());
    }

    @Test
    @Transactional
    public void verificaProcuraEmTodos() {
companyRespository.deleteAll();

        GumgaThreadScope.organizationCode.set("1.");
        Company empresa = new Company();
        empresa.setName("Gumga");
        service.save(empresa);
        empresa = new Company();
        empresa.setName("Agmug");
        service.save(empresa);
        GumgaThreadScope.organizationCode.set("2.");
        empresa = new Company();
        empresa.setName("Other");
        service.save(empresa);
        GumgaThreadScope.organizationCode.set("1.");
        QueryObject query = new QueryObject();
        SearchResult<Company> pesquisa = service.pesquisa(query);
        assertEquals(2l, pesquisa.getCount().longValue());
    }

    @Test
    @Transactional
    public void criaEmpresaePesquisaAvancadaViaQueryObject() {
      companyRespository.deleteAll(); 
       GumgaThreadScope.organizationCode.set("1.");
        Company empresa = new Company();
        empresa.setName("Gumga");
        service.save(empresa);
        empresa = new Company();
        empresa.setName("Agmug");
        service.save(empresa);
        GumgaThreadScope.organizationCode.set("2.");
        empresa = new Company();
        empresa.setName("Other");
        service.save(empresa);
        GumgaThreadScope.organizationCode.set("1.");
        QueryObject query = new QueryObject();
        query.setAq("obj.name like '%'");
        SearchResult<Company> pesquisa = service.pesquisa(query);
        assertEquals(2l, pesquisa.getCount().longValue());
    }

    @Test
    @Transactional
    public void criaEmpresaePesquisaAvancadaOrdenadaViaQueryObject() {
        Company empresa = new Company();
        empresa.setName("Gumga");
        service.save(empresa);
        empresa = new Company();
        empresa.setName("Agmug");
        service.save(empresa);
        QueryObject query = new QueryObject();
        query.setSortField("name");
        query.setAq("obj.name like '%'");
        SearchResult<Company> pesquisa = service.pesquisa(query);
        assertFalse(pesquisa.getValues().isEmpty());
    }

}
