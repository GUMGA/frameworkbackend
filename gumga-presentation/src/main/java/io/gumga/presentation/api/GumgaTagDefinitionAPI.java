package io.gumga.presentation.api;

import io.gumga.application.GumgaService;
import io.gumga.application.tag.GumgaTagDefinitionService;
import io.gumga.domain.tag.GumgaTagDefinition;
import io.gumga.presentation.GumgaAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gumgatagdefinition")
public class GumgaTagDefinitionAPI extends GumgaAPI<GumgaTagDefinition, Long> {

    @Autowired
    public GumgaTagDefinitionAPI(GumgaService<GumgaTagDefinition, Long> service) {
        super(service);
    }

    @Override
    public GumgaTagDefinition load(@PathVariable Long id) {
        return ((GumgaTagDefinitionService) service).loadGumgaTagDefinitionFat(id);
    }

}
