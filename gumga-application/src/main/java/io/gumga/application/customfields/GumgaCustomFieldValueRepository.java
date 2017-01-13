package io.gumga.application.customfields;

import io.gumga.domain.customfields.GumgaCustomField;
import io.gumga.domain.customfields.GumgaCustomFieldValue;
import io.gumga.domain.repository.GumgaCrudRepository;

public interface GumgaCustomFieldValueRepository extends GumgaCrudRepository<GumgaCustomFieldValue, Long> {

    GumgaCustomFieldValue findByFieldAndGumgaModelId(GumgaCustomField field, Long gumgaModelId);

}
