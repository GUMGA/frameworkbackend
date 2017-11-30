/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.security;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.wordnik.swagger.annotations.ApiOperation;
import io.gumga.core.FacebookRegister;
import io.gumga.core.GumgaValues;
import io.gumga.core.UserAndPassword;
import io.gumga.domain.domains.GumgaImage;
import io.gumga.domain.saas.GumgaSaaS;
import io.gumga.presentation.api.GumgaJsonRestTemplate;
import io.gumga.presentation.exceptionhandler.GumgaRunTimeException;
import io.gumga.security_v2.GumgaCacheRequestFilterV2Repository;
import io.gumga.security_v2.GumgaRequestFilterV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;

/**
 *
 * @author munif
 */
@RestController
@RequestMapping("/public/token")
class GumgaSecurityProxy {

    private final RestTemplate restTemplate;
    @Autowired
    private GumgaValues gumgaValues;
    @Autowired
    private GumgaCacheRequestFilterV2Repository requestFilterV2Repository;

    public GumgaSecurityProxy() {
        restTemplate = new GumgaJsonRestTemplate();
    }

    @ApiOperation(value = "create", notes = "Cria token através do usuário e senha informados.")
    @RequestMapping(value = "/create/{user}/{password:.+}", method = RequestMethod.GET)
    public ResponseEntity create(@PathVariable String user, @PathVariable String password) {
        String url = gumgaValues.getGumgaSecurityUrl() + "/token/create/" + user + "/" + password + "/" + gumgaValues.getSoftwareName();
        try {
            Map resposta = restTemplate.getForObject(url, Map.class);
            GumgaSecurityCode response = GumgaSecurityCode.OK; //TODO ESTÁ PARA MANTER COMPATÍVEL COM A VERSÃO ANTERIOR DO SEGURANÇA, 
            if (resposta.containsKey("response")) {
                response = GumgaSecurityCode.valueOf("" + resposta.get("response"));
            }
            return new ResponseEntity(resposta, response.httpStatus);
        } catch (Exception ex) {
            return new ResponseEntity(new ProxyProblemResponse("Problema na comunicação com o segurança.", ex.getMessage()), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @ApiOperation(value = "delete", notes = "Faz logout do usuário fazendo o token informado expirar.")
    @RequestMapping(value = "/{token:.+}", method = RequestMethod.DELETE)
    public Map delete(@PathVariable String token) {
        try {
            String url = gumgaValues.getGumgaSecurityUrl() + "/token/" + token;
            restTemplate.delete(url);
            this.requestFilterV2Repository.remove(token);
            return GumgaSecurityCode.OK.response();
        } catch (RestClientException ex) {
            throw new ProxyProblemResponse("Problema na comunicação com o segurança.", ex.getMessage()).exception();
        }
    }

    @ApiOperation(value = "login", notes = "Faz o login recebendo o objeto UserAndPassword.")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody UserAndPassword login) {
        try {
            String url = gumgaValues.getGumgaSecurityUrl() + "/token";
            login.setSoftwareName(gumgaValues.getSoftwareName());
            Map resposta = restTemplate.postForObject(url, login, Map.class);
            GumgaSecurityCode response = GumgaSecurityCode.OK; //TODO ESTÁ PARA  MANTER COMPATÍVEL COM A VERSÃO ANTERIOR DO SEGURANÇA,
            if (resposta.containsKey("response")) {
                response = GumgaSecurityCode.valueOf("" + resposta.get("response"));
            }
            return new ResponseEntity(resposta, response.httpStatus);
        } catch (RestClientException ex) {
            return new ResponseEntity(new ProxyProblemResponse("Problema na comunicação com o segurança.", ex.getMessage()), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @ApiOperation(value = "login", notes = "Faz o login recebendo o objeto UserAndPassword.")
    @RequestMapping(method = RequestMethod.POST, value = "app")
    public ResponseEntity loginApp(@RequestBody UserAndPassword login) {
        try {
            String url = gumgaValues.getGumgaSecurityUrl() + "/token/app";
            login.setSoftwareName(gumgaValues.getSoftwareName());
            Map resposta = restTemplate.postForObject(url, login, Map.class);
            GumgaSecurityCode response = GumgaSecurityCode.OK; //TODO ESTÁ PARA  MANTER COMPATÍVEL COM A VERSÃO ANTERIOR DO SEGURANÇA,
            if (resposta.containsKey("response")) {
                response = GumgaSecurityCode.valueOf("" + resposta.get("response"));
            }
            return new ResponseEntity(resposta, response.httpStatus);
        } catch (RestClientException restClientException) {
            return new ResponseEntity(new ProxyProblemResponse("Problema na comunicação com o segurança.", restClientException.getMessage()), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @ApiOperation(value = "facebook", notes = "Faz o login com facebook recebendo email e token.")
    @RequestMapping(value = "/facebook", method = RequestMethod.GET)
    public Map loginWithFacebook(@RequestParam("email") String email, @RequestParam("token") String facebookToken) {
        try {
            String url = gumgaValues.getGumgaSecurityUrl() + "/token/facebook?email=" + email + "&token=" + facebookToken + "&softwareName=" + gumgaValues.getSoftwareName();
            Map resposta = restTemplate.getForObject(url, Map.class);
            return resposta;
        } catch (RestClientException restClientException) {
            throw new ProxyProblemResponse("Problema na comunicação com o segurança.", restClientException.getMessage()).exception();
        }
    }

    @ApiOperation(value = "github", notes = "Faz o login com github recebendo email e token.")
    @RequestMapping(value = "/github", method = RequestMethod.GET)
    public Map loginWithGitHub(@RequestParam("email") String email, @RequestParam("token") String gitToken) {
        try {
            String url = gumgaValues.getGumgaSecurityUrl() + "/token/github?email=" + email + "&token=" + gitToken + "&softwareName=" + gumgaValues.getSoftwareName();
            Map resposta = restTemplate.getForObject(url, Map.class);
            return resposta;
        } catch (RestClientException restClientException) {
            throw new ProxyProblemResponse("Problema na comunicação com o segurança.", restClientException.getMessage()).exception();
        }
    }

    @ApiOperation(value = "register-facebook", notes = "Cria usuário e organização com facebook")
    @RequestMapping(value = "/register-facebook", method = RequestMethod.POST)
    public Map loginWithFacebook(@RequestBody FacebookRegister facebookRegister) {
        try {
            String url = gumgaValues.getGumgaSecurityUrl() + "/token/register-facebook";
            Map resposta = restTemplate.postForObject(url, facebookRegister, Map.class);
            return resposta;
        } catch (RestClientException restClientException) {
            throw new ProxyProblemResponse("Problema na comunicação com o segurança.", restClientException.getMessage()).exception();
        }
    }

    @RequestMapping(value = "/{token:.+}", method = RequestMethod.GET)
    public Map get(@PathVariable String token) {
        try {
            String url = gumgaValues.getGumgaSecurityUrl() + "/token/get/" + token;
            Map resposta = restTemplate.getForObject(url, Map.class);
            return resposta;
        } catch (RestClientException restClientException) {
            throw new ProxyProblemResponse("Problema na comunicação com o segurança.", restClientException.getMessage()).exception();
        }
    }

    @ApiOperation(value = "changePassword", notes = "Altera a senha do usuário informados pelo objeto UserAndPassword.")
    @RequestMapping(method = RequestMethod.PUT)
    public Map changePassword(@RequestBody UserAndPassword login) {
        try {
            String url = gumgaValues.getGumgaSecurityUrl() + "/token";
            ResponseEntity<Map> exchange = restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(login), Map.class);
            return exchange.getBody();
        } catch (RestClientException restClientException) {
            throw new ProxyProblemResponse("Problema na comunicação com o segurança.", restClientException.getMessage()).exception();
        }
    }

    @Transactional
    @ApiOperation(value = "organizations", notes = "Lista as organizações associadas ao token informado.")
    @RequestMapping(value = "/organizations/{token:.+}", method = RequestMethod.GET)
    public List organizations(@PathVariable String token) {
        try {
            String url = gumgaValues.getGumgaSecurityUrl() + "/token/organizations/" + token;
            List resposta = restTemplate.getForObject(url, List.class);
            return resposta;
        } catch (RestClientException restClientException) {
            throw new ProxyProblemResponse("Problema na comunicação com o segurança.", restClientException.getMessage()).exception();
        }
    }

    @Transactional
    @ApiOperation(value = "change organization", notes = "Altera a organização do token.")
    @RequestMapping(value = "/changeorganization/{token}/{orgId}", method = RequestMethod.GET)
    public Object changeOrganization(@PathVariable String token, @PathVariable Long orgId) {
        try {
            String url = gumgaValues.getGumgaSecurityUrl() + "/token/changeorganization/" + token + "/" + orgId;
            Map resposta = restTemplate.getForObject(url, Map.class);
            return resposta;
        } catch (RestClientException restClientException) {
            throw new ProxyProblemResponse("Problema na comunicação com o segurança.", restClientException.getMessage()).exception();
        }
    }

    @Transactional
    @ApiOperation(value = "organizations", notes = "Lista as operações associadas ao software e token informados.")
    @RequestMapping(value = "/operations/{software}/{token:.+}", method = RequestMethod.GET)
    public Set operations(@PathVariable String software, @PathVariable String token) {
        try {
            String url = gumgaValues.getGumgaSecurityUrl() + "/token/operations/" + software + "/" + token + "/";
            Set resposta = restTemplate.getForObject(url, Set.class);
            return resposta;
        } catch (RestClientException restClientException) {
            throw new ProxyProblemResponse("Problema na comunicação com o segurança.", restClientException.getMessage()).exception();
        }
    }

    @Transactional
    @ApiOperation(value = "organizations", notes = "Lista as operações associadas ao software e token informados.")
    @RequestMapping(value = "/operationskeys/{software}/{token:.+}", method = RequestMethod.GET)
    public Set operationsKeys(@PathVariable String software, @PathVariable String token) {
        try {
            String url = gumgaValues.getGumgaSecurityUrl() + "/token/operations/" + software + "/" + token + "/";
            Set resposta = restTemplate.getForObject(url, Set.class);
            HashSet<Object> keys = new HashSet<>();
            for (Iterator it = resposta.iterator(); it.hasNext();) {
                Map r = (Map) it.next();
                if(r != null && r.containsKey("key") && r.get("key") != null) {
                    keys.add(r.get("key"));
                }
            }
            return keys;
        } catch (RestClientException restClientException) {
            throw new ProxyProblemResponse("Problema na comunicação com o segurança.", restClientException.getMessage()).exception();
        }
    }

    @ApiOperation(value = "lostPassword", notes = "Permite recuperar a senha, enviando um e-mail para o login informado redirecionando o usuário para uma url.")
    @RequestMapping(method = RequestMethod.GET, value = "/lost-my-password")
    public Map lostMyPassword(@RequestParam("email") String login, @RequestParam("url") String urlCallback) {
        try {
            String url = gumgaValues.getGumgaSecurityUrl() + "/token/lost-my-password";
            url = url + "?email=" + login + "&url=" + urlCallback;
            Map resposta = restTemplate.getForObject(url, Map.class);
            return resposta;
        } catch (RestClientException restClientException) {
            throw new ProxyProblemResponse("Problema na comunicação com o segurança.", restClientException.getMessage()).exception();
        }
    }

    @ApiOperation(value = "lostPassword", notes = "Permite recuperar a senha, enviando um e-mail para o login informado.")
    @RequestMapping(method = RequestMethod.GET, value = "/lostpassword/{login:.+}")
    public Map lostPassword(@PathVariable String login) {
        try {
            String url = gumgaValues.getGumgaSecurityUrl() + "/token/lostpassword/" + login + "/";
            Map resposta = restTemplate.getForObject(url, Map.class);
            return resposta;
        } catch (RestClientException restClientException) {
            throw new ProxyProblemResponse("Problema na comunicação com o segurança.", restClientException.getMessage()).exception();
        }
    }

    @ApiOperation(value = "lostSoftwarePassword", notes = "Permite recuperar a senha, enviando um e-mail para o login informado.")
    @RequestMapping(method = RequestMethod.GET, value = "/lostsoftwarepassword/{software}/{login:.+}")
    public Map lostSoftwarePassword(@PathVariable String software, @PathVariable String login) {
        try {
            String url = gumgaValues.getGumgaSecurityUrl() + "/token/lostsoftwarepassword/" + software + "/" + login + "/";
            Map resposta = restTemplate.getForObject(url, Map.class);
            return resposta;
        } catch (RestClientException restClientException) {
            throw new ProxyProblemResponse("Problema na comunicação com o segurança.", restClientException.getMessage()).exception();
        }
    }

    @ApiOperation(value = "changeByTicket", notes = "Verifica se o ticket já foi utilizado e altera a senha do usuário.")
    @RequestMapping(method = RequestMethod.GET, value = "/lostpassword/{code}/{password:.+}")
    public Map changeByTicket(@PathVariable String code, @PathVariable String password) {
        try {
            String url = gumgaValues.getGumgaSecurityUrl() + "/token/lostpassword/" + code + "/" + password;
            Map resposta = restTemplate.getForObject(url, Map.class);
            return resposta;
        } catch (RestClientException restClientException) {
            throw new ProxyProblemResponse("Problema na comunicação com o segurança.", restClientException.getMessage()).exception();
        }
    }

    @ApiOperation(value = "findByTicket", notes = "Busca um ticket pelo código.")
    @RequestMapping(method = RequestMethod.GET, value = "/searchticket/{code}")
    public Map findByTicket(@PathVariable String code) {
        try {
            String url = gumgaValues.getGumgaSecurityUrl() + "/token/searchticket/" + code;
            Map resposta = restTemplate.getForObject(url, Map.class);
            return resposta;
        } catch (RestClientException restClientException) {
            throw new ProxyProblemResponse("Problema na comunicação com o segurança.", restClientException.getMessage()).exception();
        }
    }

    @ApiOperation(value = "/organizations/users", notes = "Buscar todos os usuarios por organização.")
    @RequestMapping(method = RequestMethod.GET, value = "/organizations/users/{token:.+}")
    public List findAllUserByOrganization(@PathVariable String token) {
        try {
            final String url = gumgaValues.getGumgaSecurityUrl() + "/token/organization/users?gumgaToken=" + token;
            List result = this.restTemplate.getForObject(url, List.class);
            return result;
        } catch (RestClientException restClientException) {
            throw new ProxyProblemResponse("Problema na comunicação com o segurança.", restClientException.getMessage()).exception();
        }
    }

    @ApiOperation(value = "/roles", notes = "Buscar todos os perfis.")
    @RequestMapping(method = RequestMethod.GET, value = "/roles")
    public List getAllRoles() {
        try {
            final String url = gumgaValues.getGumgaSecurityUrl() + "/token/roles";
            List result = this.restTemplate.getForObject(url, List.class);
            return result;
        } catch (RestClientException restClientException) {
            throw new ProxyProblemResponse("Problema na comunicação com o segurança.", restClientException.getMessage()).exception();
        }
    }

    @ApiOperation(value = "google-plus", notes = "Faz o login com google-plus recebendo email e token.")
    @RequestMapping(value = "/google-plus", method = RequestMethod.GET)
    public Map loginWithGooglePlus(@RequestParam("email") String email, @RequestParam("token") String googlePlusToken) {
        try {
            String url = gumgaValues.getGumgaSecurityUrl() + "/token/google-plus?email=" + email + "&token=" + googlePlusToken + "&softwareName=" + gumgaValues.getSoftwareName();
            Map resposta = restTemplate.getForObject(url, Map.class);
            return resposta;
        } catch (RestClientException restClientException) {
            throw new ProxyProblemResponse("Problema na comunicação com o segurança.", restClientException.getMessage()).exception();
        }
    }

    @ApiOperation(value = "findToken", notes = "Traz as informações do token.")
    @RequestMapping(value = "/get/{token:.+}", method = RequestMethod.GET)
    public Map findToken(@PathVariable String token) {
        try {
            String url = gumgaValues.getGumgaSecurityUrl() + "/token/get/" + token + "/";
            Map resposta = restTemplate.getForObject(url, Map.class);
            return resposta;
        } catch (RestClientException restClientException) {
            throw new ProxyProblemResponse("Problema na comunicação com o segurança.", restClientException.getMessage()).exception();
        }
    }

}

class UserImageDTO {

    private String metadados;
    private GumgaImage image;
    private byte[] imageData;

    public String getMetadados() {
        return metadados;
    }

    public void setMetadados(String metadados) {
        this.metadados = metadados;
    }

    public GumgaImage getImage() {
        return image;
    }

    public void setImage(GumgaImage image) {
        this.image = image;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

}
