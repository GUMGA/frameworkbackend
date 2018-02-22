package io.gumga.testmodel;

import io.gumga.application.GumgaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService extends GumgaService<Book, Long> {

    @Autowired
    public BookService(BookRepository repository) {
        super(repository);
    }



}
