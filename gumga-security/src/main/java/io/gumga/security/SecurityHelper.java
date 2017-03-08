package io.gumga.security;

import io.gumga.presentation.GumgaAPI;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.reflections.Reflections;

public class SecurityHelper {

    public static Set<GumgaOperationTO> listMyOperations(String pacote) {
        Set<GumgaOperationTO> toReturn = new HashSet<>();
        Reflections reflections = new Reflections(pacote);
        System.out.println("ReflectionsConfiguration------->" + reflections.getConfiguration().getUrls());
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
