package io.gumga.presentation.api;

import com.wordnik.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * API para acesso aos metadados de uma determinada entidade
 */
@RestController
@RequestMapping("/public/metadata/")
public class MetadataAPI {
    
    private static final Logger log = LoggerFactory.getLogger(MetadataAPI.class);

    /**
     * Busca os metadados da classe informada
     * @param classe String contendo o nome da classe que se deseja obter os metadados
     * @return Objeto GumgaEntityMetadata contendo os metadados {@link GumgaEntityMetadata}
     */
    @ApiOperation(value = "describe", notes = "Retorna o metadata da classe informada.")
    @RequestMapping(value = "describe/{classe}", method = RequestMethod.GET)
    public GumgaEntityMetadata describe(@PathVariable String classe) {
        try {
            Class clazz = Class.forName(classe);
            GumgaEntityMetadata gem = new GumgaEntityMetadata(clazz);
            return gem;
        } catch (ClassNotFoundException ex) {
            log.error("Erro ao obter Metadados.", ex);
        }
        throw new MetadataAPIException("Entity not present");
    }

}
