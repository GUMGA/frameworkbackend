package io.gumga.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class GumgaManagerProperties {
    private final Map<String, Object> properties = new HashMap<>();

    @Autowired
    private ConfigurableEnvironment configurableEnvironment;

    @PostConstruct
    private void loadProperties() {
        this.configurableEnvironment
                .getPropertySources()
                .forEach(propertySource -> {
                    if (propertySource instanceof EnumerablePropertySource) {
                        for (String key : ((EnumerablePropertySource) propertySource).getPropertyNames()) {
                            properties.put(key, propertySource.getProperty(key));
                        }
                    }
                });
    }

    public String getProperty(String key) {
        return this.properties.get(key).toString();
    }

    public String getProperty(String key, String defaultValue) {
        return this.properties.getOrDefault(key, defaultValue).toString();
    }

    private void addAll(Map<String, Object> properties) {
        this.properties.putAll(properties);
    }
}