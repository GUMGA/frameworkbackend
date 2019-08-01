package io.gumga.presentation.api;

import io.gumga.application.GumgaService;
import io.gumga.application.GumgaUserDataService;
import io.gumga.core.SearchResult;
import io.gumga.domain.GumgaUserData;
import io.gumga.presentation.GumgaAPI;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * API para acesso dos dados adicionais do Usuário
 */
@RestController
@RequestMapping("/api/gumgauserdata")
public class GumgaUserDataApi extends GumgaAPI<GumgaUserData, Long> {
    /**
     * Construtor com a injeção do módulo Service para acesso aos serviços do Framework
     * @param service Objeto Service a ser injetado
     */
    @Autowired
    public GumgaUserDataApi(GumgaService<GumgaUserData, Long> service) {
        super(service);
    }

    /**
     * Busca os dados de um determinado usuário a partir do atributo Key
     * @param prefix Chave (key) do usuário a ser buscado
     * @return um objeto SearchResult contendo os GumgaUserData do usuário buscado
     * {@link SearchResult}
     * {@link GumgaUserData}
     */
    @ApiOperation(value = "queryByKeyPrefix", notes = "Retorna os associados do usuário a uma chave.")
    @RequestMapping(value = "keyprefix/{prefix}", method = RequestMethod.GET)
    public SearchResult<GumgaUserData> queryByKeyPrefix(@PathVariable String prefix) {
        return ((GumgaUserDataService) service).searchByKeyPrefix(prefix);

    }

}
