package io.gumga.security;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.gumga.core.GumgaThreadScope;
import io.gumga.core.GumgaValues;
import io.gumga.presentation.api.GumgaJsonRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

@Service
public class GumgaSecurityIntegration {

    @Autowired
    private GumgaValues gumgaValues;

    public ResponseEntity verifyAndCreateOperations(GumgaOperationTO[] gotos) {
        Set<GumgaOperationTO> gumgaOperations = Stream.concat(SecurityHelper.listMyOperations("")
                .stream(), Arrays.asList(gotos).stream())
                .collect(Collectors.toSet());
        return createKeysOnSecurity(gumgaOperations);
    }

    public GumgaJsonRestTemplate restTemplate() {
        return new GumgaJsonRestTemplate();
    }

    public ResponseEntity createKeysOnSecurity(Set<GumgaOperationTO> gumgaOperations) {
        if(this.gumgaValues.getSoftwareName().isEmpty()){
            throw new RuntimeException("O Nome do software deve estar preenchido no Gumga Values.");
        }
        if(this.gumgaValues.getCustomFileProperties().getProperty("security.token").isEmpty()){
            throw new RuntimeException("Precisamos que você informe um token eterno no arquivo Properties usando a chave: security.token");
        }
        String securityUrl = gumgaValues.getGumgaSecurityUrl().replace("/publicoperations", "");
        System.out.println("Verificando : "+securityUrl);
        boolean securityOnline = getResponseCode(securityUrl) == 200;
        if (securityOnline) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("gumgaToken", this.gumgaValues.getCustomFileProperties().getProperty("security.token", GumgaThreadScope.gumgaToken.get()));
            Map request = new HashMap();
            request.put("softwareName", this.gumgaValues.getSoftwareName());
            request.put("operations", gumgaOperations);
            Object resp = restTemplate().exchange(securityUrl+"/api/software/check-operations", HttpMethod.POST, new HttpEntity(request, headers), Object.class).getBody();
            return ResponseEntity.ok(resp);
        } else {
            System.out.println("GumgaFramework Log: Security is offline!");
            return ResponseEntity.notFound().build();
        }
    }

    public static int getResponseCode(String urlString) {
        try {
            URL u = new URL(urlString);
            HttpURLConnection huc = (HttpURLConnection) u.openConnection();
            huc.setRequestMethod("GET");
            huc.setConnectTimeout(2000);
            huc.setReadTimeout(2000);
            huc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)");
            huc.connect();
            return huc.getResponseCode();
        } catch (MalformedURLException ex) {
            return -1;
        } catch (IOException ex) {
            return -2;
        }
    }

}
