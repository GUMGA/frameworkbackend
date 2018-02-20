/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.presentation.api;

import io.gumga.domain.GumgaModel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe para obtenção de metadados de uma determinada entidade
 * @author munif
 */
public class GumgaEntityMetadata {

    private String name;
    private String pack;
    private List<GumgaAtributeMetadata> atributes;

    /**
     * Construtor que obtém os dados pertinentes da entidade recebida como parâmetro
     * @param clazz Objeto a serem extraídos os metadados
     */
    public GumgaEntityMetadata(Class clazz) {
        name = clazz.getSimpleName();
        pack = clazz.getCanonicalName().substring(0, clazz.getCanonicalName().lastIndexOf('.'));
        atributes = new ArrayList<>();
        List<Class> classes = new ArrayList<>();
        while (!(clazz.equals(Object.class) || clazz.equals(GumgaModel.class))) {
            classes.add(0, clazz);
            clazz = clazz.getSuperclass();
        }
        for (Class c : classes) {
            for (Field f : c.getDeclaredFields()) {
                GumgaAtributeMetadata gam = new GumgaAtributeMetadata(f.getName(), f.getType().getSimpleName());
                atributes.add(gam);
            }
        }
    }

    /**
     * @return Lista de atributos em metadados da entidade construída
     */
    public List<GumgaAtributeMetadata> getAtributes() {
        return atributes;
    }

    /**
     * @return String com o nome da entidade construída
     */
    public String getName() {
        return name;
    }

    /**
     * @return String com o pacote da entidade construída
     */
    public String getPack() {
        return pack;
    }

}
