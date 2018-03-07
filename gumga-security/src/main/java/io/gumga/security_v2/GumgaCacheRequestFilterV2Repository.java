package io.gumga.security_v2;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Classe com métodos para manipular filtro das requisições com cache
 */
@Component
public class GumgaCacheRequestFilterV2Repository {
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, Object>> cache;

    public GumgaCacheRequestFilterV2Repository() {
        this.cache = new ConcurrentHashMap<>();
    }

    /**
     * Adiciona cache
     * @param token Token
     * @param data Dados
     */
    public void add(String token, ConcurrentHashMap<String, Object> data) {
        if(!StringUtils.isEmpty(token)) {
            cache.put(token, data);
        }
    }

    /**
     * Remove cache
     * @param token Token
     */
    public void remove(String token) {
        if(!StringUtils.isEmpty(token)) {
            cache.remove(token);
        }
    }

    /**
     * Verifica se token é válido
     * @param token Token
     * @param seconds Tempo em segundos
     * @return Token é válido
     */
    public Boolean isValid(String token, Long seconds) {
        if(!StringUtils.isEmpty(token)) {
            ConcurrentHashMap<String, Object> result = cache.get(token);
            return result != null && result.containsKey("created") && ((LocalDateTime)result.get("created")).isAfter(LocalDateTime.now().minusSeconds(seconds));
        }
        return Boolean.FALSE;
    }

    /**
     * Dados do token
     * @param token Token
     * @return Dados do token
     */
    public ConcurrentHashMap<String, Object> getData(String token) {
        return cache.get(token);
    }
}
