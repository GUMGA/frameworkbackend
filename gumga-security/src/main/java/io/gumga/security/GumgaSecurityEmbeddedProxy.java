package io.gumga.security;

import com.wordnik.swagger.annotations.ApiOperation;
import io.gumga.core.GumgaValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

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
        restTemplate = new RestTemplate();
    }

    public String getSecurityUrl(){
        return gumgaValues.getGumgaSecurityUrl().replace("publicoperations", "");
    }

    @Transactional
    @ApiOperation(value = "UsersByOrganization", notes = "Lista os usuários de uma organização")
    @RequestMapping(value = "/organization/{organizationId}/users", method = RequestMethod.GET)
    public List<Map> getUsersByOrganization(@PathVariable("organizationId") Long id){
        String url = getSecurityUrl() + "/publicoperations/token/organization/" + id + "/users";
        List<Map> result = this.restTemplate.getForObject(url, List.class);
        return result;
    }

    @Transactional
    @ApiOperation(value = "getOrganization", notes = "Busca a organização pelo id informado")
    @RequestMapping(value = "/organization/{organizationId}", method = RequestMethod.GET)
    public Map getOrganization(@PathVariable("organizationId") Long id, @RequestHeader("gumgaToken") String token){
        String url =  getSecurityUrl () + "/api/organization/" + id + "?gumgaToken="+token;
        return this.restTemplate.getForObject(url, Map.class);
    }

    @Transactional
    @ApiOperation(value = "saveUser", notes = "Salva o usuário no segurança")
    @RequestMapping(value = "/save-user", method = RequestMethod.POST)
    public Map saveUser(@RequestBody Map user, @RequestHeader("gumgaToken") String token){
        String url =  getSecurityUrl () + "/api/user?gumgaToken="+token;
        return restTemplate.postForObject(url, user, Map.class);
    }

    @Transactional
    @ApiOperation(value = "saveOrganization", notes = "Salva a organização no segurança")
    @RequestMapping(value = "/save-organization", method = RequestMethod.POST)
    public Map saveOrganization(@RequestBody Map organization, @RequestHeader("gumgaToken") String token){
        String url =  getSecurityUrl () + "/api/organization?gumgaToken="+token;
        return restTemplate.postForObject(url, organization, Map.class);
    }

}
