/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.presentation.api;

import io.gumga.presentation.CustomGumgaRestTemplate;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * API para implementação de rotas públicas de serviços Gumga
 */
@RestController
@RequestMapping("/public/")
public class GumgaThirdPartProxy {

    private RestTemplate restTemplate;
    @Autowired(required = false)
    private CustomGumgaRestTemplate gumgaRestTemplate;

    @PostConstruct
    public void initRestTemplate() {
        restTemplate = new GumgaJsonRestTemplate();
        restTemplate = gumgaRestTemplate != null ? gumgaRestTemplate.getRestTemplate(restTemplate) : restTemplate;
    }

    /**
     * Pesquisa informações do Cep informado.
     * Implementa uma rota para busca de dados postais em uma API externa
     * @param cep String contendo o Cep a ser buscado
     * @return Uma coleção (Map) contendo a resposta do servidor externo
     */
    @ApiOperation(value = "searchCep",notes = "Pesquisa informações do cep informado.")
    @RequestMapping(value = "cep/{cep}",method = RequestMethod.GET)
    public Map buscaCep(@PathVariable String cep) {
        String url = "http://gumga.com.br/services-api/public/busca-cep/"+cep;
        Map resposta = restTemplate.getForObject(url, Map.class);
        return resposta;
    }

}
