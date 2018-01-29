/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.security;

import com.wordnik.swagger.annotations.ApiOperation;
import io.gumga.core.GumgaThreadScope;
import io.gumga.core.GumgaValues;
import io.gumga.presentation.api.GumgaJsonRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;
import org.springframework.web.client.RestClientException;

/**
 * Classe para busca de valores adicionais de determinada instância
 * @author munif
 */
@RestController
@RequestMapping("/api/proxy/instancevalues")
public class GumgaInstanceValuesProxy {

    @Autowired
    private GumgaValues gumgaValues;

    private String getBaseUrl() {
        return gumgaValues.getGumgaSecurityUrl();
    }

    private RestTemplate restTemplate;

    private RestTemplate getRestTemplate() {
        if (restTemplate == null) {
            restTemplate = new GumgaJsonRestTemplate();
        }
        return restTemplate;
    }

    /**
     * Busca valores da instância atual
     * @return Mapa com valores da instância
     */
    @Transactional
    @ApiOperation(value = "List", notes = "Carrega entidade pela chave informada.")
    @RequestMapping(method = RequestMethod.GET)
    public Map load() {
        try {
            String software = GumgaThreadScope.softwareName.get();
            String token = GumgaThreadScope.gumgaToken.get();
            String url = getBaseUrl() + "/token/instance-values/" + software + "/" + token;
            
            Map response = getRestTemplate().getForObject(url, Map.class);
            if (response == null) {
                response = Collections.EMPTY_MAP;
            }
            return response;
        } catch (RestClientException restClientException) {
            throw new ProxyProblemResponse("Problema na comunicação com o segurança.", restClientException.getMessage()).exception();
        }
    }

}
