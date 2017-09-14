package io.gumga.core.gquery;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author munif
 */
public class CriteriaTest {


    @Test
    public void testEMPTY() {
        Criteria c = new Criteria();
        assertEquals("1='1'", c.toString());
    }

    @Test
    public void testEQUAL() {
        Criteria c = new Criteria("name", ComparisonOperator.EQUAL, "munif");
        assertEquals("name=\'munif\'", c.toString());
    }

    @Test
    public void testGREATER() {
        Criteria c = new Criteria("name", ComparisonOperator.GREATER, "munif");
        assertEquals("name>\'munif\'", c.toString());
    }

    @Test
    public void testGREATER_EQUAL() {
        Criteria c = new Criteria("name", ComparisonOperator.GREATER_EQUAL, "munif");
        assertEquals("name>=\'munif\'", c.toString());
    }

    @Test
    public void testLOWER() {
        Criteria c = new Criteria("name", ComparisonOperator.LOWER, "munif");
        assertEquals("name<\'munif\'", c.toString());
    }

    @Test
    public void testLOWER_EQUAL() {
        Criteria c = new Criteria("name", ComparisonOperator.LOWER_EQUAL, "munif");
        assertEquals("name<=\'munif\'", c.toString());
    }

    @Test
    public void testNOT_EQUAL() {
        Criteria c = new Criteria("name", ComparisonOperator.NOT_EQUAL, "munif");
        assertEquals("name!=\'munif\'", c.toString());
    }

    @Test
    public void testLIKE() {
        Criteria c = new Criteria("name", ComparisonOperator.LIKE, "munif%junior");
        assertEquals("name like \'munif%junior\'", c.toString());
    }

    @Test
    public void testSTARTS_WITH() {
        Criteria c = new Criteria("name", ComparisonOperator.STARTS_WITH, "munif");
        assertEquals("name like \'munif%\'", c.toString());
    }

    @Test
    public void testENDS_WITH() {
        Criteria c = new Criteria("name", ComparisonOperator.ENDS_WITH, "munif");
        assertEquals("name like \'%munif\'", c.toString());
    }

    @Test
    public void testCONTAINS() {
        Criteria c = new Criteria("name", ComparisonOperator.CONTAINS, "munif");
        assertEquals("name like \'%munif%\'", c.toString());
    }

    @Test
    public void testFuntions() {
        Criteria c = new Criteria("name", ComparisonOperator.CONTAINS, "munif");
        c.setFieldFunction("lower(%s)");
        c.setValueFunction("LOWER(%s)");
        System.out.println("----->" + c);
        assertEquals("lower(name) like LOWER(\'%munif%\')", c.toString());
    }

    @Test
    public void testFuntions2() {
        Criteria c = new Criteria("name", ComparisonOperator.STARTS_WITH, "munif");
        c.setFieldFunction("lower(%s)");
        c.setValueFunction("LOWER(%s)");
        System.out.println("----->" + c);
        assertEquals("lower(name) like LOWER(\'munif%\')", c.toString());
    }

    @Test
    public void testFuntions3() {
        Criteria c = new Criteria("name", ComparisonOperator.ENDS_WITH, "munif");
        c.setFieldFunction("lower(%s)");
        c.setValueFunction("LOWER(%s)");
        System.out.println("----->" + c);
        assertEquals("lower(name) like LOWER(\'%munif\')", c.toString());
    }

}
