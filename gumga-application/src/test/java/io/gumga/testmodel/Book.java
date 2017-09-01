package io.gumga.testmodel;

import io.gumga.domain.GumgaModel;
import io.gumga.domain.GumgaMultitenancy;
import io.gumga.domain.logicaldelete.GumgaLDModel;
import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name = GumgaModel.SEQ_NAME, sequenceName = "SEQ_CAR")
@GumgaMultitenancy
public class Book extends GumgaLDModel<Long> {

    private String title;
    @Column(name = "book_type")
    @Enumerated(EnumType.STRING)
    private BookType ttype;

    public Book() {

    }

    public Book(String title) {
        this.title = title;
    }

    public Book(String title, BookType ttype) {
        this.title = title;
        this.ttype = ttype;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BookType getTtype() {
        return ttype;
    }

    public void setTtype(BookType ttype) {
        this.ttype = ttype;
    }

}
