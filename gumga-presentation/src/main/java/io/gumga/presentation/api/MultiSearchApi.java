/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.presentation.api;

import io.gumga.application.GumgaUntypedRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * API para acesso e busca de atributos diversos
 * @author munif
 */
@RestController
@RequestMapping("/api/multisearch/")
public class MultiSearchApi {

    @Autowired
    private GumgaUntypedRepository gur;

    /**
     * Injeta o módulo de busca para objetos não tipados
     * @param gur Objeto GumgaUntypedRepository {@link GumgaUntypedRepository}
     */
    public void setGur(GumgaUntypedRepository gur) {
        this.gur = gur;
    }

    /**
     * Faz uma busca nos atributos anotados
     * @param text String com o conteúdo a ser pesquisado
     * @return Lista com os resultados de atributos compatíveis ao texto de entrada
     */
    @Transactional
    @ApiOperation(value = "search", notes = "Faz uma pesquisa múltipla com o texto informado.")
    @RequestMapping(value="search/{text}",method = RequestMethod.GET)
    public List<GumgaGenericResult> search(@PathVariable String text) {
        List<GumgaGenericResult> aRetornar = new ArrayList<>();
        List<Object> resultado = gur.fullTextSearch(text);
        for (Object obj : resultado) {
            aRetornar.add(new GumgaGenericResult(obj));
        }
        return aRetornar;
    }

}
