package io.gumga.application.nlp;

import io.gumga.domain.nlp.GumgaNLPThing;

import java.math.BigDecimal;

@GumgaNLPThing("Nota")
public class NotaFiscal {

    private String cliente;

    private BigDecimal valor;

    @Override
    public String toString() {
        return "NotaFiscal{" + "cliente=" + cliente + ", valor=" + valor + '}';
    }

}
