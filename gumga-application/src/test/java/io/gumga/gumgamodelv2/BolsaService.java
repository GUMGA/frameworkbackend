package io.gumga.gumgamodelv2;

import io.gumga.application.GumgaService;
import io.gumga.domain.GumgaObjectAndRevision;
import io.gumga.domain.repository.GumgaCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class BolsaService extends GumgaService<Bolsa, String> {

    @Autowired
    public BolsaService(GumgaCrudRepository<Bolsa, String> repository) {
        super(repository);
    }

}
