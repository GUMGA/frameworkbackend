/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.application;

import io.gumga.core.GumgaThreadScope;
import io.gumga.core.QueryObject;
import io.gumga.domain.domains.GumgaOi;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author wlademir
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringConfig.class})
public class GumgaCrudAndQueryNotOnlyTypedRepositoryImplTest {

    @Autowired
    TaskRepository repository;

    @Autowired
    private CarRepository carRepository;

    private static boolean notPersisted = true;

    @Before
    public void setUp() {
        if (notPersisted) {
            GumgaThreadScope.organizationCode.set("1");

            repository.save(new Task("Cadastrar Pessoa"));
            repository.save(new Task("Cadastrar Funcionario"));
            repository.save(new Task("Cadastrar Fornecedor"));
            repository.save(new Task("Cadastrar Tipo"));
            repository.save(new Task("Cadastrar Feriado"));
            repository.save(new Task("Cadastrar Recesso"));
            repository.save(new Task("Cadastrar Facultativo"));
            repository.save(new Task("Cadastrar Observação"));

            GumgaThreadScope.organizationCode.set("2");

            repository.save(new Task("Deletar Pessoa"));
            repository.save(new Task("Deletar Funcionario"));
            repository.save(new Task("Deletar Fornecedor"));
            repository.save(new Task("Deletar Tipo"));
            repository.save(new Task("Deletar Feriado"));
            repository.save(new Task("Deletar Recesso"));
            repository.save(new Task("Deletar Facultativo"));
            repository.save(new Task("Deletar Observação"));

            GumgaThreadScope.organizationCode.set("3");

            repository.save(new Task("Alterar Pessoa"));
            repository.save(new Task("Alterar Funcionario"));
            repository.save(new Task("Alterar Fornecedor"));
            repository.save(new Task("Alterar Tipo"));
            repository.save(new Task("Alterar Feriado"));
            repository.save(new Task("Alterar Recesso"));
            repository.save(new Task("Alterar Facultativo"));
            repository.save(new Task("Alterar Observação"));

            notPersisted = false;
        }
    }

    /**
     * Test of getUniqueResult method, of class
     * GumgaCrudAndQueryNotOnlyTypedRepositoryImpl.
     */
    @Test
    public void testGetUniqueResult() {
        QueryObject q = new QueryObject();
        
        System.out.println("getUniqueResult");
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("param", "Deletar Pessoa");
        Task expResult = new Task("Deletar Pessoa");
        Task result = repository.getUniqueResult("select obj from Task obj where obj.name = :param", Task.class, parametros);
        assertEquals(expResult.getName(), result.getName());

    }

    /**
     * Test of getResultList method, of class
     * GumgaCrudAndQueryNotOnlyTypedRepositoryImpl.
     */
    @Test
    public void testGetResultListMap() {
        System.out.println("getResultList Map");
        List result = repository.getResultList("select new map(obj.oi,count(obj.oi)) from Task obj group by obj.oi",  new HashMap<>());
        result.stream().forEach((Object result1) -> {
            ((Map<Integer,Integer>)result1).entrySet().stream().forEach((entrySet) -> {
                System.out.println("Index: "+entrySet.getKey()+"; value: "+entrySet.getValue());
            });
            System.out.println("");
        });
    
        assertTrue(!result.isEmpty());
    }
    
    @Test
    public void testGetResultList_2args() {
        System.out.println("getResultList 2 args");
        List<Task> result = repository.getResultList("select obj from Task obj",  new HashMap<>());
        assertTrue(!result.isEmpty());
    }

    /**
     * Test of getResultList method, of class
     * GumgaCrudAndQueryNotOnlyTypedRepositoryImpl.
     */
    @Test
    public void testGetResultList_3args() {
        System.out.println("getResultList 3 args");
        Map<String, Object> param = new HashMap<>();
        param.put("param", "Cadastrar%");
        param.put("oi", new GumgaOi("1"));
        
        List<Task> result = repository.getResultList("select obj from Task obj where obj.name like(:param) and obj.oi=:oi",  param,7);
        assertTrue(result.size()==7);
    }

    /**
     * Test of getResultList method, of class
     * GumgaCrudAndQueryNotOnlyTypedRepositoryImpl.
     */
    @Test
    public void testGetResultList_4args() {
        System.out.println("getResultList 4 args");
        Map<String, Object> param = new HashMap<>();
        param.put("param", "Alterar%");
        
        List<Task> result = repository.getResultList("select obj from Task obj where obj.name like(:param)",  param,5,5);
        assertTrue(result.size()==3);
    }

    /**
     * Test of getJPAQuerydsl method, of class
     * GumgaCrudAndQueryNotOnlyTypedRepositoryImpl.
     */
    @Test
    public void testGetJPAQuerydsl() {
        System.out.println("getJPAQuerydsl"+repository.getJPAQuerydsl().from(QCar.car).count());
        assertTrue(repository.getJPAQuerydsl().from(QTask.task).count() > 0);
    }
    
    @Test
    public void testQueryImplementationInterfa(){
        System.out.println("testQueryImplementationInterfa");
        List<Task>result =repository.getTaskDescriptionAndOi("%Pessoa%", Arrays.asList(new GumgaOi("1"), new GumgaOi("2")));
        assertTrue(result.size()==2);
    }

}
