package io.gumga.domain;

import br.com.insula.opes.CpfCnpj;
import io.gumga.core.GumgaIdable;
import io.gumga.core.GumgaThreadScope;
import io.gumga.domain.domains.*;
import io.gumga.domain.domains.usertypes.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Objects;

/**
 * Classe super importante para o framework. Esta classe deve ser a "mãe" de
 * todas as entidades gerenciadas pelo framework
 *
 * @author munif
 */
@MappedSuperclass
@TypeDefs({
    @TypeDef(name = "gumgaaddress", defaultForType = GumgaAddress.class, typeClass = GumgaAddressUserType.class),
    @TypeDef(name = "gumgaboolean", defaultForType = GumgaBoolean.class, typeClass = GumgaBooleanUserType.class),
    @TypeDef(name = "gumgabarcode", defaultForType = GumgaBarCode.class, typeClass = GumgaBarCodeUserType.class),
    @TypeDef(name = "gumgacep", defaultForType = GumgaCEP.class, typeClass = GumgaCEPUserType.class),
    @TypeDef(name = "gumgacnpj", defaultForType = GumgaCNPJ.class, typeClass = GumgaCNPJUserType.class),
    @TypeDef(name = "gumgacpf", defaultForType = GumgaCPF.class, typeClass = GumgaCPFUserType.class),
    @TypeDef(name = "gumgaemail", defaultForType = GumgaEMail.class, typeClass = GumgaEMailUserType.class),
    @TypeDef(name = "gumgafile", defaultForType = GumgaFile.class, typeClass = GumgaFileUserType.class),
    @TypeDef(name = "gumgageolocation", defaultForType = GumgaGeoLocation.class, typeClass = GumgaGeoLocationUserType.class),
    @TypeDef(name = "gumgaip4", defaultForType = GumgaIP4.class, typeClass = GumgaIP4UserType.class),
    @TypeDef(name = "gumgaip6", defaultForType = GumgaIP6.class, typeClass = GumgaIP6UserType.class),
    @TypeDef(name = "gumgaimage", defaultForType = GumgaImage.class, typeClass = GumgaImageUserType.class),
    @TypeDef(name = "gumgamoney", defaultForType = GumgaMoney.class, typeClass = GumgaMoneyUserType.class),
    @TypeDef(name = "gumgamutilinestring", defaultForType = GumgaMultiLineString.class, typeClass = GumgaMultiLineStringUserType.class),
    @TypeDef(name = "gumgaphonenumber", defaultForType = GumgaPhoneNumber.class, typeClass = GumgaPhoneNumberUserType.class),
    @TypeDef(name = "gumgatime", defaultForType = GumgaTime.class, typeClass = GumgaTimeUserType.class),
    @TypeDef(name = "gumgaoi", defaultForType = GumgaOi.class, typeClass = GumgaOiUserType.class),
    @TypeDef(name = "gumgaurl", defaultForType = GumgaURL.class, typeClass = GumgaURLUserType.class),
    @TypeDef(name = "cpfcnpj", defaultForType = CpfCnpj.class, typeClass = CpfCnpjUserType.class)

})
public abstract class GumgaModel<ID extends Serializable> implements GumgaIdable<ID>, Serializable {

    public static final String SEQ_NAME = "SEQ";
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = SEQ_NAME)
    protected ID id;

//    @org.hibernate.annotations.Index(name = "oi_index")
    protected GumgaOi oi;

    public GumgaModel() {
        Class classe = this.getClass();
        if (classe.isAnnotationPresent(GumgaMultitenancy.class)) {
            String oc = GumgaThreadScope.organizationCode.get();
            if (oc == null) {
                GumgaMultitenancy tenancy = this.getClass().getAnnotation(GumgaMultitenancy.class);
                oc = tenancy.publicMarking().getMark();
            }
            oi = new GumgaOi(oc);
        }
    }

    public GumgaModel(GumgaOi oi) {
        this.oi = oi;
    }

    public ID getId() {
        return id;
    }

    public GumgaOi getOi() {
        return oi;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        if (id == null) {
            return super.hashCode();
        }
        hash = 29 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GumgaModel<?> other = (GumgaModel<?>) obj;

        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
