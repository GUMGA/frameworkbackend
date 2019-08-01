/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.core.gquery;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;


/**
 *
 * @author munif
 */
//@Disabled
public class GQueryTest {
/*

    @Test
    public void testEMPTY() {
        GQuery gQuery = new GQuery();
        assertEquals("(1 = 1)", gQuery.toString());
    }

    @Test
    public void testSIMPLE() {
        GQuery gQuery = new GQuery(new Criteria("name", ComparisonOperator.EQUAL, "munif"));
        assertEquals("(name = 'munif')", gQuery.toString());
    }

    @Test
    public void testNOT_EQUAL() {
        GQuery gQuery = new GQuery(LogicalOperator.NOT, new Criteria("name", ComparisonOperator.GREATER, "munif"));
        assertEquals("(!name > 'munif')", gQuery.toString());
    }

    @Test
    public void testAND() {
        GQuery gQuery = new GQuery(LogicalOperator.AND, Arrays.asList(new GQuery[]{
            new GQuery(new Criteria("name", ComparisonOperator.STARTS_WITH, "munif")),
            new GQuery(new Criteria("age", ComparisonOperator.GREATER_EQUAL, "18")),
            new GQuery(new Criteria("name", ComparisonOperator.CONTAINS, "gebara"))
        }));
        assertEquals("((name like 'munif%') AND (age >= '18') AND (name like '%gebara%'))", gQuery.toString());
    }

    @Test
    public void testOR() {
        GQuery gQuery = new GQuery(LogicalOperator.OR, Arrays.asList(new GQuery[]{
            new GQuery(new Criteria("name", ComparisonOperator.STARTS_WITH, "munif")),
            new GQuery(new Criteria("name", ComparisonOperator.STARTS_WITH, "vicente")),
            new GQuery(new Criteria("name", ComparisonOperator.STARTS_WITH, "duda"))
        }));
        assertEquals("((name like 'munif%') OR (name like 'vicente%') OR (name like 'duda%'))", gQuery.toString());
    }

    @Test
    public void testCOMPLEX1() {
        GQuery gQuery1 = new GQuery(LogicalOperator.OR, Arrays.asList(new GQuery[]{
            new GQuery(new Criteria("name", ComparisonOperator.STARTS_WITH, "munif")),
            new GQuery(new Criteria("name", ComparisonOperator.STARTS_WITH, "vicente")),
            new GQuery(new Criteria("name", ComparisonOperator.STARTS_WITH, "duda"))
        }));
        GQuery gQuery2 = new GQuery(new Criteria("name", ComparisonOperator.CONTAINS, "gebara"));
        GQuery gQuery = new GQuery(LogicalOperator.AND, Arrays.asList(new GQuery[]{gQuery1, gQuery2}));

        assertEquals("(((name like 'munif%') OR (name like 'vicente%') OR (name like 'duda%')) AND (name like '%gebara%'))", gQuery.toString());
    }

    @Test
    public void testFluent1() {
        GQuery gQuery1 = new GQuery(LogicalOperator.OR, Arrays.asList(new GQuery[]{
            new GQuery(new Criteria("name", ComparisonOperator.STARTS_WITH, "munif")),
            new GQuery(new Criteria("name", ComparisonOperator.STARTS_WITH, "vicente")),
            new GQuery(new Criteria("name", ComparisonOperator.STARTS_WITH, "duda"))
        }));
        GQuery gQuery2 = new GQuery(new Criteria("name", ComparisonOperator.CONTAINS, "gebara"));
        GQuery gQuery = gQuery1.and(gQuery2);
        assertEquals("(((name like 'munif%') OR (name like 'vicente%') OR (name like 'duda%')) AND (name like '%gebara%'))", gQuery.toString());
    }

    @Test
    public void testFluent2() {
        GQuery gQuery1 = new GQuery(LogicalOperator.OR, Arrays.asList(new GQuery[]{
            new GQuery(new Criteria("name", ComparisonOperator.STARTS_WITH, "munif")),
            new GQuery(new Criteria("name", ComparisonOperator.STARTS_WITH, "vicente")),
            new GQuery(new Criteria("name", ComparisonOperator.STARTS_WITH, "duda"))
        }));
        GQuery gQuery2 = new GQuery(new Criteria("name", ComparisonOperator.CONTAINS, "gebara"));
        GQuery gQuery = gQuery1.or(gQuery2);
        assertEquals("((name like 'munif%') OR (name like 'vicente%') OR (name like 'duda%') OR (name like '%gebara%'))", gQuery.toString());
    }

    @Test
    public void testFluent3() {
        GQuery gQuery = new GQuery(new Criteria("name", ComparisonOperator.STARTS_WITH, "munif"))
                .or(new Criteria("name", ComparisonOperator.STARTS_WITH, "vicente"))
                .or(new Criteria("name", ComparisonOperator.STARTS_WITH, "duda"))
                .or(new Criteria("name", ComparisonOperator.CONTAINS, "gebara"));
        assertEquals("((name like 'munif%') OR (name like 'vicente%') OR (name like 'duda%') OR (name like '%gebara%'))", gQuery.toString());
    }

    @Test
    public void testFluent4() {
        GQuery gQuery = new GQuery(new Criteria("name", ComparisonOperator.STARTS_WITH, "munif"))
                .or(new Criteria("name", ComparisonOperator.STARTS_WITH, "vicente"))
                .or(new Criteria("name", ComparisonOperator.STARTS_WITH, "duda"))
                .and(new Criteria("name", ComparisonOperator.CONTAINS, "gebara"));
        assertEquals("(((name like 'munif%') OR (name like 'vicente%') OR (name like 'duda%')) AND (name like '%gebara%'))", gQuery.toString());
    }
*/
}
