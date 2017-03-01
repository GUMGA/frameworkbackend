package io.gumga.presentation.api;

import java.util.Arrays;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class GumgaJsonRestTemplate extends RestTemplate {

    public GumgaJsonRestTemplate() {
        super(Arrays.asList(new MappingJackson2HttpMessageConverter()));
    }

}
