package io.gumga.alldatabases;

import com.mysema.commons.lang.Assert;
import io.gumga.core.GumgaThreadScope;
import io.gumga.core.QueryObject;
import io.gumga.core.gquery.ComparisonOperator;
import io.gumga.core.gquery.Criteria;
import io.gumga.core.gquery.GQuery;
import io.gumga.testmodel.Company;
import io.gumga.testmodel.CompanyRepository;
import io.gumga.testmodel.CompanyService;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import static org.junit.Assert.assertEquals;

import io.gumga.testmodel.Tipo;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.management.Query;

public abstract class AllDatabasesTest {

    @Autowired
    protected CompanyService service;
    @Autowired
    protected CompanyRepository repository;

    @Before
    @Transactional
    public void setUp() {
        Calendar dia = Calendar.getInstance();
        dia.set(1975, 8, 18, 10, 0, 0);
        GumgaThreadScope.organizationCode.set("1.");
        if (service.count() > 0) {
            return;
        }

        service.save(new Company(1001L, new BigDecimal("10.41"), Tipo.SIMPLES, "João", dia.getTime(), (true)));
        dia.set(1975, 8, 18, 11, 0, 0);
        service.save(new Company(100L, new BigDecimal(40), Tipo.AVANCADO,"José", dia.getTime(), (false)));
        dia.set(1985, 8, 18, 11, 0, 0);
        service.save(new Company("Maria", dia.getTime(), (true)));
        dia.set(1985, 8, 18, 11, 0, 0);
        service.save(new Company("Jose", dia.getTime(), (false)));
        dia.set(1985, 8, 18, 11, 0, 0);
        service.save(new Company("JOAO", dia.getTime(), (false)));
        dia.set(1985, 8, 18, 11, 0, 0);
        service.save(new Company("CaRlOs", dia.getTime(), (false)));
        dia.set(1900, 8, 18, 11, 0, 0);
        service.save(new Company("AMÁRILDO SANTOS", dia.getTime(), (false)));
        GumgaThreadScope.organizationCode.set("2.");
        service.save(new Company("João da Outra", dia.getTime(), (true)));

    }

    @Test
    @Transactional
    public void maiusculasBuscaSimples() {
        GumgaThreadScope.organizationCode.set("1.");
        QueryObject query = new QueryObject();
        query.setQ("carlos");
        query.setSearchFields("name");
        query.setSortField("name");
        List<Company> result = service.pesquisa(query).getValues();
        System.out.println("MaiusculasBuscaSimples---->" + result);
        assertEquals(1, result.size());
    }

    @Test
    @Transactional
    public void maiusculasBuscaAvancada() {
        GumgaThreadScope.organizationCode.set("1.");
        QueryObject query = new QueryObject();
        query.setAq("upper(obj.name) like upper('%carlos%')");
        List<Company> result = service.pesquisa(query).getValues();
        System.out.println("MaiusculasBuscaAvancada---->" + result);
        assertEquals(1, result.size());
    }

    @Test
    @Transactional
    public void acentosEMaiusculasBuscaSimples() {
        GumgaThreadScope.organizationCode.set("1.");
        QueryObject query = new QueryObject();
        query.setQ("joão");
        query.setSearchFields("name");
        query.setSortField("name");
        List<Company> result = service.pesquisa(query).getValues();
        System.out.println("acentosEMaiusculasBuscaSimples---->" + result);
        //Assert.isTrue(result.size()==1||result.size()==2, "Um ou dois por causa do postgress e do h2");
        Assert.isTrue(result.size() == 2, "Um ou dois por causa do postgress e do h2");
    }

    @Test
    @Transactional
    public void acentosEMaiusculasBuscaSimples2() {
        GumgaThreadScope.organizationCode.set("1.");
        QueryObject query = new QueryObject();
        query.setPhonetic(true);
        query.setQ("joao");
        query.setSearchFields("name");
        query.setSortField("name");
        List<Company> result = service.pesquisa(query).getValues();
        System.out.println("acentosEMaiusculasBuscaSimples---->" + result);
        //Assert.isTrue(result.size()==1||result.size()==2, "Um ou dois por causa do postgress e do h2");
        Assert.isTrue(result.size() == 2, "Um ou dois por causa do postgress e do h2");
    }

