package io.gumga.security;

import com.wordnik.swagger.annotations.ApiOperation;
import io.gumga.core.GumgaThreadScope;
import io.gumga.core.GumgaValues;
import io.gumga.presentation.api.GumgaJsonRestTemplate;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.client.RestClientException;

/**
 * Métodos de busca de informações do segurança
 */
@RestController
@RequestMapping("/api/security")
public class GumgaSecurityEntitiesProxy {
    
    private static final Logger log = LoggerFactory.getLogger(GumgaSecurityEntitiesProxy.class);

    private final RestTemplate restTemplate;
    @Autowired
    private GumgaValues gumgaValues;

    public GumgaSecurityEntitiesProxy() {
        this.restTemplate = new GumgaJsonRestTemplate();
    }

    /**
     * Busca todas as organizações
     * @return Organizações
     */
    @ApiOperation(value = "getAllOrganizations", notes = "Buscar todas as organizações.")
    @RequestMapping(method = RequestMethod.GET, value = "/organizations")
    public Map getAllOrganizations() {
        final String param = "?gumgaToken=" + GumgaThreadScope.gumgaToken.get() + "&pageSize=" + (Integer.MAX_VALUE - 1);
        final String url = gumgaValues.getGumgaSecurityUrl().replace("/publicoperations", "/api/organization") + param;
        Map response = new HashedMap();
        try {
            response = restTemplate.getForObject(url, Map.class);
            return response;
        } catch (Exception e) {
            response.put(403, "Acesso negado!" + e);
        }
        return response;
    }

    /**
     * Organização pelo Oi
     * @param oi Oi
     * @return Organização
     */
    @ApiOperation(value = "getOrganizationFatByOi", notes = "Buscar todas as organizações pelo oi.")
    @RequestMapping(method = RequestMethod.GET, value = "/organizations/fatbyoi/{oi:.+}")
    public Map getOrganizationFatByOi(@PathVariable String oi) {
        final String param = "?gumgaToken=" + GumgaThreadScope.gumgaToken.get() + "&pageSize=" + (Integer.MAX_VALUE - 1);
        final String url = gumgaValues.getGumgaSecurityUrl().replace("/publicoperations", "/api/organization/fatbyoi/").concat(oi + "/") + param;
        Map response = new HashedMap();
        try {
            response = restTemplate.getForObject(url, Map.class);
            return response;
        } catch (Exception e) {
            response.put(403, "Acesso negado!" + e);
        }

        return response;
    }

    /**
     * Usuário pelo e-mail
     * @param email e-mail
     * @return Usuário
     */
    @RequestMapping(method = RequestMethod.GET, path = "/user-by-email/{email:.+}")
    public ResponseEntity<Map> getUserByEmail(@PathVariable String email) {
        return userByEmail(email);
    }

    /**
     * Usuário pelo e-mail
     * @param email e-mail
     * @return Usuário
     */
    @RequestMapping(method = RequestMethod.GET, path = "/user-by-email")
    public ResponseEntity<Map> getUserByEmailWithParam(@RequestParam("email") String email) {
        return userByEmail(email);
    }

