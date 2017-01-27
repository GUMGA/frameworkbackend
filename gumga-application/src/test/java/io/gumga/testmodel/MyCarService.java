package io.gumga.testmodel;

import io.gumga.application.GumgaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyCarService extends GumgaService<MyCar, Long> {

    @Autowired
    public MyCarService(MyCarRepository repository) {
        super(repository);
    }

}
