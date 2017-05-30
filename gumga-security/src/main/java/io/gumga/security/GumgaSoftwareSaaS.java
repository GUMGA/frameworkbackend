package io.gumga.security;

import io.gumga.core.GumgaThreadScope;
import io.gumga.core.GumgaValues;
import io.gumga.domain.saas.GumgaSaaS;
import io.gumga.presentation.api.GumgaJsonRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import org.springframework.web.client.RestClientException;

@RestController
@RequestMapping(path = "/api/proxy/security-saas")
public class GumgaSoftwareSaaS {

    @Autowired
    private GumgaValues gumgaValues;

    private String getBaseUrl() {
        return gumgaValues.getGumgaSecurityUrl().replace("/publicoperations", "/api/security-saas");
    }

    private RestTemplate restTemplate;

    private RestTemplate getRestTemplate() {
        if (restTemplate == null) {
            restTemplate = new GumgaJsonRestTemplate();
        }
        return restTemplate;
    }

    @RequestMapping(value = "/instance", method = RequestMethod.POST)
    public ResponseEntity registerSaaS(@RequestBody GumgaSaaS gumgaSaaS) {
        return post("/instance", gumgaSaaS);
    }

    private ResponseEntity<Map> post(String uri, GumgaSaaS gumgaSaaS) {
        try {
            this.restTemplate = getRestTemplate();
            HttpEntity requestEntity = createRequestEntity(gumgaSaaS);
            String url = getBaseUrl().concat(uri);
            return this.restTemplate.exchange(url, HttpMethod.POST, requestEntity, Map.class);
        } catch (RestClientException restClientException) {
            throw new ProxyProblemResponse("Problema na comunicação com o sergurança.", restClientException.getMessage()).exception();
        }
    }

    private HttpEntity createRequestEntity(GumgaSaaS gumgaSaaS) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "application/json, text/plain, */*");
            headers.set("Accept-Encoding", "gzip, deflate");
            headers.set("Content-Type", "application/json;charset=utf-8");
            headers.set("gumgaToken", GumgaThreadScope.gumgaToken.get());
            return new HttpEntity(gumgaSaaS, headers);
        } catch (Exception e) {
            throw new ProxyProblemResponse("Problema na comunicação com o sergurança.", e.getMessage()).exception();
        }
    }
}