    /**
     * Usuário pelo e-mail
     * @param email e-mail
     * @return Usuário
     */
    private ResponseEntity<Map> userByEmail(@RequestParam("email") String email) {
        final HttpHeaders headers = new HttpHeaders();
        try {
            headers.set("gumgaToken", GumgaThreadScope.gumgaToken.get());
            final String url = this.gumgaValues.getGumgaSecurityUrl().replace("/publicoperations", "/api/gumga-security/user-by-email/" + email + "/");
            final Map result = this.restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>(email, headers), Map.class).getBody();
            return ResponseEntity.ok(result);
        } catch (RestClientException restClientException) {
            throw new ProxyProblemResponse("Problema na comunicação com o segurança.", restClientException.getMessage()).exception();
        }
    }

    /**
     * Cria usário
     * @param user usuário
     * @return Usuário
     */
    @RequestMapping(method = RequestMethod.POST, path = "/create-user")
    public ResponseEntity<Map> createUser(@RequestBody Map user) {
        try {
            final HttpHeaders headers = new HttpHeaders();
            headers.set("gumgaToken", GumgaThreadScope.gumgaToken.get());
            final String url = this.gumgaValues.getGumgaSecurityUrl().replace("/publicoperations", "/api/gumga-security/create-user");
            final Map result = this.restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<Map>(user, headers), Map.class).getBody();
            if (result.get("id") != null) {
                final Long id = Long.valueOf(result.get("id").toString());
                if (id > 0l) {
                    addUserInOrganization(id);
                }
            }

            return ResponseEntity.ok(result);
        } catch (RestClientException restClientException) {
            throw new ProxyProblemResponse("Problema na comunicação com o segurança.", restClientException.getMessage()).exception();
        } catch (NumberFormatException numberFormatException) {
            throw new ProxyProblemResponse("Problema na comunicação com o segurança.", numberFormatException.getMessage()).exception();
        }
    }

    /**
     * Atualiza usuário
     * @param user usuário
     * @return Usuário
     */
    @RequestMapping(method = RequestMethod.PUT, path = "/update-user")
    public ResponseEntity<Map> updateUser(@RequestBody Map user) {
        try {
            final HttpHeaders headers = new HttpHeaders();
            headers.set("gumgaToken", GumgaThreadScope.gumgaToken.get());
            final String url = this.gumgaValues.getGumgaSecurityUrl().replace("/publicoperations", "/api/gumga-security/update-user");
            final Map result = this.restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<Map>(user, headers), Map.class).getBody();
            return ResponseEntity.ok(result);
        } catch (RestClientException restClientException) {
            throw new ProxyProblemResponse("Problema na comunicação com o segurança.", restClientException.getMessage()).exception();
        }
    }

    /**
     * Adiciona usuário na organização atual
     * @param idUser Id do usuário
     * @return Status
     */
    @RequestMapping(method = RequestMethod.GET, path = "/add-user-organization/{idUser}")
    public ResponseEntity<Void> addUserInOrganization(@PathVariable Long idUser) {
        try {
            final HttpHeaders headers = new HttpHeaders();
            headers.set("gumgaToken", GumgaThreadScope.gumgaToken.get());
            final String url = this.gumgaValues.getGumgaSecurityUrl().replace("/publicoperations", "/api/gumga-security/add-user-organization/" + idUser + "/" + GumgaThreadScope.organizationId.get());
            final Map result = this.restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>(headers), Map.class).getBody();
            return ResponseEntity.noContent().build();
        } catch (RestClientException restClientException) {
            throw new ProxyProblemResponse("Problema na comunicação com o segurança.", restClientException.getMessage()).exception();
        }
    }

    /**
     * Remove usuário de determinada organização
     * @param idUser Id do usuário
     * @param oi Id da organização
     * @return Status
     */
    @RequestMapping(method = RequestMethod.GET, path = "/remove-user-organization/{idUser}/{oi:.+}")
    public ResponseEntity<Void> removerUserOfOrganization(@PathVariable Long idUser, @PathVariable String oi) {
        try {
            final HttpHeaders headers = new HttpHeaders();
            headers.set("gumgaToken", GumgaThreadScope.gumgaToken.get());
            final String url = this.gumgaValues.getGumgaSecurityUrl().replace("/publicoperations", "/api/gumga-security/remove-user-organization/" + idUser + "/" + oi + "/");
            final Map result = this.restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>(headers), Map.class).getBody();
            return ResponseEntity.noContent().build();
        } catch (RestClientException restClientException) {
            throw new ProxyProblemResponse("Problema na comunicação com o segurança.", restClientException.getMessage()).exception();
        }
    }

    /**
     * Perfil pela instância
     * @return Lista de perfis
     */
    @RequestMapping(method = RequestMethod.GET, path = "/role-by-instance")
    public ResponseEntity<List<Map>> getRoleByInstance() {
        try {
            final HttpHeaders headers = new HttpHeaders();
            headers.set("gumgaToken", GumgaThreadScope.gumgaToken.get());
            final String url = this.gumgaValues.getGumgaSecurityUrl().replace("/publicoperations", "/api/gumga-security/role-by-instance");
            final List<Map> result = this.restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>(headers), List.class).getBody();
            return ResponseEntity.ok(result);
        } catch (RestClientException restClientException) {
            throw new ProxyProblemResponse("Problema na comunicação com o segurança.", restClientException.getMessage()).exception();
        }
    }

    /**
     * Adiciona Usuário no perfil
     * @param idUser Id do usuário
     * @param idRole Id do perfil
     * @return Status
     */
    @RequestMapping(method = RequestMethod.GET, path = "/add-user-role/{idUser}/{idRole}")
    public ResponseEntity<Void> addUserInRole(@PathVariable Long idUser, @PathVariable Long idRole) {
        try {
            final HttpHeaders headers = new HttpHeaders();
            headers.set("gumgaToken", GumgaThreadScope.gumgaToken.get());
            final String url = this.gumgaValues.getGumgaSecurityUrl().replace("/publicoperations", "/api/gumga-security/add-user-role/" + idUser + "/" + idRole);
            final Map result = this.restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>(headers), Map.class).getBody();
            return ResponseEntity.noContent().build();
        } catch (RestClientException restClientException) {
            throw new ProxyProblemResponse("Problema na comunicação com o segurança.", restClientException.getMessage()).exception();
        }
    }

    /**
     * Remove usuário do perfil
     * @param idUser Id do usuário
     * @param idRole Id do perfil
     * @return Status
     */
    @RequestMapping(method = RequestMethod.GET, path = "/remove-user-role/{idUser}/{idRole}")
    public ResponseEntity<Void> removeUserOfRole(@PathVariable Long idUser, @PathVariable Long idRole) {
        try {
            final HttpHeaders headers = new HttpHeaders();
            headers.set("gumgaToken", GumgaThreadScope.gumgaToken.get());
            final String url = this.gumgaValues.getGumgaSecurityUrl().replace("/publicoperations", "/api/gumga-security/remove-user-role/" + idUser + "/" + idRole);
            final Map result = this.restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>(headers), Map.class).getBody();
            return ResponseEntity.noContent().build();
        } catch (RestClientException restClientException) {
            throw new ProxyProblemResponse("Problema na comunicação com o segurança.", restClientException.getMessage()).exception();
        }
    }

    /**
     * Busca foto do usuário
     * @param userImage Usuário
     * @return Imagem
     */
    @RequestMapping(method = RequestMethod.POST, path = "/user-image")
    public ResponseEntity<Map> saveImageUser(@RequestBody Map userImage) {
        try {
            final HttpHeaders headers = new HttpHeaders();
            headers.set("gumgaToken", GumgaThreadScope.gumgaToken.get());
            final String url = this.gumgaValues.getGumgaSecurityUrl().replace("/publicoperations", "/api/userimage");
            final Map result = this.restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<Map>(userImage, headers), Map.class).getBody();
            return ResponseEntity.ok(result);
        } catch (RestClientException restClientException) {
            throw new ProxyProblemResponse("Problema na comunicação com o segurança.", restClientException.getMessage()).exception();
        }
    }

    /**
     * Busca fotos do usuário
     * @param idUser Id do Usuário
     * @return Imagem
     */
    @RequestMapping(method = RequestMethod.GET, path = "/image-by-user/{idUser}")
    public ResponseEntity<List<Map>> getAllImageByUser(@PathVariable Long idUser) {
        try {
            final HttpHeaders headers = new HttpHeaders();
            headers.set("gumgaToken", GumgaThreadScope.gumgaToken.get());
            final String url = this.gumgaValues.getGumgaSecurityUrl().replace("/publicoperations", "/api/userimage/user-id/" + idUser);
            final List<Map> result = this.restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<List>(headers), List.class).getBody();
            return ResponseEntity.ok(result);
        } catch (RestClientException restClientException) {
            throw new ProxyProblemResponse("Problema na comunicação com o segurança.", restClientException.getMessage()).exception();
        }
    }

    /**
     * Remove imagem do usuário
     * @param idImage Id do usuário
     * @return Status
     */
    @RequestMapping(method = RequestMethod.GET, path = "/remove-image/{idImage}")
    public ResponseEntity<Void> removeImage(@PathVariable Long idImage) {
        try {
            final HttpHeaders headers = new HttpHeaders();
            headers.set("gumgaToken", GumgaThreadScope.gumgaToken.get());
            final String url = this.gumgaValues.getGumgaSecurityUrl().replace("/publicoperations", "/api/userimage/" + idImage);
            this.restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<List>(headers), Map.class);
            return ResponseEntity.noContent().build();
        } catch (RestClientException restClientException) {
            throw new ProxyProblemResponse("Problema na comunicação com o segurança.", restClientException.getMessage()).exception();
        }
    }

    /**
     * Verifica o usuário de acordo com informações do mesmo, como reconhecimento facial
     * @param userImageDTO Metadados do usuário
     * @return Informações do usuário, caso seja válido
     */
    @ApiOperation(value = "/whois", notes = "Verificar o usuario.")
    @RequestMapping(method = RequestMethod.POST, value = "/whois")
    public Map whois(@RequestBody UserImageDTO userImageDTO) {
        try {
            final String url = gumgaValues.getGumgaSecurityUrl().replace("publicoperations", "api") + "/facereco/whois";
            Map resposta = restTemplate.postForObject(url, userImageDTO, Map.class);
            return resposta;
        } catch (RestClientException restClientException) {
            throw new ProxyProblemResponse("Problema na comunicação com o segurança.", restClientException.getMessage()).exception();
        }
    }

    /**
     * Busca usuário de acordo com imagem utilizando reconhecimento facial
     * @param userImage Dados do usuário
     * @return Informações do usuário
     */
    @RequestMapping(method = RequestMethod.POST, path = "/facereco/whois")
    public ResponseEntity<Map> facerecoWhois(@RequestBody Map userImage) {
        try {
            final HttpHeaders headers = new HttpHeaders();
            headers.set("gumgaToken", GumgaThreadScope.gumgaToken.get());
            final String url = this.gumgaValues.getGumgaSecurityUrl().replace("/publicoperations", "/api/facereco/whois");
            final Map result = this.restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<Map>(userImage, headers), Map.class).getBody();
            return ResponseEntity.ok(result);
        } catch (RestClientException restClientException) {
            throw new ProxyProblemResponse("Problema na comunicação com o segurança.", restClientException.getMessage()).exception();
        }
    }

}
