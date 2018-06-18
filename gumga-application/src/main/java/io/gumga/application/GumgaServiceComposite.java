package io.gumga.application;

import io.gumga.application.GumgaService;
import io.gumga.core.GumgaIdable;
import io.gumga.domain.repository.GumgaCrudRepository;

import java.io.Serializable;

public abstract class GumgaServiceComposite<T extends GumgaIdable<X>, X extends Serializable> extends GumgaService<T, X> {

    public GumgaServiceComposite(GumgaCrudRepository repository) {
        super(repository);
    }
}
