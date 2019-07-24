package io.gumga.domain.TestGumgaModel;

import io.gumga.core.GumgaThreadScope;
import io.gumga.domain.TestGumgaModel.TestModel.EntityGumgaSharedModelUUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class)
public class GumgaSharedModelUUIDTest {

    @Test
    public void verifyOi() {
        GumgaThreadScope.organizationCode.set("1.");
        EntityGumgaSharedModelUUID testEntityV2 = new EntityGumgaSharedModelUUID();
        assertEquals("1.", testEntityV2.getOi().getValue());
    }
}
