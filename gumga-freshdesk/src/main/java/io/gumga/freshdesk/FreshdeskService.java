package io.gumga.freshdesk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;

/**
 * Serviço para manipulação de tickets no Freshdesk
 */
@Service
public class FreshdeskService {
    private static final Logger log = LoggerFactory.getLogger(FreshdeskService.class);
    private static final String DOMAIN = System.getProperty("freshdesk.domain");
    private static final String USER = System.getProperty("freshdesk.user");
    private static final String PASSWORD = System.getProperty("freshdesk.password");
    private static final String URL = "https://%s.freshdesk.com/api/v2/tickets";
    private final RestTemplate restTemplate;

    public FreshdeskService() {
        this.restTemplate = new RestTemplate();
    }

    public Boolean createATicket(Freshdesk freshdesk) {
        Objects.nonNull(DOMAIN);
        Objects.nonNull(USER);
        Objects.nonNull(PASSWORD);

        return sendTicket(freshdesk, DOMAIN, USER, PASSWORD);
    }

    /**
     * Cria um ticket
     * @param freshdesk Informações no freshdesk
     * @param domain Domínio, exemplo: teste.freshdesk.com
     * @param user Usuário
     * @param password senha
     * @return
     */
    public Boolean createATicket(Freshdesk freshdesk, String domain, String user, String password) {
        Objects.nonNull(domain);
        Objects.nonNull(user);
        Objects.nonNull(password);

        return sendTicket(freshdesk, domain, user, password);
    }

    private Boolean sendTicket(Freshdesk freshdesk, String domain, String user, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.put("Authorization", Arrays.asList(Base64.getEncoder().encodeToString((user.concat(":").concat(password)).getBytes(StandardCharsets.UTF_8))));
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<Map> exchange;
        try {
            exchange = restTemplate.exchange(String.format(URL, domain), HttpMethod.POST, new HttpEntity(freshdesk, headers), Map.class);
        } catch (HttpClientErrorException e) {
            log.error(e.getResponseBodyAsString(), e);
            return false;
        }
        if(exchange.getStatusCode().is2xxSuccessful()) {
            return true;
        }
        return false;
    }

//    public static void main(String[] args) {
//        FreshdeskService f = new FreshdeskService();
//        Freshdesk builder = Freshdesk.build()
//                .description("teste1231231")
//                .email("fsabadini@hotmail.com")
//                .subject("teste1123")
//                .type("Bug")
//                .group(28000007441l)
//                .builder();
//        Boolean aTicket = f.createATicket(builder);
//        System.out.println("asd" + aTicket);
//    }
}
