package io.gumga.logicaldelete;

import io.gumga.application.SpringConfig;
import io.gumga.core.GumgaThreadScope;
import io.gumga.core.QueryObject;
import io.gumga.testmodel.Book;
import io.gumga.testmodel.BookRepository;
import io.gumga.testmodel.BookService;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@Disabled
//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = {SpringConfig.class})
public class LogicalDeleteTest {
/*
    @Autowired
    protected BookService bookService;
    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    @Transactional
    public void setUp() {
        bookRepository.deleteAll();
        GumgaThreadScope.organizationCode.set("1.");
        bookService.save(new Book("Lord of the Rings"));
        bookService.save(new Book("Do Androids Dream of Electric Sheep?"));
        bookService.save(new Book("Player One"));
        bookService.save(new Book("To Kill a Mockingbird"));
    }

    @Test
    @Transactional
    public void consultaSimples() {
        GumgaThreadScope.organizationCode.set("1.");
        QueryObject query = new QueryObject();
        query.setQ("Lord");
        query.setSearchFields("title");
        List<Book> result = bookService.pesquisa(query).getValues();
        System.out.println("MaiusculasBuscaSimples---->" + result);
        assertEquals(1, result.size());
    }

    @Test
    @Transactional
    public void consultaTodos() {
        GumgaThreadScope.organizationCode.set("1.");
        QueryObject query = new QueryObject();
        List<Book> result = bookService.pesquisa(query).getValues();
        assertEquals(4, result.size());
    }

    @Test
    @Transactional
    public void consultaTodosInativosViaQ() {
        GumgaThreadScope.organizationCode.set("1.");
        QueryObject query = new QueryObject();
        List<Book> result = bookService.pesquisa(query).getValues();
        assertEquals(4, result.size());
        query.setInactiveSearch(true);
        List<Book> result2 = bookService.pesquisa(query).getValues();
        int excluidosAntes=result2.size();
        bookService.delete(result);
        query.setInactiveSearch(true);
        result = bookService.pesquisa(query).getValues();
        assertEquals(4+excluidosAntes, result.size());
    }


    @Test
    @Transactional
    public void deletaUm() {
        GumgaThreadScope.organizationCode.set("1.");
        QueryObject query = new QueryObject();
        List<Book> result = bookService.pesquisa(query).getValues();
        assertEquals(4, result.size());
        bookService.delete(result.get(0));
        result = bookService.pesquisa(query).getValues();
        assertEquals(3, result.size());

    }

    @Test
    @Transactional
    public void removePermanente(){
        GumgaThreadScope.organizationCode.set("1.");
        List<Book> result = bookRepository.findAll();
        assertEquals(4, result.size());
        bookService.deletePermanentGumgaLDModel(result.get(0));
        result = bookRepository.findAll();
        assertEquals(3, result.size());
    }

*/

}
