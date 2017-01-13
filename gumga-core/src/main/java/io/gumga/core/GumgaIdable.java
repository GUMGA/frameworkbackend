package io.gumga.core;

import java.io.Serializable;

/**
 * Claasse base para qualquer entidade que possui um atributo ID
 *
 * @author Equipe Gumga
 */
@FunctionalInterface
public interface GumgaIdable<T extends Serializable> {

    T getId();

}
