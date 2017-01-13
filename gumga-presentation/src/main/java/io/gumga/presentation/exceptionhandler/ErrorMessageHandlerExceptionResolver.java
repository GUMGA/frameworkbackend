/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.presentation.exceptionhandler;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ErrorMessageHandlerExceptionResolver extends AbstractHandlerExceptionResolver {

    private static final int DEFAULT_ORDER = 0;

    private Map<Class<? extends Exception>, ErrorMessageFactory> errorMessageFactories;
    private HttpMessageConverter<?>[] messageConverters;

    public ErrorMessageHandlerExceptionResolver() {
        setOrder(DEFAULT_ORDER);
    }

    public void setErrorMessageFactories(ErrorMessageFactory[] errorMessageFactories) {
        this.errorMessageFactories = new HashMap<>(errorMessageFactories.length);
        for (ErrorMessageFactory<?> errorMessageFactory : errorMessageFactories) {
            this.errorMessageFactories.put(errorMessageFactory.getExceptionClass(), errorMessageFactory);
        }
    }

    public void setMessageConverters(HttpMessageConverter<?>[] messageConverters) {
        this.messageConverters = messageConverters;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ErrorMessageFactory errorMessageFactory = errorMessageFactories.get(ex.getClass());
        if (errorMessageFactory != null) {
            response.setStatus(errorMessageFactory.getResponseCode());
            ErrorMessage errorMessage = errorMessageFactory.getErrorMessage(ex);
            ServletWebRequest webRequest = new ServletWebRequest(request, response);
            try {
                return handleResponseBody(errorMessage, webRequest);
            } catch (Exception handlerException) {
                logger.warn("Handling of [" + ex.getClass().getName() + "] resulted in Exception", handlerException);
            }
        }
        return null;
    }

    /**
     * Copied from
     * {@link org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerExceptionResolver}
     */
    @SuppressWarnings("unchecked")
    private ModelAndView handleResponseBody(Object returnValue, ServletWebRequest webRequest)
            throws ServletException, IOException {

        HttpInputMessage inputMessage = new ServletServerHttpRequest(webRequest.getRequest());
        List<MediaType> acceptedMediaTypes = inputMessage.getHeaders().getAccept();
        if (acceptedMediaTypes.isEmpty()) {
            acceptedMediaTypes = Collections.singletonList(MediaType.ALL);
        }
        MediaType.sortByQualityValue(acceptedMediaTypes);
        try (ServletServerHttpResponse outputMessage = new ServletServerHttpResponse(webRequest.getResponse())) {
            Class<?> returnValueType = returnValue.getClass();
            if (this.messageConverters != null) {
                for (MediaType acceptedMediaType : acceptedMediaTypes) {
                    for (HttpMessageConverter messageConverter : this.messageConverters) {
                        if (messageConverter.canWrite(returnValueType, acceptedMediaType)) {
                            messageConverter.write(returnValue, acceptedMediaType, outputMessage);
                            return new ModelAndView();
                        }
                    }
                }
            }
            if (logger.isWarnEnabled()) {
                logger.warn("Could not find HttpMessageConverter that supports return type [" + returnValueType + "] and "
                        + acceptedMediaTypes);
            }
        }

        return null;
    }

}
