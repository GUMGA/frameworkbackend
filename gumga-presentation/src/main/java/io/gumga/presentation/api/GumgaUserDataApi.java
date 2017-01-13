package io.gumga.presentation.api;

import com.wordnik.swagger.annotations.ApiOperation;
import io.gumga.application.GumgaService;
import io.gumga.application.GumgaUserDataService;
import io.gumga.core.SearchResult;
import io.gumga.domain.GumgaUserData;
import io.gumga.presentation.GumgaAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gumgauserdata")
public class GumgaUserDataApi extends GumgaAPI<GumgaUserData, Long> {

    @Autowired

    public GumgaUserDataApi(GumgaService<GumgaUserData, Long> service) {
        super(service);
    }

    @ApiOperation(value = "queryByKeyPrefix", notes = "Retorna os associados do usuário a uma chave.")
    @RequestMapping(value = "keyprefix/{prefix}", method = RequestMethod.GET)
    public SearchResult<GumgaUserData> queryByKeyPrefix(@PathVariable String prefix) {
        return ((GumgaUserDataService) service).searchByKeyPrefix(prefix);

    }

}
