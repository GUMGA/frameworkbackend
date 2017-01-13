package io.gumga.core;

import org.junit.Assert;
import org.junit.Test;

public class MunifTest {

    @Test
    public void testeFeliz() {
        Assert.assertTrue(ExemploUtils.ehPar(2));
        Assert.assertFalse(ExemploUtils.ehPar(3));
        Assert.assertTrue(ExemploUtils.ehPar(4));
        Assert.assertFalse(ExemploUtils.ehPar(5));
        Assert.assertTrue(ExemploUtils.ehPar(6));
    }

}
