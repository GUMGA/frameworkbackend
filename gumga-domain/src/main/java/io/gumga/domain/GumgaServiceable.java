package io.gumga.domain;

import io.gumga.domain.service.GumgaDeletableServiceable;
import io.gumga.domain.service.GumgaReadableServiceable;
import io.gumga.domain.service.GumgaWritableServiceable;

import java.io.Serializable;

/**
 * Interface básica para serviços do framework
 */
public interface GumgaServiceable<T, ID extends Serializable> extends GumgaReadableServiceable<T, ID>, GumgaWritableServiceable<T, ID>, GumgaDeletableServiceable<T, ID> {

}
