package io.gumga.presentation;

import java.io.Serializable;

public class GumgaDTOAPIV2<T, ID extends Serializable> extends AbstractGumgaAPI<T, ID> {

    public GumgaDTOAPIV2(GumgaGatewayV2<?, ID, T> service) {
        super(service);
    }
}
