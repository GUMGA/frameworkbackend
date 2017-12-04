package io.gumga.security_v2;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class GumgaCacheRequestFilterV2Repository {
    private final Map<String, Map<String, Object>> cache;

    public GumgaCacheRequestFilterV2Repository() {
        this.cache = new HashMap<>();
    }

    public void add(String token, Map<String, Object> data) {
        cache.put(token, data);
    }

    public void remove(String token) {
        cache.remove(token);
    }

    public Boolean isValid(String token, Long seconds) {
        Map<String, Object> result = cache.get(token);
        return result != null && result.containsKey("created") && ((LocalDateTime)result.get("created")).isAfter(LocalDateTime.now().minusSeconds(seconds));
    }

    public Map<String, Object> getData(String token) {
        return cache.get(token);
    }
}
