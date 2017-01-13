package io.gumga.presentation.api;

import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/public/metadata/")
public class MetadataAPI {

    @ApiOperation(value = "describe", notes = "Retorna o metadata da classe informada.")
    @RequestMapping(value = "describe/{classe}", method = RequestMethod.GET)
    public GumgaEntityMetadata describe(@PathVariable String classe) {
        try {
            Class clazz = Class.forName(classe);
            GumgaEntityMetadata gem = new GumgaEntityMetadata(clazz);
            return gem;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MetadataAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new MetadataAPIException("Entity not present");
    }

}
