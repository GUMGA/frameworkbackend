/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.domain.test.money;

import io.gumga.domain.CurrencyFormatter;
import io.gumga.domain.domains.GumgaMoney;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author gyowanny
 */
@ExtendWith(SpringExtension.class)
public class GumgaMoneyTest {
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
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
    
//    @Test
//    @Disabled
//    public void testFormatCurrency() {
//        CurrencyFormatter.replaceSymbol("BRL", "R$");
//        GumgaMoney hundred = new GumgaMoney(GumgaMoney.HUNDRED);
//        CurrencyFormatter currencyFormatter = new CurrencyFormatter(new Locale("pt","BR"), 2);
//        String formatted = currencyFormatter.format(hundred);
//        assertNotNull(formatted);
//        assertEquals(formatted, "R$ 100,00");
//
//        currencyFormatter = new CurrencyFormatter(Locale.US, 2);
//        formatted = currencyFormatter.format(hundred);
//        assertNotNull(formatted);
//        assertEquals(formatted, "$ 100.00");
//    }
}
