/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.application;

import java.io.IOException;

/**
 *
 * @author munif
 */
public class GumgaGenericRepostoryHelperException extends RuntimeException {

    public GumgaGenericRepostoryHelperException(String message) {
        super(message);
    }

    GumgaGenericRepostoryHelperException(IOException ex) {
        super(ex);
    }

}
