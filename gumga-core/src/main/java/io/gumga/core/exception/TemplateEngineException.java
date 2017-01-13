/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.core.exception;

/**
 *
 * @author gyowannyqueiroz
 */
public class TemplateEngineException extends Exception {

    public TemplateEngineException() {
    }

    public TemplateEngineException(String message) {
        super(message);
    }

    public TemplateEngineException(String message, Throwable cause) {
        super(message, cause);
    }

    public TemplateEngineException(Throwable cause) {
        super(cause);
    }

    public TemplateEngineException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