    @Test
    @Transactional
    public void acentosEMaiusculasBuscaAvancada() {
        GumgaThreadScope.organizationCode.set("1.");
        QueryObject query = new QueryObject();
        query.setAq("upper(obj.name) like upper('%joao%') or upper(obj.name) like upper('%joão%')");
        List<Company> result = service.pesquisa(query).getValues();
        System.out.println("acentosEMaiusculasBuscaAvancada---->" + result);
        assertEquals(2, result.size());
    }

    @Test
    @Transactional
    public void acentosEMaiusculasBuscaAvancada2() {
        GumgaThreadScope.organizationCode.set("1.");
        QueryObject query = new QueryObject();
        query.setAq("upper(obj.name) like upper('%amá%')");
        List<Company> result = service.pesquisa(query).getValues();
        System.out.println("acentosEMaiusculasBuscaAvancada---->" + result);
        assertEquals(1, result.size());
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
        query.setAq("obj.ativo IS FALSE");
        List<Company> result = service.pesquisa(query).getValues();
        System.out.println("\nbooleanSimples---->" + result + "\n");
        assertEquals(5, result.size());
    }

    @Test
    @Transactional
    public void dataBuscaSimples() { //Não FUNCIONA
        GumgaThreadScope.organizationCode.set("1.");
        QueryObject query = new QueryObject();
        query.setQ("18/09/1975");
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
        query.setAq("obj.date >= to_timestamp('1975-09-18 00:00:00','yyyy/MM/dd HH24:mi:ss') AND obj.date <= to_timestamp('1975-09-19 23:59:59','yyyy/MM/dd HH24:mi:ss')");
        //query.setAq("obj.date >= '1975-09-18' AND obj.date <= '1975-09-19'");
        List<Company> result = service.pesquisa(query).getValues();
        System.out.println("\ndataBuscaSimples---->" + result + "\n");
        assertEquals(2, result.size());
    }

    @Test
    @Transactional
    public void dataBuscaAvancada2() {
        GumgaThreadScope.organizationCode.set("1.");
        QueryObject query = new QueryObject();
        query.setAq("obj.date > to_timestamp('1980-01-11 00:00:00','yyyy/MM/dd HH24:mi:ss')");
        //query.setAq("obj.date > '1980-01-11'");
        List<Company> result = service.pesquisa(query).getValues();
        System.out.println("\ndataBuscaSimples---->" + result + "\n");
        assertEquals(4, result.size());
    }

    @Test
    @Transactional
    public void gQueryEmpty() {
        GumgaThreadScope.organizationCode.set("1.");
        QueryObject query = new QueryObject();
        query.setgQuery(new GQuery());
        List<Company> result = service.pesquisa(query).getValues();
        assertEquals(7, result.size());
    }

    @Test
    @Transactional
    public void gQueryEQUAL() {
        GumgaThreadScope.organizationCode.set("1.");
        QueryObject query = new QueryObject();
        GQuery gQuery = new GQuery(new Criteria("name", ComparisonOperator.EQUAL, "carlos").addIgnoreCase());
        query.setgQuery(gQuery);
        List<Company> result = service.pesquisa(query).getValues();
        assertEquals(1, result.size());
    }

    @Test
    @Transactional
    public void gQueryStartsIgnoreCase() {
        GumgaThreadScope.organizationCode.set("1.");
        QueryObject query = new QueryObject();
        GQuery gQuery = new GQuery(new Criteria("name", ComparisonOperator.STARTS_WITH, "j").addIgnoreCase());
        query.setgQuery(gQuery);
        List<Company> result = service.pesquisa(query).getValues();
        assertEquals(4, result.size());
    }

