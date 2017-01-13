/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.presentation.api;

import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/public/")
public class GumgaThirdPartProxy {

    private final RestTemplate restTemplate;

    public GumgaThirdPartProxy() {
        restTemplate = new RestTemplate();
    }

    @ApiOperation(value = "searchCep",notes = "Pesquisa informações do cep informado.")
    @RequestMapping(value = "cep/{cep}",method = RequestMethod.GET)
    public Map buscaCep(@PathVariable String cep) {
        String url = "http://gumga.com.br/services-api/public/busca-cep/"+cep;
        Map resposta = restTemplate.getForObject(url, Map.class);
        return resposta;
    }

}
