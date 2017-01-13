/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.domain;

/**
 * Determina a política de Multitenancy a ser utilizada
 * @author munif
 */
public enum GumgaMultitenancyPolicy {
    /**
     * Organizaçoes podem ver registros de suas sub organizaçoes mas o inverso nao e permitido.
     */
    TOP_DOWN,
    /**
     * Todos tem o mesmo acesso dentro da hieraquia.
     */
    ORGANIZATIONAL
    
    
    
}
