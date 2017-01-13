/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.domain.test.money;

import io.gumga.core.GumgaThreadScope;
import io.gumga.domain.GumgaTenancyUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 *
 * @author willian
 */
@RunWith(JUnit4.class)
public class GumgaTenancyUtilsTest {
    @Test
    public void testChangeOi() {
        GumgaThreadScope.organizationCode.set("1.2.");
        TestEntity testEntity = new TestEntity();
        Assert.assertEquals("1.2.",testEntity.getOi().getValue());
        GumgaTenancyUtils.changeOi("1.3.", testEntity);
        Assert.assertEquals("1.3.",testEntity.getOi().getValue());
    }
}
