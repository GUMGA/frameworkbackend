package io.gumga.presentation;

import java.io.Serializable;

/**
 * Classe para manipulação de objetos DTO
 * @param <T>
 * @param <ID>
 */
public class GumgaDTOAPIV2<T, ID extends Serializable> extends AbstractGumgaAPI<T, ID> {
    /**
     * Construtor com a injeção do módulo Service para acesso aos serviços do Framework
     * @param service Objeto Service a ser injetado
     */
    public GumgaDTOAPIV2(GumgaGatewayV2<?, ID, T> service) {
        super(service);
    }
}
