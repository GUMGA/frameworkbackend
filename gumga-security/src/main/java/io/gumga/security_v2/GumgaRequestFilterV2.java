package io.gumga.security_v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.gumga.application.GumgaLogService;
import io.gumga.core.GumgaThreadScope;
import io.gumga.core.GumgaValues;
import io.gumga.domain.GumgaLog;
import io.gumga.security.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class GumgaRequestFilterV2 extends HandlerInterceptorAdapter {

    private static final Logger log = LoggerFactory.getLogger(HandlerInterceptorAdapter.class);
    private static final Logger logGumga = LoggerFactory.getLogger(GumgaRequestFilter.class);

    private final String softwareId;

    private final RestTemplate restTemplate;

    private ObjectMapper mapper;

    @Autowired
    private GumgaLogService gls;

    @Autowired
    private GumgaValues gumgaValues;

    private ThreadLocal<Long> tempo = new ThreadLocal<>();

    @Autowired(required = false)
    private ApiOperationTranslator aot;

    public void setAot(ApiOperationTranslator aot) {
        this.aot = aot;
    }

    public GumgaRequestFilterV2() {
        softwareId = "SomeSoftware";
        restTemplate = new RestTemplate();
        mapper = new ObjectMapper();
    }

    public GumgaRequestFilterV2(String si) {
        softwareId = si;
        restTemplate = new RestTemplate();
        mapper = new ObjectMapper();
    }

    public void dummy() {

    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        tempo.set(System.currentTimeMillis());
        String token;
        String errorMessage = "Error";
        String errorResponse = GumgaSecurityCode.SECURITY_INTERNAL_ERROR.toString();
        AuthorizatonResponse ar = new AuthorizatonResponse();
        String operationKey = "NOOP";

        try {
            GumgaThreadScope.userRecognition.set(request.getHeader("userRecognition"));

            token = request.getHeader("gumgaToken");
            if (token == null) {
                token = request.getParameter("gumgaToken");
            }
            if (token == null) {
                token = "no token";
            }
            GumgaThreadScope.gumgaToken.set(token);

            String endPoint = request.getRequestURL().toString();
            String method = request.getMethod();

            HandlerMethod hm;

            //TODO MUDANÇA DO SPRING DO 4.0.0 PARA 4.2.6
            if (o instanceof HandlerMethod) {
                hm = (HandlerMethod) o;
            } else {
                return true;
            }

            GumgaOperationKey gumgaOperationKeyMethodAnnotation = hm.getMethodAnnotation(GumgaOperationKey.class);
            if (gumgaOperationKeyMethodAnnotation != null) {
                operationKey = gumgaOperationKeyMethodAnnotation.value();
            } else {
                operationKey = aot.getOperation(endPoint, method, request);
            }
            if (operationKey.equals("NOOP")) {
                String apiName = hm.getBean().getClass().getSimpleName();
                if (apiName.contains("$$")) {
                    apiName = apiName.substring(0, apiName.indexOf("$$"));
                }
                operationKey = apiName + "_" + hm.getMethod().getName();
            }
            if (endPoint.contains("public") || endPoint.contains("api-docs")) {
                saveLog(new AuthorizatonResponse("allow", "public", "public", "public", "public", "public", null), request, operationKey, endPoint, method, true);
                return true;
            }

            String url = gumgaValues.getGumgaSecurityUrl() + "/token/authorize/" + softwareId + "/" + token + "/" + request.getRemoteAddr() + "/" + operationKey + "?version=v2";

//            ar = restTemplate.getForObject(url, AuthorizatonResponse.class);
            Map authorizatonResponse = restTemplate.getForObject(url, Map.class);

            ar = new AuthorizatonResponse(authorizatonResponse);

            GumgaThreadScope.login.set(ar.getLogin());
            GumgaThreadScope.ip.set(request.getRemoteAddr());
            GumgaThreadScope.organization.set(ar.getOrganization());
            GumgaThreadScope.organizationCode.set(ar.getOrganizationCode());
            GumgaThreadScope.operationKey.set(operationKey);
            GumgaThreadScope.organizationId.set(ar.getOrganizationId());
            GumgaThreadScope.authorizationResponse.set(authorizatonResponse);
            GumgaThreadScope.operationKey.set(operationKey);
            GumgaThreadScope.ip.set(request.getRemoteAddr());
            GumgaThreadScope.softwareName.set(softwareId);

            saveLog(ar, request, operationKey, endPoint, method, ar.isAllowed());
            if (ar.isAllowed()) {
                return true;
            } else {
                errorMessage = ar.toString();
                errorResponse = ar.getResponse();
            }
        } catch (Exception ex) {
            log.error("erro no filtro segurança", ex);
        }

        GumgaSecurityCode gsc = GumgaSecurityCode.valueOf(errorResponse);
        response.setStatus(gsc.httpStatus.value());
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("response", ar.getResponse());
        resposta.put("operation", operationKey);
        mapper.writeValue(response.getOutputStream(), resposta);

        //response.getOutputStream().write(("Error:" + errorMessage).getBytes());
        return false;
    }

    public void saveLog(AuthorizatonResponse ar, HttpServletRequest request, String operationKey, String endPoint, String method, boolean allowed, String token) {
        if (gumgaValues.isLogActive()) {
            GumgaLog gl = new GumgaLog(ar.getLogin(), request.getRemoteAddr(), ar.getOrganizationCode(),
                    ar.getOrganization(), softwareId, operationKey, endPoint, method, allowed);
            gls.save(gl);
        }
        if (gumgaValues.isLogRequestOnConsole()) {
            String contextRoot = request.getContextPath();
            String pathInfo = request.getRequestURI().substring(request.getRequestURI().indexOf(contextRoot));
            if (!gumgaValues.getUrlsToNotLog().contains(pathInfo)) {
                logGumga.info(String.format("Request anymarket from[%s] - login[%s] [%s][%s] [%s]- software[%s][%s] - destino[%s - %s - %s]", request.getRemoteAddr(),
                        ar.getLogin(), ar.getOrganizationCode(), ar.getOrganization(), token, softwareId, operationKey, method, pathInfo, allowed));
            }
        }
    }

    public void saveLog(AuthorizatonResponse ar, HttpServletRequest requset, String operationKey, String endPoint, String method, boolean a) {
        if (gumgaValues.isLogActive()) {
            GumgaLog gl = new GumgaLog(ar.getLogin(), requset.getRemoteAddr(), ar.getOrganizationCode(),
                    ar.getOrganization(), softwareId, operationKey, endPoint, method, a);
            gls.save(gl);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        tempo.remove();
        GumgaThreadScope.ip.remove();
        GumgaThreadScope.login.remove();
        GumgaThreadScope.organization.remove();
        GumgaThreadScope.organizationCode.remove();
        GumgaThreadScope.operationKey.remove();
        GumgaThreadScope.organizationId.remove();
    }

}
