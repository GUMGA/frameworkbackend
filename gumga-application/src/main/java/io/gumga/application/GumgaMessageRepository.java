package io.gumga.application;

import io.gumga.domain.GumgaMessage;
import io.gumga.domain.repository.GumgaCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GumgaMessageRepository extends GumgaCrudRepository<GumgaMessage, Long> {

}

