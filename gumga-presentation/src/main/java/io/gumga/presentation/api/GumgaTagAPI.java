package io.gumga.presentation.api;

import io.gumga.application.GumgaService;
import io.gumga.application.tag.GumgaTagService;
import io.gumga.domain.tag.GumgaTag;
import io.gumga.presentation.GumgaAPI;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API para acesso e manipulação de tags de objetos
 */
@RestController
@RequestMapping("/api/gumgatag")
public class GumgaTagAPI extends GumgaAPI<GumgaTag, Long> {

    @Autowired
    public GumgaTagAPI(GumgaService<GumgaTag, Long> service) {
        super(service);
    }


//    @Override
//    public GumgaTag load(@PathVariable Long id) {
//        return getService().loadGumgaTagFat(id);
//    }

    /**
     * Busca as tags pelo tipo e id do objeto associado
     * @param objectType String com o tipo do objeto
     * @param objectId Id do objeto a ser buscado
     * @return Lista contendo as tags associadas ao objeto recebido
     */
    @ApiOperation(value = "find", notes = "Retorna as tags pelo tipo e id do objeto associado")
    @RequestMapping(value = "find/{objectType}/{objectId}")
    public List<GumgaTag> find(@PathVariable("objectType") String objectType,
            @PathVariable("objectId") Long objectId){
        return getService().findByObjectTypeAndObjectId(objectType, objectId);
    }

    /**
     * Salva uma lista de tags de uma só vez
     * @param tags Lista de tags a ser salva
     */
    @Transactional
    @ApiOperation(value = "saveall", notes = "salva varias tags ao mesmo tempo")
    @RequestMapping(value = "saveall",method = RequestMethod.POST)
    public void saveAll(@RequestBody List<GumgaTag> tags){
        tags.stream().forEach(t -> getService().save(t));
    }


    private GumgaTagService getService(){
        return (GumgaTagService) service;
    }
}
