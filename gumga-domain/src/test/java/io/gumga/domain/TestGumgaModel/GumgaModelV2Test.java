package io.gumga.domain.TestGumgaModel;

import io.gumga.core.GumgaThreadScope;
import io.gumga.domain.TestGumgaModel.TestModel.EntityGumgaModelUUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class)
public class GumgaModelV2Test {

    @Test
    public void t() {
        GumgaThreadScope.organizationCode.set("1.");
        EntityGumgaModelUUID testEntityV2 = new EntityGumgaModelUUID();
        assertEquals("1.", testEntityV2.getOi().getValue());
    }
}
