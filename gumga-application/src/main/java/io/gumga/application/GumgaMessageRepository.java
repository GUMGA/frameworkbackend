package io.gumga.application;

import io.gumga.domain.GumgaMessage;
import io.gumga.domain.repository.GumgaCrudRepository;

public interface GumgaMessageRepository extends GumgaCrudRepository<GumgaMessage, Long> {

}

