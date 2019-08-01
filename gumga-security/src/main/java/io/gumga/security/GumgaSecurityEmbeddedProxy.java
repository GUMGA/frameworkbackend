package io.gumga.security;

import io.gumga.core.GumgaThreadScope;
import io.gumga.core.GumgaValues;
import io.gumga.domain.integration.IntegrationEspecificationDTO;
import io.gumga.presentation.CustomGumgaRestTemplate;
import io.gumga.presentation.api.GumgaJsonRestTemplate;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

/**
 * Classe que contém métodos de integração do segurança
 * Created by mateus on 07/02/17.
 */
@RestController
@RequestMapping("/api/security-embedded")
public class GumgaSecurityEmbeddedProxy {

    private RestTemplate restTemplate;

    @Autowired
    private GumgaValues gumgaValues;
    @Autowired(required = false)
    private CustomGumgaRestTemplate gumgaRestTemplate;

    @PostConstruct
    public void initRestTemplate(){
        restTemplate = new GumgaJsonRestTemplate();
        restTemplate = gumgaRestTemplate != null ? gumgaRestTemplate.getRestTemplate(restTemplate) : restTemplate;
    }

    /**
     * @return Url do segurança
     */
    public String getSecurityUrl() {
        return gumgaValues.getGumgaSecurityUrl().replace("publicoperations", "");
    }

    /**
     * Lista os usuários de uma organização
     * @param id Id da organização
     * @return Lista de usuários
     */
    @Transactional
    @ApiOperation(value = "UsersByOrganization", notes = "Lista os usuários de uma organização")
    @RequestMapping(value = "/organization/{organizationId}/users", method = RequestMethod.GET)
    public List<Map> getUsersByOrganization(@PathVariable("organizationId") Long id) {
        try {
            String url = getSecurityUrl() + "/publicoperations/token/organization/" + id + "/users";
            List<Map> result = this.restTemplate.getForObject(url, List.class);
            return result;
        } catch (RestClientException restClientException) {
            throw new ProxyProblemResponse("Problema na comunicação com o segurança.", restClientException.getMessage()).exception();
        }
    }

    /**
     * Busca a organização pelo id informado
     * @param oi id da organização
     * @param token Token
     * @return Organização
     */
    @Transactional
    @ApiOperation(value = "getOrganization", notes = "Busca a organização pelo id informado")
    @RequestMapping(value = "/organization/{organizationId:.+}", method = RequestMethod.GET)
    public Map getOrganization(@PathVariable("organizationId") String oi, @RequestHeader("gumgaToken") String token) {
        try {
            String url = getSecurityUrl() + "/api/organization/fatbyoi/" + oi + "?gumgaToken=" + token;
            Map result = this.restTemplate.getForObject(url, Map.class);
            return result;
        } catch (RestClientException restClientException) {
            throw new ProxyProblemResponse("Problema na comunicação com o segurança.", restClientException.getMessage()).exception();
        }
    }

    /**
     * Salva o usuário no segurança
     * @param user Usuário
     * @param token Token
     * @return Usuário
     */
    @Transactional
    @ApiOperation(value = "saveUser", notes = "Salva o usuário no segurança")
    @RequestMapping(value = "/save-user", method = RequestMethod.POST)
    public Map saveUser(@RequestBody Map user, @RequestHeader("gumgaToken") String token) {
        try {
            String url = getSecurityUrl() + "/api/user?gumgaToken=" + token;
            return restTemplate.postForObject(url, user, Map.class);
        } catch (RestClientException restClientException) {
            throw new ProxyProblemResponse("Problema na comunicação com o segurança.", restClientException.getMessage()).exception();
        }
    }

    /**
     * Salva o perfil no segurança
     * @param roleAndList Perfil
     * @param token Token
     * @return Perfil
     */
    @Transactional
    @ApiOperation(value = "saveRole", notes = "Salva o perfil no segurança")
    @RequestMapping(value = "/save-role", method = RequestMethod.POST)
    public Map saveRole(@RequestBody Map roleAndList, @RequestHeader("gumgaToken") String token) {
        try {
            String url = getSecurityUrl() + "/api/role/saveall?gumgaToken=" + token;
            return restTemplate.postForObject(url, roleAndList, Map.class);
        } catch (RestClientException restClientException) {
            throw new ProxyProblemResponse("Problema na comunicação com o segurança.", restClientException.getMessage()).exception();
        }
    }

    /**
     * Salva a organização no segurança
     * @param organization Organização
     * @param token Token
     * @return Organização
     */
    @Transactional
    @ApiOperation(value = "saveOrganization", notes = "Salva a organização no segurança")
    @RequestMapping(value = "/save-organization", method = RequestMethod.POST)
    public Map saveOrganization(@RequestBody Map organization, @RequestHeader("gumgaToken") String token) {
        try {
            String url = getSecurityUrl() + "/api/organization?gumgaToken=" + token;
            return restTemplate.postForObject(url, organization, Map.class);
        } catch (RestClientException restClientException) {
            throw new ProxyProblemResponse("Problema na comunicação com o segurança.", restClientException.getMessage()).exception();
        }
    }

    /**
     * Instância atual
     * @return Instância
     */
    @RequestMapping(method = RequestMethod.GET, path = "/get-instance")
    public ResponseEntity<Map> getInstance() {
        try {
            final HttpHeaders headers = new HttpHeaders();
            headers.set("gumgaToken", GumgaThreadScope.gumgaToken.get());
            final String url = getSecurityUrl() + "/api/gumga-security/get-instance";
            final Map result = this.restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>(headers), Map.class).getBody();
            return ResponseEntity.ok(result);
        } catch (RestClientException restClientException) {
            throw new ProxyProblemResponse("Problema na comunicação com o segurança.", restClientException.getMessage()).exception();
        }
    }

    /**
     * Nova integração para o usuário
     * @param dto Objeto de Integração {@link IntegrationEspecificationDTO}
     * @return Status da integração
     */
    @RequestMapping(method = RequestMethod.POST, path = "/newintegrationuser")
    @Transactional
    public String newIntegrationUser(@RequestBody IntegrationEspecificationDTO dto) {
        final String url = getSecurityUrl() + "/api/integration/new";
        final HttpHeaders headers = new HttpHeaders();
        headers.set("gumgaToken", GumgaThreadScope.gumgaToken.get());
        String resp = this.restTemplate.exchange(url,HttpMethod.POST, new HttpEntity(dto, headers), String.class).getBody();
        return resp;
    }
}


