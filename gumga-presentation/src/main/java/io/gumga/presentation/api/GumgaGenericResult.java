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
public class GumgaGenericResult {

    private GumgaEntityMetadata metadata;
    private Object data;

    public GumgaGenericResult(Object data) {
        this.data = data;
        metadata = new GumgaEntityMetadata(data.getClass());
    }

    public GumgaEntityMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(GumgaEntityMetadata metadata) {
        this.metadata = metadata;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return data.toString();
    }

}
