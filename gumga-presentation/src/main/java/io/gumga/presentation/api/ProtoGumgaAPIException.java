/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.presentation.api;

/**
 *
 * @author munif
 */
class ProtoGumgaAPIException extends RuntimeException {

    public ProtoGumgaAPIException(Exception e) {
        super(e);
    }

}
