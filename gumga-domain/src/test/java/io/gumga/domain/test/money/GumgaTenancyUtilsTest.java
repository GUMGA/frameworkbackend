/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.domain.test.money;

import io.gumga.core.GumgaThreadScope;
import io.gumga.domain.GumgaTenancyUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 * @author willian
 */
@ExtendWith(SpringExtension.class)
public class GumgaTenancyUtilsTest {
    @Test
    public void testChangeOi() {
        GumgaThreadScope.organizationCode.set("1.2.");
        TestEntity testEntity = new TestEntity();
        assertEquals("1.2.",testEntity.getOi().getValue());
        GumgaTenancyUtils.changeOi("1.3.", testEntity);
        assertEquals("1.3.",testEntity.getOi().getValue());
    }
}