    @Test
    @Transactional
    public void gQueryActive() {
        GumgaThreadScope.organizationCode.set("1.");
        QueryObject query = new QueryObject();
        GQuery gQuery = new GQuery(new Criteria("ativo", ComparisonOperator.EQUAL, "1"));
        query.setgQuery(gQuery);
        List<Company> result = service.pesquisa(query).getValues();
        assertEquals(2, result.size());
    }

    @Test
    @Transactional
    public void gQueryActiveNameWithJ() {
        GumgaThreadScope.organizationCode.set("1.");
        QueryObject query = new QueryObject();
        GQuery gQuery = new GQuery(new Criteria("ativo", ComparisonOperator.EQUAL, "1"));
        gQuery = gQuery.and(new Criteria("name", ComparisonOperator.STARTS_WITH, "j").addIgnoreCase());
        query.setgQuery(gQuery);
        List<Company> result = service.pesquisa(query).getValues();

        System.out.println("gQueryActiveNameWithJ------>" + gQuery + "/" + result);
        assertEquals(1, result.size());
    }

    @Test
    @Transactional
    public void gQueryStartsIgnoreCaseAndAcentos() {
        GumgaThreadScope.organizationCode.set("1.");
        QueryObject query = new QueryObject();
        GQuery gQuery = new GQuery(new Criteria("name", ComparisonOperator.STARTS_WITH, "jose").addIgnoreCase().addTranslate());
        query.setgQuery(gQuery);
        List<Company> result = service.pesquisa(query).getValues();
        assertEquals(2, result.size());
    }

    @Test
    @Transactional
    public void findCompanyEqualsIdGQuery() {
        GumgaThreadScope.organizationCode.set("1.");
        Company company = this.repository.findAll().get(0);
        GQuery gQuery = new GQuery(new Criteria("id", ComparisonOperator.EQUAL, company.getId()));
        QueryObject query = new QueryObject();
        query.setgQuery(gQuery);

        int count = service.pesquisa(query).getValues().size();
        assertEquals(1, count);
    }

    @Test
    @Transactional
    public void findCompanyGreaterQuantidadeGQuery() {
        GumgaThreadScope.organizationCode.set("1.");
        GQuery gQuery = new GQuery(new Criteria("quantidade", ComparisonOperator.GREATER, 1000));
        QueryObject query = new QueryObject();
        query.setgQuery(gQuery);
        int count = service.pesquisa(query).getValues().size();
        assertEquals(1, count);
    }

    @Test
    @Transactional
    public void findCompanyEqualValorBigDecimalGQuery() {
        GumgaThreadScope.organizationCode.set("1.");
        GQuery gQuery = new GQuery(new Criteria("valor", ComparisonOperator.EQUAL, new BigDecimal("10.41")));
        QueryObject query = new QueryObject();
        query.setgQuery(gQuery);
        int count = service.pesquisa(query).getValues().size();
        assertEquals(1, count);
    }

    @Test
    @Transactional
    public void findCompanyEqualEnumGQuery() {
        GumgaThreadScope.organizationCode.set("1.");
        GQuery gQuery = new GQuery(new Criteria("tipo", ComparisonOperator.EQUAL, Tipo.AVANCADO));
        QueryObject query = new QueryObject();
        query.setgQuery(gQuery);
        int count = service.pesquisa(query).getValues().size();
        assertEquals(1, count);
    }

    @Test
    @Transactional
    public void findCompanyEqualsDateGQuery() {
        GumgaThreadScope.organizationCode.set("1.");
        Calendar dia = Calendar.getInstance();
        dia.set(1900, 8, 18, 11, 0, 0);

        GQuery gQuery = new GQuery(new Criteria("date", ComparisonOperator.EQUAL, dia.getTime()));
        QueryObject query = new QueryObject();
        query.setgQuery(gQuery);
        System.out.println("DATE:"+gQuery.toString());

        int count = service.pesquisa(query).getValues().size();
        assertEquals(1, count);
    }



}
