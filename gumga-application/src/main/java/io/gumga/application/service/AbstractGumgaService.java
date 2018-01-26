package io.gumga.application.service;

import io.gumga.application.customfields.GumgaCustomEnhancerService;
import io.gumga.core.exception.NoMultiTenancyException;
import io.gumga.core.utils.ReflectionUtils;
import io.gumga.domain.GumgaMultitenancy;
import io.gumga.domain.customfields.GumgaCustomizableModel;
import io.gumga.domain.domains.GumgaOi;
import io.gumga.domain.repository.GumgaCrudRepository;
import io.gumga.domain.repository.GumgaMultitenancyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

/**
 * Classe abstrata que contém métodos para criação de serviços para manipulação da classe domínio, id da organização e busca de campos customizados
 * @param <T> Classe que contenha um identificador padrão, exemplo: ID do registro
 * @param <ID> Tipo do identificador contido na classe
 */
public abstract class AbstractGumgaService<T, ID extends Serializable> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected final GumgaCrudRepository<T, ID> repository;

    @Autowired
    protected GumgaCustomEnhancerService gces;

    public AbstractGumgaService(GumgaCrudRepository<T, ID> repository) {
        this.repository = repository;
    }

    /**
     * Objeto com a classe domínio do service atual, exemplo: UsuarioService extends AbstractGumgaService<Usuario, Long>
     * @return Classe domínio
     */
    @SuppressWarnings("unchecked")
    public Class<T> clazz() {
        return (Class<T>) ReflectionUtils.inferGenericType(getClass());
    }

    /**
     * Carrega atributos customizados da entidade recebida por parâmetro, a mesma deve extender a entidade GumgaCustomizableModel
     * @param entity Entidade Customizavel
     */
    public void loadGumgaCustomFields(Object entity) {
        if (entity instanceof GumgaCustomizableModel) {
            gces.loadCustomFields(entity);
        }
    }

    /**
     * Carrega um objeto do tipo GumgaOi com o id da organização de acordo com a política de Multitenancy do usuário atual
     * @return GumgaOi
     */
    public GumgaOi gumgaOiForSearch() {
        try {
            String oiPattern = GumgaMultitenancyUtil.getMultitenancyPattern(clazz().getAnnotation(GumgaMultitenancy.class));
            GumgaOi oi = new GumgaOi(oiPattern);
            return oi;
        }
        catch(Exception ex){
            throw new NoMultiTenancyException("The class "+clazz().getCanonicalName()+" haven't MultiTenancy" );
        }
    }

    /**
     * Carrega um objeto do tipo GumgaOi com o id da organização com o caractere porcentagem concatenado (%), de acordo com a política de Multitenancy do usuário atual
     * @return GumgaOi preparado pra busca no hql, exemplo: select * from Usuario where oi like '1.2.%
     */
    public GumgaOi gumgaOiForSearchWithWildCard() {
        try {
            String oiPattern = GumgaMultitenancyUtil.getMultitenancyPattern(clazz().getAnnotation(GumgaMultitenancy.class));
            GumgaOi oi = new GumgaOi(oiPattern.concat("%"));
            return oi;
        }
        catch(Exception ex){
            throw new NoMultiTenancyException("The class "+clazz().getCanonicalName()+" haven't MultiTenancy" );
        }
    }

}
