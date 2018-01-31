package io.gumga.presentation.api;

import java.util.Arrays;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Classe para manipulação de mensagens REST via Jackson
 */
public class GumgaJsonRestTemplate extends RestTemplate {
//    TODO Revisar o conteúdo deste JavaDocs

    /**
     * Construtor para um objeto Jackson HTTP
     */
    public GumgaJsonRestTemplate() {
        super(Arrays.asList(new MappingJackson2HttpMessageConverter()));
    }

}




