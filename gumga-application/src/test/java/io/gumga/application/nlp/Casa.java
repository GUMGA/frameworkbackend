package io.gumga.application.nlp;

import io.gumga.domain.nlp.GumgaNLPThing;

import java.math.BigDecimal;

/**
 *
 * @author munif
 */
@GumgaNLPThing
public class Casa {

    @GumgaNLPThing("cor")
    private String corDaCasa;
    private BigDecimal tamanho;
    @GumgaNLPThing("valor")
    private BigDecimal valorVenal;
    private String cpf;

    @Override
    public String toString() {
        return "Casa{" + "cor=" + corDaCasa + ", tamanho=" + tamanho + ", valor=" + valorVenal + ", cpf=" + cpf + '}';
    }

}
