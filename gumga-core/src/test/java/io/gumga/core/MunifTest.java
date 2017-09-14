package io.gumga.core;

import io.gumga.core.gquery.ComparisonOperator;
import io.gumga.core.gquery.Criteria;
import io.gumga.core.gquery.GQuery;
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

    @Test
    public void test() {
        GQuery gQuery = new GQuery(new Criteria("nome", ComparisonOperator.CONTAINS, "mafe"));
        System.out.println(gQuery.toString());

        GQuery idade = gQuery.and(new Criteria("idade", ComparisonOperator.GREATER, "20"));
        System.out.println(idade.toString());
    }


}
