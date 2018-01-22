package io.gumga.security_v2;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
        if(!StringUtils.isEmpty(token)) {
            cache.put(token, data);
        }
    }

    public void remove(String token) {
        if(!StringUtils.isEmpty(token)) {
            cache.remove(token);
        }
    }

    public Boolean isValid(String token, Long seconds) {
        if(!StringUtils.isEmpty(token)) {
            Map<String, Object> result = cache.get(token);
            return result != null && result.containsKey("created") && ((LocalDateTime)result.get("created")).isAfter(LocalDateTime.now().minusSeconds(seconds));
        }
        return Boolean.FALSE;
    }

    public Map<String, Object> getData(String token) {
        return cache.get(token);
    }
}
