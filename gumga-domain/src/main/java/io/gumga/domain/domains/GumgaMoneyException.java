/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.domain.domains;

/**
 * Excessão utilizada quando houver problemas com unidades monetárias
 * @author munif
 */
class GumgaMoneyException extends RuntimeException {

    public GumgaMoneyException(String message) {
        super(message);
    }

}
