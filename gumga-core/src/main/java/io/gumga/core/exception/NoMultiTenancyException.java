/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.core.exception;

/**
 *
 * @author willian
 */
public class NoMultiTenancyException extends RuntimeException{

    public NoMultiTenancyException(String message) {
        super(message);
    }
    
}
