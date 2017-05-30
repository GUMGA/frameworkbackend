package io.gumga.security;

import com.wordnik.swagger.annotations.ApiOperation;
import io.gumga.core.GumgaThreadScope;
import io.gumga.core.GumgaValues;
import io.gumga.presentation.api.GumgaJsonRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import org.springframework.web.client.RestClientException;

/**
 * Created by mateus on 07/02/17.
 */
@RestController
@RequestMapping("/api/security-embedded")
public class GumgaSecurityEmbeddedProxy {

    private final RestTemplate restTemplate;
    @Autowired
    private GumgaValues gumgaValues;

    public GumgaSecurityEmbeddedProxy() {
        restTemplate = new GumgaJsonRestTemplate();
    }

    public String getSecurityUrl() {
        return gumgaValues.getGumgaSecurityUrl().replace("publicoperations", "");
    }

    @Transactional
    @ApiOperation(value = "UsersByOrganization", notes = "Lista os usuários de uma organização")
    @RequestMapping(value = "/organization/{organizationId}/users", method = RequestMethod.GET)
    public List<Map> getUsersByOrganization(@PathVariable("organizationId") Long id) {
        try {
            String url = getSecurityUrl() + "/publicoperations/token/organization/" + id + "/users";
            List<Map> result = this.restTemplate.getForObject(url, List.class);
            return result;
        } catch (RestClientException restClientException) {
            throw new ProxyProblemResponse("Problema na comunicação com o sergurança.", restClientException.getMessage()).exception();
        }
    }

    @Transactional
    @ApiOperation(value = "getOrganization", notes = "Busca a organização pelo id informado")
    @RequestMapping(value = "/organization/{organizationId:.+}", method = RequestMethod.GET)
    public Map getOrganization(@PathVariable("organizationId") String oi, @RequestHeader("gumgaToken") String token) {
        try {
            String url = getSecurityUrl() + "/api/organization/fatbyoi/" + oi + "?gumgaToken=" + token;
            Map result = this.restTemplate.getForObject(url, Map.class);
            return result;
        } catch (RestClientException restClientException) {
            throw new ProxyProblemResponse("Problema na comunicação com o sergurança.", restClientException.getMessage()).exception();
        }
    }

    @Transactional
    @ApiOperation(value = "saveUser", notes = "Salva o usuário no segurança")
    @RequestMapping(value = "/save-user", method = RequestMethod.POST)
    public Map saveUser(@RequestBody Map user, @RequestHeader("gumgaToken") String token) {
        try {
            String url = getSecurityUrl() + "/api/user?gumgaToken=" + token;
            return restTemplate.postForObject(url, user, Map.class);
        } catch (RestClientException restClientException) {
            throw new ProxyProblemResponse("Problema na comunicação com o sergurança.", restClientException.getMessage()).exception();
        }
    }

    @Transactional
    @ApiOperation(value = "saveRole", notes = "Salva o perfil no segurança")
    @RequestMapping(value = "/save-role", method = RequestMethod.POST)
    public Map saveRole(@RequestBody Map roleAndList, @RequestHeader("gumgaToken") String token) {
        try {
            String url = getSecurityUrl() + "/api/role/saveall?gumgaToken=" + token;
            return restTemplate.postForObject(url, roleAndList, Map.class);
        } catch (RestClientException restClientException) {
            throw new ProxyProblemResponse("Problema na comunicação com o sergurança.", restClientException.getMessage()).exception();
        }
    }

    @Transactional
    @ApiOperation(value = "saveOrganization", notes = "Salva a organização no segurança")
    @RequestMapping(value = "/save-organization", method = RequestMethod.POST)
    public Map saveOrganization(@RequestBody Map organization, @RequestHeader("gumgaToken") String token) {
        try {
            String url = getSecurityUrl() + "/api/organization?gumgaToken=" + token;
            return restTemplate.postForObject(url, organization, Map.class);
        } catch (RestClientException restClientException) {
            throw new ProxyProblemResponse("Problema na comunicação com o sergurança.", restClientException.getMessage()).exception();
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "/get-instance")
    public ResponseEntity<Map> getInstance() {
        try {
            final HttpHeaders headers = new HttpHeaders();
            headers.set("gumgaToken", GumgaThreadScope.gumgaToken.get());
            final String url = getSecurityUrl() + "/api/gumga-security/get-instance";
            final Map result = this.restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>(headers), Map.class).getBody();
            return ResponseEntity.ok(result);
        } catch (RestClientException restClientException) {
            throw new ProxyProblemResponse("Problema na comunicação com o sergurança.", restClientException.getMessage()).exception();
        }
    }

}
