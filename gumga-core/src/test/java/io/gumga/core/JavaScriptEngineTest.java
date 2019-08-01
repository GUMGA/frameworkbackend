/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.core;

import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 * @author munif
 */
public class JavaScriptEngineTest {

    public JavaScriptEngineTest() {
    }

    @Test
    public void testEvalNumber() {
        Object eval = JavaScriptEngine.eval("5*3", null);
        System.out.println("eval=" + eval + " " + eval.getClass());
        assertEquals(15, eval);
    }

    @Test
    public void testEvalString() {
        Object eval = JavaScriptEngine.eval("'munif'", null);
        System.out.println("eval=" + eval + " " + eval.getClass());
        assertEquals("munif", eval);
    }

    @Test
    public void testEvalNumberWithContext() {
        QueryObject qo = new QueryObject();
        qo.setStart(10);
        Map<String,Object> contexto=new HashMap<>();
        contexto.put("qo", qo);
        Object eval = JavaScriptEngine.eval("2*qo.getStart()", contexto );
        System.out.println("eval=" + eval + " " + eval.getClass());
        assertEquals(20.0, eval);
    }

    @Test
    public void testEvalForDate() {
        Date date = JavaScriptEngine.evalForDate("new Date()", null);
        assertEquals(Date.class, date.getClass());
    }

    @Test
    public void testEvalNullForDate() {
        Date date = JavaScriptEngine.evalForDate(null, null);
        assertEquals(null, date);
    }

    @Test
    public void testEvalEmptyForDate() {
        Date date = JavaScriptEngine.evalForDate("", null);
        assertEquals(null, date);
    }
    

}
