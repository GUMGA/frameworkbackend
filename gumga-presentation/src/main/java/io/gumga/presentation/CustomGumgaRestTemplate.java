package io.gumga.presentation;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

public interface CustomGumgaRestTemplate {

    HttpComponentsClientHttpRequestFactory getRequestFactory();
    List<HttpMessageConverter<?>> getMessageConverters();

    default RestTemplate getRestTemplate(RestTemplate restTemplate) {
        Optional.ofNullable(getMessageConverters())
                .ifPresent(data -> restTemplate.setMessageConverters(data));

        Optional.ofNullable(getRequestFactory())
                .ifPresent(data -> restTemplate.setRequestFactory(data));

        return restTemplate;
    }
}
