package io.gumga.application;

import io.gumga.core.GumgaThreadScope;
import io.gumga.core.QueryObject;
import io.gumga.core.SearchResult;
import io.gumga.testmodel.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfig.class})
public class DeleteTest {

    @Autowired
    private CompanyService companyService;
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private BookService bookService;
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MyCarService myCarService;

    public DeleteTest() {
        GumgaThreadScope.organizationCode.set("1.");
        GumgaThreadScope.login.set("gumga@gumga.com.br");
    }

    @Test
    public void deleteSimple() {
        companyRepository.deleteAll();
        Company empresa1 = new Company("GUMGA");
        Company empresa2 = new Company("MUNIF");
        Company empresa3 = new Company("GEBARA");
        Company empresa4 = new Company("JUNIOR");
        companyService.save(empresa1);
        companyService.save(empresa2);
        companyService.save(empresa3);
        companyService.save(empresa4);
        QueryObject query = new QueryObject();
        SearchResult<Company> pesquisa = companyService.pesquisa(query);
        assertEquals(4l, pesquisa.getCount().longValue());
        companyService.delete(empresa4);
        pesquisa = companyService.pesquisa(query);
        assertEquals(3l, pesquisa.getCount().longValue());
    }

    @Test
    @Transactional
    public void deleteMultiple() {
        bookRepository.deleteAll();
        Book livro1 = new Book("The");
        Book livro2 = new Book("is");
        Book livro3 = new Book("ont");
        Book livro4 = new Book("the");
        Book livro5 = new Book("table");
        bookService.save(livro1);
        bookService.save(livro2);
        bookService.save(livro3);
        bookService.save(livro4);
        bookService.save(livro5);

        QueryObject query = new QueryObject();
        SearchResult<Book> pesquisa = bookService.pesquisa(query);
        assertEquals(5l, pesquisa.getCount().longValue());
        bookService.delete(Arrays.asList(new Book[]{livro1, livro2, livro3}));
        pesquisa = bookService.pesquisa(query);
        assertEquals(2l, pesquisa.getCount().longValue());
        query.setInactiveSearch(true);
        pesquisa = bookService.pesquisa(query);
        assertEquals(3l, pesquisa.getCount().longValue());

    }

    @Test
    public void deleteFromOtherOi() {
        Assertions.assertThrows(JpaObjectRetrievalFailureException.class, () -> {
            Book livro99 = new Book("TheThe");
            bookService.save(livro99);
            GumgaThreadScope.organizationCode.set("99.");
            bookService.delete(livro99);
        });
    }

    @Test
    public void deleteMultipleFromOtherOi() {
        Assertions.assertThrows(JpaObjectRetrievalFailureException.class, () -> {
            MyCar carro1 = new MyCar("A001", "red");
            MyCar carro2 = new MyCar("A002", "blue");
            GumgaThreadScope.organizationCode.set("99.");
            MyCar carro3 = new MyCar("A003", "green");
            MyCar carro4 = new MyCar("A004", "white");
            myCarService.save(carro1);
            myCarService.save(carro2);
            myCarService.save(carro3);
            myCarService.save(carro4);
            myCarService.delete(Arrays.asList(new MyCar[]{carro1, carro2, carro3, carro4}));
        });


    }

    @Test
    @Transactional
    public void queryEnum() {
        bookRepository.deleteAll();
        Book livro1 = new Book("The", BookType.EXERCISE);
        Book livro2 = new Book("is", BookType.TEXT);
        Book livro3 = new Book("ont", BookType.EXERCISE);
        Book livro4 = new Book("the", BookType.TEXT);
        Book livro5 = new Book("table", BookType.EXERCISE);
        bookService.save(livro1);
        bookService.save(livro2);
        bookService.save(livro3);
        bookService.save(livro4);
        bookService.save(livro5);

        QueryObject query = new QueryObject();
        query.setQ("EXERCISE");
        query.setSearchFields("ttype");

        SearchResult<Book> pesquisa = bookService.pesquisa(query);
        assertEquals(3l, pesquisa.getCount().longValue());

    }

}
