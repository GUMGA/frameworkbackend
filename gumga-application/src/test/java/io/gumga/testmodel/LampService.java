package io.gumga.testmodel;

import io.gumga.application.GumgaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LampService extends GumgaService<Lamp, Long> {

    @Autowired
    public LampService(LampRepository repository) {
        super(repository);
    }
}
