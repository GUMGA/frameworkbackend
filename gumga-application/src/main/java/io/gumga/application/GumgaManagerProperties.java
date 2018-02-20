package io.gumga.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe que permite o gerenciamento de todas as variáveis de ambiente da aplicação
 */
@Component
public class GumgaManagerProperties {
    private final Map<String, Object> properties = new HashMap<>();

    @Autowired
    private ConfigurableEnvironment configurableEnvironment;

    /**
     * Carrega todos os arquivos de propriedades da aplicação
     */
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

    /**
     * Busca propriedade que se encontra em qualquer parte da aplicação
     * @param key Nome da proprieade (Variável de ambiente)
     * @return Valor da propriedade (Variável de ambiente)
     */
    public String getProperty(String key) {
        return this.properties.get(key).toString();
    }

    /**
     * Busca propriedade que se encontra em qualquer parte da aplicação, porém se não houver nenhum, retorna um valor padrão
     * @param key Nome da proprieade (Variável de ambiente)
     * @return Valor da propriedade (Variável de ambiente)
     */
    public String getProperty(String key, String defaultValue) {
        return this.properties.getOrDefault(key, defaultValue).toString();
    }

    /**
     * Adiciona mapa de propriedades como variáveis de ambiente na aplicação
     * @param properties Mapa de propriedades
     */
    private void addAll(Map<String, Object> properties) {
        this.properties.putAll(properties);
    }
}