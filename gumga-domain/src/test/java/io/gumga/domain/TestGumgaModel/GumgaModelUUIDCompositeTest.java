package io.gumga.domain.TestGumgaModel;

import io.gumga.core.GumgaThreadScope;
import io.gumga.domain.TestGumgaModel.TestModel.EntityGumgaModelUUIDComposite;
import org.junit.Assert;
import org.junit.Test;

public class GumgaModelUUIDCompositeTest {

    @Test
    public void getOi() {
        GumgaThreadScope.organizationCode.set("2.");
        EntityGumgaModelUUIDComposite entity = new EntityGumgaModelUUIDComposite();
        Assert.assertEquals("2.", entity.getOi().getValue());
    }

//    @Test
//    public void toStringGumgaModelUUIDComposite() {
//        GumgaThreadScope.organizationCode.set("2.");
//        EntityGumgaModelUUIDComposite entity = new EntityGumgaModelUUIDComposite();
//        System.out.println(entity.getId());
//    }
}
