package io.gumga.presentation.api;

import io.gumga.application.GumgaService;
import io.gumga.application.tag.GumgaTagDefinitionService;
import io.gumga.domain.tag.GumgaTagDefinition;
import io.gumga.presentation.GumgaAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Classe para acesso das definições de Tag
 */
@RestController
@RequestMapping("/api/gumgatagdefinition")
public class GumgaTagDefinitionAPI extends GumgaAPI<GumgaTagDefinition, Long> {
    /**
     * Construtor com a injeção do módulo Service para acesso aos serviços do Framework
     * @param service Objeto Service a ser injetado
     */
    @Autowired
    public GumgaTagDefinitionAPI(GumgaService<GumgaTagDefinition, Long> service) {
        super(service);
    }

    /**
     * Busca definição de tag de acordo com id
     * @param id id da entidade a ser buscada
     * @return Definição do tag
     */
    @Override
    public GumgaTagDefinition load(@PathVariable Long id) {
        return ((GumgaTagDefinitionService) service).loadGumgaTagDefinitionFat(id);
    }
}
