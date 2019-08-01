/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.domain.customfields;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 *
 * @author munif
 */
public class GumgaCustomFieldTest {
    
    
    @Test
    public void emptyDate() {
        GumgaCustomField gcf = new GumgaCustomField();
        gcf.setType(CustomFieldType.DATE);
        gcf.setDefaultValueScript("");
        GumgaCustomFieldValue gcv=new GumgaCustomFieldValue(gcf);
        System.out.println("--------->"+gcv.getDateValue());
        assertNotNull(gcv);
    }
    
    
}
