package io.gumga.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BusService extends GumgaService<Bus, Long> {

    @Autowired
    public BusService(BusRepository repository) {
        super(repository);
    }

    @Override
    public void delete(Bus resource) {
        super.delete(resource); //To change body of generated methods, choose Tools | Templates.
    }
    
    

}
