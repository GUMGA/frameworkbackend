package io.gumga.security;

import io.gumga.presentation.GumgaAPI;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityHelper {
    
    private static final Logger log = LoggerFactory.getLogger(SecurityHelper.class);

    public static Set<GumgaOperationTO> listMyOperations(String pacote) {
        Set<GumgaOperationTO> toReturn = new HashSet<>();
        Reflections reflections = new Reflections(pacote);
        log.warn("ReflectionsConfiguration------->" + reflections.getConfiguration().getUrls());
        Set<Class<? extends GumgaAPI>> classOfInterest = reflections.getSubTypesOf(GumgaAPI.class);
        for (Class classe : classOfInterest) {
            for (Method metodo : classe.getDeclaredMethods()) {
                if (metodo.isAnnotationPresent(GumgaOperationKey.class)) {
                    GumgaOperationKey gok = metodo.getAnnotation(GumgaOperationKey.class);
                    toReturn.add(new GumgaOperationTO(gok));
                }
            }
        }
        return toReturn;
    }

}
