/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.domain.test.money;

import io.gumga.domain.CurrencyFormatter;
import io.gumga.domain.domains.GumgaMoney;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.math.BigDecimal;
import java.util.Locale;

import static org.junit.Assert.*;

/**
 *
 * @author gyowanny
 */
@RunWith(JUnit4.class)
public class GumgaMoneyTest {
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testNewGumgaMoney() {
        GumgaMoney hundred = new GumgaMoney(GumgaMoney.HUNDRED);
        GumgaMoney money = new GumgaMoney(GumgaMoney.HUNDRED);
        assertNotNull(money.getValue());
        assertTrue(money.compareTo(hundred) == 0);
    }
    
    @Test
    public void testGumgaMoneyAdd() {
        GumgaMoney hundred = new GumgaMoney(GumgaMoney.HUNDRED);
        GumgaMoney twoHundred = new GumgaMoney(GumgaMoney.HUNDRED.multiply(new BigDecimal("2")));
        GumgaMoney money = new GumgaMoney(GumgaMoney.HUNDRED);
        assertNotNull(money.getValue());
        money = money.add(hundred);
        assertNotNull(money);
        assertNotNull(money.getValue());
        assertTrue(money.compareTo(twoHundred) == 0);
    }
    
    @Test
    public void testGumgaMoneySubtract() {
        GumgaMoney hundred = new GumgaMoney(GumgaMoney.HUNDRED);
        GumgaMoney money = new GumgaMoney(GumgaMoney.HUNDRED);
        assertNotNull(money.getValue());
        money = money.subtract(hundred);
        assertNotNull(money);
        assertNotNull(money.getValue());
        assertTrue(money.compareTo(new GumgaMoney(BigDecimal.ZERO)) == 0);
    }
    
    @Test
    public void testGumgaMoneyMultiply() {
        GumgaMoney hundred = new GumgaMoney(GumgaMoney.HUNDRED);
        GumgaMoney money = new GumgaMoney(GumgaMoney.HUNDRED);
        assertNotNull(money.getValue());
        money = money.multiply(hundred);
        assertNotNull(money);
        assertNotNull(money.getValue());
        assertTrue(money.compareTo(new GumgaMoney(GumgaMoney.HUNDRED.multiply(GumgaMoney.HUNDRED))) == 0);
    }
    
    @Test
    public void testGumgaMoneyDivideBy() {
        GumgaMoney hundred = new GumgaMoney(GumgaMoney.HUNDRED);
        GumgaMoney money = new GumgaMoney(GumgaMoney.HUNDRED);
        assertNotNull(money.getValue());
        money = money.divideBy(hundred);
        assertNotNull(money);
        assertNotNull(money.getValue());
        assertTrue(money.compareTo(new GumgaMoney(GumgaMoney.HUNDRED.divide(GumgaMoney.HUNDRED))) == 0);
    }
    
    @Test
    public void testGumgaMoneyPercentageOf() {
        GumgaMoney hundred = new GumgaMoney(GumgaMoney.HUNDRED);
        GumgaMoney value = GumgaMoney.valueOf("30");
        assertNotNull(value);
        assertNotNull(value.getValue());
        GumgaMoney percentageOf = value.percentageOf(hundred);
        assertNotNull(percentageOf);
        assertNotNull(percentageOf.getValue());
        GumgaMoney p = GumgaMoney.valueOf("30");
        p.setScale(hundred.getValue().scale());
        assertTrue(percentageOf.compareTo(p) == 0);

        boolean exception = false;
        try{
            percentageOf = value.percentageOf(null);
        }catch(Exception e){
            exception = true;
        }
        assertTrue(exception);
    }
    
    @Test
    @Ignore
    public void testFormatCurrency() {
        CurrencyFormatter.replaceSymbol("BRL", "R$");
        GumgaMoney hundred = new GumgaMoney(GumgaMoney.HUNDRED);
        CurrencyFormatter currencyFormatter = new CurrencyFormatter(new Locale("pt","BR"), 2);
        String formatted = currencyFormatter.format(hundred);
        assertNotNull(formatted);
        assertEquals(formatted, "R$ 100,00");
        
        currencyFormatter = new CurrencyFormatter(Locale.US, 2);
        formatted = currencyFormatter.format(hundred);
        assertNotNull(formatted);
        assertEquals(formatted, "$ 100.00");
    }
}
