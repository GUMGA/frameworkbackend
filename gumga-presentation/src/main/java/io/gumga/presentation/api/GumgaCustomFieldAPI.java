package io.gumga.presentation.api;

import io.gumga.application.GumgaService;
import io.gumga.domain.customfields.GumgaCustomField;
import io.gumga.presentation.GumgaAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Classe para acesso e manipulação de campos customizáveis
 */
@RestController
@RequestMapping("/api/gumgacustomfield")
public class GumgaCustomFieldAPI extends GumgaAPI<GumgaCustomField, Long> {
    /**
     * Construtor com a injeção um objeto GumgaService para acesso de serviços do Framework
     * @param service Objeto Service a ser injetado {@link GumgaService}
     */
    @Autowired
    public GumgaCustomFieldAPI(GumgaService<GumgaCustomField, Long> service) {
        super(service);
    }


}

