/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.domain;

import io.gumga.domain.domains.GumgaOi;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author willian
 */
public class GumgaTenancyUtils {

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
            Logger.getLogger(GumgaTenancyUtils.class.getName()).log(Level.SEVERE, "Erro ao alterar o OI", ex);
        }
    }
}
