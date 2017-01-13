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

public abstract class AbstractGumgaService<T, ID extends Serializable> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected final GumgaCrudRepository<T, ID> repository;

    @Autowired
    protected GumgaCustomEnhancerService gces;

    public AbstractGumgaService(GumgaCrudRepository<T, ID> repository) {
        this.repository = repository;
    }

    @SuppressWarnings("unchecked")
    public Class<T> clazz() {
        return (Class<T>) ReflectionUtils.inferGenericType(getClass());
    }

    public void loadGumgaCustomFields(Object entity) {
        if (entity instanceof GumgaCustomizableModel) {
            gces.loadCustomFields((GumgaCustomizableModel) entity);
        }
    }

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
