package io.gumga.security;

import io.gumga.presentation.exceptionhandler.GumgaRunTimeException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;

public class ProxyProblemResponse {

    public String message;

    public String cause;

    public ProxyProblemResponse(String message, String cause) {
        this.message = message;
        this.cause = cause;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public Map<String, Object> getMap() {
        Map resposta = new HashMap<String, Object>();
        resposta.put("message", message);
        resposta.put("cause", cause);
        return resposta;
    }

    public List<String> getList() {
        List resposta = new ArrayList<String>();
        resposta.add("message:" + message);
        resposta.add("cause:" + cause);
        return resposta;
    }

    public RuntimeException exception() {
        return new GumgaRunTimeException("message:" + message + "\n" + "cause:" + cause, HttpStatus.SERVICE_UNAVAILABLE);
    }

}
