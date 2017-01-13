package io.gumga.security;

import com.wordnik.swagger.annotations.ApiOperation;
import io.gumga.core.GumgaThreadScope;
import io.gumga.core.GumgaValues;
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

@RestController
@RequestMapping("/api/security")
public class GumgaSecurityEntitiesProxy {

    private final RestTemplate restTemplate;
    @Autowired
    private GumgaValues gumgaValues;

    public GumgaSecurityEntitiesProxy() {
        this.restTemplate = new RestTemplate();
    }

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

    @RequestMapping(method = RequestMethod.GET, path = "/user-by-email/{email}")
    public ResponseEntity<Map> getUserByEmail(@PathVariable String email) {
        final HttpHeaders headers = new HttpHeaders();
        headers.set("gumgaToken", GumgaThreadScope.gumgaToken.get());
        final String url = this.gumgaValues.getGumgaSecurityUrl().replace("/publicoperations", "/api/gumga-security/user-by-email/" + email + "/");
        final Map result = this.restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>(email, headers), Map.class).getBody();
        return ResponseEntity.ok(result);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/create-user")
    public ResponseEntity<Map> getUserByEmail(@RequestBody Map user) {
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
    }

    @RequestMapping(method = RequestMethod.GET, path = "/add-user-organization/{idUser}")
    public ResponseEntity<Void> addUserInOrganization(@PathVariable Long idUser) {
        final HttpHeaders headers = new HttpHeaders();
        headers.set("gumgaToken", GumgaThreadScope.gumgaToken.get());
        final String url = this.gumgaValues.getGumgaSecurityUrl().replace("/publicoperations", "/api/gumga-security/add-user-organization/" + idUser + "/" + GumgaThreadScope.organizationId.get());
        final Map result = this.restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>(headers), Map.class).getBody();

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/remove-user-organization/{idUser}/{oi}")
    public ResponseEntity<Void> removerUserOfOrganization(@PathVariable Long idUser, @PathVariable String oi) {
        final HttpHeaders headers = new HttpHeaders();
        headers.set("gumgaToken", GumgaThreadScope.gumgaToken.get());
        final String url = this.gumgaValues.getGumgaSecurityUrl().replace("/publicoperations", "/api/gumga-security/remove-user-organization/" + idUser + "/" + oi + "/");
        final Map result = this.restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>(headers), Map.class).getBody();

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/role-by-instance")
    public ResponseEntity<List<Map>> getRoleByInstance() {
        final HttpHeaders headers = new HttpHeaders();
        headers.set("gumgaToken", GumgaThreadScope.gumgaToken.get());
        final String url = this.gumgaValues.getGumgaSecurityUrl().replace("/publicoperations", "/api/gumga-security/role-by-instance");
        final List<Map> result = this.restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>(headers), List.class).getBody();

        return ResponseEntity.ok(result);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/add-user-role/{idUser}/{idRole}")
    public ResponseEntity<Void> addUserInRole(@PathVariable Long idUser, @PathVariable Long idRole) {
        final HttpHeaders headers = new HttpHeaders();
        headers.set("gumgaToken", GumgaThreadScope.gumgaToken.get());
        final String url = this.gumgaValues.getGumgaSecurityUrl().replace("/publicoperations", "/api/gumga-security/add-user-role/" + idUser + "/" + idRole);
        final Map result = this.restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>(headers), Map.class).getBody();

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/remove-user-role/{idUser}/{idRole}")
    public ResponseEntity<Void> removeUserOfRole(@PathVariable Long idUser, @PathVariable Long idRole) {
        final HttpHeaders headers = new HttpHeaders();
        headers.set("gumgaToken", GumgaThreadScope.gumgaToken.get());
        final String url = this.gumgaValues.getGumgaSecurityUrl().replace("/publicoperations", "/api/gumga-security/remove-user-role/" + idUser + "/" + idRole);
        final Map result = this.restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>(headers), Map.class).getBody();

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(method = RequestMethod.POST, path = "/user-image")
    public ResponseEntity<Map> saveImageUser(@RequestBody Map userImage) {
        final HttpHeaders headers = new HttpHeaders();
        headers.set("gumgaToken", GumgaThreadScope.gumgaToken.get());
        final String url = this.gumgaValues.getGumgaSecurityUrl().replace("/publicoperations", "/api/userimage");

        final Map result = this.restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<Map>(userImage, headers), Map.class).getBody();

        return ResponseEntity.ok(result);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/image-by-user/{idUser}")
    public ResponseEntity<List<Map>> getAllImageByUser(@PathVariable Long idUser) {
        final HttpHeaders headers = new HttpHeaders();
        headers.set("gumgaToken", GumgaThreadScope.gumgaToken.get());
        final String url = this.gumgaValues.getGumgaSecurityUrl().replace("/publicoperations", "/api/userimage/user-id/" + idUser);

        final List<Map> result = this.restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<List>(headers), List.class).getBody();

        return ResponseEntity.ok(result);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/remove-image/{idImage}")
    public ResponseEntity<Void> removeImage(@PathVariable Long idImage) {
        final HttpHeaders headers = new HttpHeaders();
        headers.set("gumgaToken", GumgaThreadScope.gumgaToken.get());
        final String url = this.gumgaValues.getGumgaSecurityUrl().replace("/publicoperations", "/api/userimage/" + idImage);

        this.restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<List>(headers), Map.class);

        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "/whois", notes = "Verificar o usuario.")
    @RequestMapping(method = RequestMethod.POST, value = "/whois")
    public Map whois(@RequestBody UserImageDTO userImageDTO) {
        final String url = gumgaValues.getGumgaSecurityUrl().replace("publicoperations", "api") + "/facereco/whois";
        Map resposta = restTemplate.postForObject(url, userImageDTO, Map.class);
        return resposta;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/facereco/whois")
    public ResponseEntity<Map> facerecoWhois(@RequestBody Map userImage) {
        final HttpHeaders headers = new HttpHeaders();
        headers.set("gumgaToken", GumgaThreadScope.gumgaToken.get());
        final String url = this.gumgaValues.getGumgaSecurityUrl().replace("/publicoperations", "/api/facereco/whois");
        System.out.println("URL: " + url);
        final Map result = this.restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<Map>(userImage, headers), Map.class).getBody();

        return ResponseEntity.ok(result);
    }


}

