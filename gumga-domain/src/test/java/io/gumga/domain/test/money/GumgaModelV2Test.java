package io.gumga.domain.test.money;

import io.gumga.core.GumgaThreadScope;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


@RunWith(JUnit4.class)
public class GumgaModelV2Test {

    @Test
    public void t() {
        GumgaThreadScope.organizationCode.set("1.");
        EntityGumgaModelUUID testEntityV2 = new EntityGumgaModelUUID();
        Assert.assertEquals("1.", testEntityV2.getOi().getValue());
    }
}
