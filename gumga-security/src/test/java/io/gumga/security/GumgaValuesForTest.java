package io.gumga.security;

import io.gumga.core.GumgaValues;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class GumgaValuesForTest implements GumgaValues {

    public static final String DEFAULT_SECURITY_URL = "http://gumga.com.br";

    private Properties properties;

    public GumgaValuesForTest() {
        properties=getCustomFileProperties();
    }

    @Override
    public String getGumgaSecurityUrl() {
        String urlHost = properties.getProperty("url.host", DEFAULT_SECURITY_URL);
        return urlHost + "/security-api/publicoperations";
    }

    @Override
    public String getSoftwareName() {
        return "br.com.gumga.security";
    }
}
