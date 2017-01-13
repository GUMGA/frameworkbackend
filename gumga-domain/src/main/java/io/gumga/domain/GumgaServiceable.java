package io.gumga.domain;

import io.gumga.domain.service.GumgaDeletableServiceable;
import io.gumga.domain.service.GumgaReadableServiceable;
import io.gumga.domain.service.GumgaWritableServiceable;

/**
 * Interface básica para serviços do framework
 */
public interface GumgaServiceable<T> extends GumgaReadableServiceable<T>, GumgaWritableServiceable<T>, GumgaDeletableServiceable<T> {

}
