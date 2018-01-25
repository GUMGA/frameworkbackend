package io.gumga.application.customfields;

import io.gumga.application.GumgaService;
import io.gumga.core.QueryObject;
import io.gumga.domain.customfields.GumgaCustomField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Classe para manipulação de campos customizados
 */
@Service
public class GumgaCustomFieldService extends GumgaService<GumgaCustomField, Long> {

    private final GumgaCustomFieldRepository repository;

    @Autowired
    public GumgaCustomFieldService(GumgaCustomFieldRepository repository) {
        super(repository);
        this.repository = repository;
    }

    /**
     * Retonar a lista de atributos genericos da classe passada por parametro
     * @param clazzName Nome da classe
     * @return Lista de campos customizados
     */
    List<GumgaCustomField> findByClass(String clazzName) {
        return repository.search(getQueryObject(clazzName)).getValues();

    }

    /**
     * Retonar a lista de atributos genericos da classe passada por parametro
     * @param c Classe
     * @return Lista de campos customizados
     */
    List<GumgaCustomField> findByClass(Class c) {
        return findByClass(c.getName());
    }

    /**
     * Retonar a lista de atributos genericos da classe passada por parametro
     * @param obj Instância da classe
     * @return Lista de campos customizados
     */
    List<GumgaCustomField> findByClass(Object obj) {
        return findByClass(obj.getClass());
    }

    /**
     * Objeto de Pesquisa dos campos customizados de determinada classe
     * @param clazzName Nome da Classe
     * @return Objeto de pesquisa
     */
    private QueryObject getQueryObject(String clazzName) {
        QueryObject qo = new QueryObject();
        qo.setAq("obj.clazz='" + clazzName + "'");
        qo.setSortField("visualizationOrder");
        return qo;

    }

}
