package io.gumga.application;

import io.gumga.domain.GumgaMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class GumgaMessageService extends GumgaService<GumgaMessage, Long> implements Serializable {

    private GumgaMessageRepository repository;

    @Autowired
    public GumgaMessageService(GumgaMessageRepository repository) {
        super(repository);
        this.repository = repository;
    }

}
