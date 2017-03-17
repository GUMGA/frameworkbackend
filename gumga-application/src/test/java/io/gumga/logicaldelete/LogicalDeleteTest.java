package io.gumga.logicaldelete;

import io.gumga.application.SpringConfig;
import io.gumga.core.GumgaThreadScope;
import io.gumga.core.QueryObject;
import io.gumga.mysql.MysqlSpringConfig;
import io.gumga.testmodel.Book;
import io.gumga.testmodel.BookService;
import io.gumga.testmodel.Company;
import io.gumga.testmodel.CompanyService;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringConfig.class})
public class LogicalDeleteTest {

    @Autowired
    protected BookService bookService;

    @Before
    @Transactional
    public void setUp() {
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

    //@Test
    public void zzz() {
        System.out.println("-----> PAUSE <----");
        new Scanner(System.in).next();
    }

}
