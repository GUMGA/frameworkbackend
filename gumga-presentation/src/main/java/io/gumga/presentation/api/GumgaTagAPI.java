package io.gumga.presentation.api;

import com.wordnik.swagger.annotations.ApiOperation;
import io.gumga.application.GumgaService;
import io.gumga.application.tag.GumgaTagService;
import io.gumga.domain.tag.GumgaTag;
import io.gumga.presentation.GumgaAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gumgatag")
public class GumgaTagAPI extends GumgaAPI<GumgaTag, Long> {

    @Autowired
    public GumgaTagAPI(GumgaService<GumgaTag, Long> service) {
        super(service);
    }

    @Override
    public GumgaTag load(@PathVariable Long id) {
        return getService().loadGumgaTagFat(id);
    }

    @ApiOperation(value = "find", notes = "Retorna as tags pelo tipo e id do objeto associado")
    @RequestMapping(value = "find/{objectType}/{objectId}")
    public List<GumgaTag> find(@PathVariable("objectType") String objectType,
            @PathVariable("objectId") Long objectId){
        return getService().findByObjectTypeAndObjectId(objectType, objectId);
    }
    
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
