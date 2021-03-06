package io.gumga.domain.shared;
import io.gumga.domain.GumgaModel;
import io.gumga.domain.domains.GumgaOi;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * Classe que possibilita o compartilhamento de registro entre organizações e usuários
 * @author munif, mateus, felipe
 */
@MappedSuperclass
public class GumgaSharedModel<ID extends Serializable> extends GumgaModel<ID> {

    public static final int MAX_LENGTH = 4000;
    public static final String GLOBAL = "GLOBAL.";
    public static final String SHARED_BASE = "B%s.MO";
    public static final String SHARED_MATRIX = "BM%s.%s.O";
    /**
     * Organizações
     */
    @Column(name = "gumga_orgs",length = MAX_LENGTH)
    private String gumgaOrganizations;
    /**
     * Usuários
     */
    @Column(name = "gumga_users",length = MAX_LENGTH)
    private String gumgaUsers;

    public GumgaSharedModel() {
        init();
    }

    public GumgaSharedModel(GumgaOi oi) {
        super(oi);
        init();
    }

    public String getGumgaOrganizations() {
        return gumgaOrganizations;
    }

    public String getGumgaUsers() {
        return gumgaUsers;
    }

    private final void init() {
        gumgaOrganizations = ",";
        gumgaUsers = ",";
    }

    public void addOrganization(String oi) {
        gumgaOrganizations = StringList.add(gumgaOrganizations, oi, MAX_LENGTH);
    }

    public void addUser(String login) {
        gumgaUsers = StringList.add(gumgaUsers, login, MAX_LENGTH);
    }

    public void removeOrganization(String oi) {
        gumgaOrganizations = StringList.remove(gumgaOrganizations, oi);
    }

    public void removeUser(String login) {
        gumgaUsers = StringList.remove(gumgaUsers, login);
    }

    public void removeAllOrganization() {
        gumgaOrganizations = StringList.removeAll();
    }

    public void removeAllUser() {
        gumgaUsers = StringList.removeAll();
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

//  ,abacaxi,laranja,fanta-laranja,coca,guarana,
// ,coca,guarana,

