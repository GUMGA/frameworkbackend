/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.core;

import java.util.Map;

/**
 * Claasse que contem o escopo GUMGA, acessível de qualquer camada que é
 * preenchida pelo interceptor. Seu uso é recomendado na Integração e em
 * consultas não triviais que necessitam do O.I. (Organization Identification),
 * de informações sobre a requisição (Token, IP, Operação) e permite ignorar a
 * checagem de propriedade para perminir a execução de métodos de negócio que
 * permeiam o Tenancy.
 *
 * @author munif
 */

public class GumgaThreadScope {

    /**
     * Ip da requisição
     */
    public static final ThreadLocal<String> ip = new ThreadLocal<>();
    /**
     * login do usuário da requisição
     */
    public static final ThreadLocal<String> login = new ThreadLocal<>();
    /**
     * nome organização do usuário da requisição
     */
    public static final ThreadLocal<String> organization = new ThreadLocal<>();
    /**
     * código da organização do usuário da requisição
     */
    public static final ThreadLocal<String> organizationCode = new ThreadLocal<>();
    /**
     * chave da operação da requisição
     */
    public static final ThreadLocal<String> operationKey = new ThreadLocal<>();
    /**
     * irá ignorar a checagem de propriedade dos registros do Tenacy
     */
    public static final ThreadLocal<Boolean> ignoreCheckOwnership = new ThreadLocal<>();
    /**
     * token da requisição
     */
    public static final ThreadLocal<String> gumgaToken = new ThreadLocal<>();

    /**
     * id do Software que fez a requisição
     */
    public static final ThreadLocal<String> softwareName = new ThreadLocal<>();

    /**
     * id da Organição que fez a requisição
     */
    public static final ThreadLocal<Long> organizationId = new ThreadLocal<>();

    /**
     * perfis do usuario que está logado
     */
    public static final ThreadLocal<Map> authorizationResponse = new ThreadLocal<>();
    /*
        id do usuario que foi reconhecido
     */
    public static final ThreadLocal<String> userRecognition = new ThreadLocal<>();

}
