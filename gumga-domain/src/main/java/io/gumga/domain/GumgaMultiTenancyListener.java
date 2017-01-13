/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.domain;

import io.gumga.core.GumgaThreadScope;
import io.gumga.domain.domains.GumgaOi;

import javax.persistence.PrePersist;

/**
 * Esta classe é utilizada pelo framework para "marcar" as entidades com o
 * código da organização e proporcional o MultiTenancy
 *
 * @author munif
 */
public class GumgaMultiTenancyListener {

    @PrePersist
    public void prePersist(GumgaModel gumgaModel) {
        Class classe = gumgaModel.getClass();
        if (classe.isAnnotationPresent(GumgaMultitenancy.class)) {
            String oc = GumgaThreadScope.organizationCode.get();
            gumgaModel.oi = new GumgaOi(oc);
        }
    }
}
