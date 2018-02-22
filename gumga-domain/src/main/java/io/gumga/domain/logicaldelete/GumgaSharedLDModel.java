package io.gumga.domain.logicaldelete;
import io.gumga.domain.GumgaModel;
import io.gumga.domain.domains.GumgaOi;
import io.gumga.domain.shared.GumgaSharedModel;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * Classe que possibilita o compartilhamento de registro entre organizações e usuários
 * e que implementa a exclusão lógica
 * @param <ID>
 */
@MappedSuperclass
public class GumgaSharedLDModel<ID extends Serializable> extends GumgaSharedModel<ID> {
    public static final int MAX_LENGTH = 4000;
    public static final String GLOBAL = "GLOBAL.";

    /**
     * Logical State
     */
    @Column(name = "gumga_active")
    protected Boolean gumgaActive;

    public GumgaSharedLDModel() {
        this.gumgaActive = true;
    }

    public GumgaSharedLDModel(GumgaOi oi) {
        super(oi);
        this.gumgaActive = true;

    }

    public Boolean getGumgaActive() {
        return gumgaActive;
    }

    public void setGumgaActive(Boolean gumgaActive) {
        this.gumgaActive = gumgaActive;
    }



}

class StringList {

    public static String add(String base, String value, int max) {
        if (!contains(base, value)) {
            String toReturn = base + value + ",";
//            if (toReturn.length() > max) {
//                throw new MaximumSharesExceededException("Capacidade de compartilhamentos excedida.");
//            }
            return toReturn;
        }
        return base;
    }

    public static String remove(String base, String value) {
        return base.replaceAll("," + value + ",", ",");
    }

    public static String removeAll() {
        return ",";
    }

    private static boolean contains(String base, String value) {
        return base.contains("," + value + ",");
    }

}
