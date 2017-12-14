package io.gumga.security_v2;


import io.gumga.core.GumgaThreadScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class GumgaCacheRequestFilterV2 extends GumgaRequestFilterV2 {

    private static final Logger log = LoggerFactory.getLogger(GumgaCacheRequestFilterV2.class);

    @Autowired
    private GumgaCacheRequestFilterV2Repository repository;
    private Long tokenDuration;

    public GumgaCacheRequestFilterV2(String software, Long tokenDuration) {
        super(software);
        this.tokenDuration = tokenDuration;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        if (!(o instanceof HandlerMethod)) {
            return true;
        }

        String endPoint = request.getRequestURL().toString();
        if (request.getServletPath().equals("/api/security/facereco/whois") || endPoint.contains("public") || endPoint.contains("api-docs")) {
            log.info("Não pegou do cache a autorização!");
            return super.preHandle(request, response, o);
        }
        String token = request.getHeader("gumgaToken");

        if (token == null) {
            token = request.getParameter("gumgaToken");
        }

        if (token == null) {
            log.info("Não pegou do cache a autorização!");
            return super.preHandle(request, response, o);
        }

        if(repository.isValid(token, tokenDuration)) {
            setGumgaThreadScope(repository.getData(token));
            log.info("Pegou do cache a autorização!");
            return true;
        }

        log.info("Não pegou do cache a autorização!");
        boolean result = super.preHandle(request, response, o);
        if(result) {
            Map<String, Object> data = getData();
            repository.add(token, data);
            setGumgaThreadScope(data);
        }

        return result;
    }

    private void setGumgaThreadScope(Map<String, Object> data) {
        GumgaThreadScope.ignoreCheckOwnership.set(Boolean.FALSE);

        Object login = data.get("login");
        if(login != null) {
            GumgaThreadScope.login.set((String) login);
        }

        Object organization = data.get("organization");
        if(organization != null) {
            GumgaThreadScope.organization.set((String) organization);
        }

        Object organizationCode = data.get("organizationCode");
        if(organizationCode != null) {
            GumgaThreadScope.organizationCode.set((String) organizationCode);
        }

        Object organizationId = data.get("organizationId");
        if(organizationId != null) {
            GumgaThreadScope.organizationId.set((Long) organizationId);
        }

        Object authorizationResponse = data.get("authorizationResponse");
        if(authorizationResponse != null) {
            GumgaThreadScope.authorizationResponse.set((Map) authorizationResponse);
        }

        Object softwareName = data.get("softwareName");
        if(softwareName != null) {
            GumgaThreadScope.softwareName.set((String) softwareName);
        }

        Object instanceOi = data.get("instanceOi");
        if(instanceOi != null) {
            GumgaThreadScope.instanceOi.set((String) instanceOi);
        }

        Object userRecognition = data.get("userRecognition");
        if(userRecognition != null) {
            GumgaThreadScope.userRecognition.set((String) userRecognition);
        }

        Object gumgaToken = data.get("gumgaToken");
        if(gumgaToken != null) {
            GumgaThreadScope.gumgaToken.set((String) gumgaToken);
        }
    }
}
