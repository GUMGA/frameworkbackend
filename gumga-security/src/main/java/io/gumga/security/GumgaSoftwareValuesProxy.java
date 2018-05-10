/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.security;

import com.wordnik.swagger.annotations.ApiOperation;
import io.gumga.core.GumgaThreadScope;
import io.gumga.core.GumgaValues;
import io.gumga.presentation.CustomGumgaRestTemplate;
import io.gumga.presentation.RestResponse;
import io.gumga.presentation.api.GumgaJsonRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;
import org.springframework.web.client.RestClientException;

/**
 *
 * @author munif
 */
@RestController
@RequestMapping("/api/proxy/softwarevalues")
public class GumgaSoftwareValuesProxy {

    @Autowired
    private GumgaValues gumgaValues;
    @Autowired(required = false)
    private CustomGumgaRestTemplate gumgaRestTemplate;

    private String getBaseUrl() {
        return gumgaValues.getGumgaSecurityUrl().replace("/publicoperations", "") + "/api/softwarevalue/";
    }

    private RestTemplate restTemplate;

    private RestTemplate getRestTemplate() {
        if (restTemplate == null) {
            restTemplate = new GumgaJsonRestTemplate();
            restTemplate = gumgaRestTemplate != null ? gumgaRestTemplate.getRestTemplate(restTemplate) : restTemplate;
        }
        return restTemplate;
    }

    @Transactional
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "delete", notes = "Deleta objeto com o id correspondente.")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public RestResponse delete(@PathVariable Long id) {
        String url = getBaseUrl() + id + "?gumgaToken=" + GumgaThreadScope.gumgaToken.get();
        try {
            RestTemplate restTemplate = null;
            getRestTemplate().delete(url);
            return new RestResponse("OK");
        } catch (Exception ex) {
            return new RestResponse(ex);
        }
    }

    @Transactional
    @ApiOperation(value = "save", notes = "Salva o objeto correspodente.")
    @RequestMapping(method = RequestMethod.POST)
    public RestResponse save(@RequestBody Map model) {
        try {
            String url = getBaseUrl() + "?gumgaToken=" + GumgaThreadScope.gumgaToken.get();
            model.put("softwareName", GumgaThreadScope.softwareName.get());
            Map response = getRestTemplate().postForObject(url, model, Map.class);
            return new RestResponse(response);
        } catch (RestClientException restClientException) {
            throw new ProxyProblemResponse("Problema na comunicação com o segurança.", restClientException.getMessage()).exception();
        }
    }

    @Transactional
    @ApiOperation(value = "load", notes = "Carrega entidade pela chave informada.")
    @RequestMapping(value = "/{key}", method = RequestMethod.GET)
    public Map load(@PathVariable String key) {
        try {
            String software = GumgaThreadScope.softwareName.get();
            String url = getBaseUrl() + "findbysoftwareandkey/" + software + "/" + key + "?gumgaToken=" + GumgaThreadScope.gumgaToken.get();
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
