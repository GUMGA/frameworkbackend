/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.domain;

import io.gumga.domain.domains.GumgaOi;

import java.lang.reflect.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 *
 * @author willian
 */
public class GumgaTenancyUtils {
    
    private static final Logger log = LoggerFactory.getLogger(GumgaTenancyUtils.class);

    /**
     * Método que altera o oi de qualquer objeto que herde de GumgaModel
     *
     * @param novoOi
     * @param gumgaModel
     */
    public static void changeOi(String novoOi, GumgaModel<Long> gumgaModel) {
        try {
            Field oi = GumgaModel.class.getDeclaredField("oi");
            oi.setAccessible(true);
            GumgaOi noi = new GumgaOi(novoOi);
            oi.set(gumgaModel, noi);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            log.error("Erro ao alterar o OI", ex);
        }
    }
}
