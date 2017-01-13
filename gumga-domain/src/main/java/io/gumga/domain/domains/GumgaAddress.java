package io.gumga.domain.domains;

import java.util.Objects;

/**
 * Domínio de Endereço da Gumga, muito importante, quando for utiliza-lo,
 * colocar na entidade os nomes da colunas como no exemplo a seguir:<br>
 * {@literal @}Columns(columns = {<br> {@literal @}Column(name =
 * "endereco_zip_code"),<br> {@literal @}Column(name =
 * "endereco_premisse_type"),<br> {@literal @}Column(name =
 * "endereco_premisse"),<br> {@literal @}Column(name = "endereco_number"),<br>
 * {@literal @}Column(name = "endereco_information"),<br>
 * {@literal @}Column(name = "endereco_neighbourhood"),<br>
 * {@literal @}Column(name = "endereco_localization"),<br>
 * {@literal @}Column(name = "endereco_state"),<br> {@literal @}Column(name =
 * "endereco_country")<br>
 * })<br>
 * private GumgaAddress endereco;<br>
 *
 * @author munif
 */
public class GumgaAddress extends GumgaDomain {

    private String zipCode;
    private String premisseType;
    private String premisse;
    private String number;
    private String information;
    private String neighbourhood;
    private String localization;
    private String state;
    private String country;

    public GumgaAddress() {

    }

    public GumgaAddress(GumgaAddress other) {
        if (other != null) {
            this.zipCode = other.zipCode;
            this.premisseType = other.premisseType;
            this.premisse = other.premisse;
            this.number = other.number;
            this.information = other.information;
            this.neighbourhood = other.neighbourhood;
            this.localization = other.localization;
            this.state = other.state;
            this.country = other.country;
        }
    }

    public GumgaAddress(String zipCode, String premisseType, String premisse, String number, String information, String neighbourhood, String localization, String state, String country) {
        this.zipCode = zipCode;
        this.premisseType = premisseType;
        this.premisse = premisse;
        this.number = number;
        this.information = information;
        this.neighbourhood = neighbourhood;
        this.localization = localization;
        this.state = state;
        this.country = country;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPremisseType() {
        return premisseType;
    }

    public void setPremisseType(String premisseType) {
        this.premisseType = premisseType;
    }

    public String getPremisse() {
        return premisse;
    }

    public void setPremisse(String premisse) {
        this.premisse = premisse;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getNeighbourhood() {
        return neighbourhood;
    }

    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    public String getLocalization() {
        return localization;
    }

    public void setLocalization(String localization) {
        this.localization = localization;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.zipCode);
        hash = 23 * hash + Objects.hashCode(this.premisseType);
        hash = 23 * hash + Objects.hashCode(this.premisse);
        hash = 23 * hash + Objects.hashCode(this.number);
        hash = 23 * hash + Objects.hashCode(this.information);
        hash = 23 * hash + Objects.hashCode(this.neighbourhood);
        hash = 23 * hash + Objects.hashCode(this.localization);
        hash = 23 * hash + Objects.hashCode(this.state);
        hash = 23 * hash + Objects.hashCode(this.country);
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
        final GumgaAddress other = (GumgaAddress) obj;
        if (!Objects.equals(this.zipCode, other.zipCode)) {
            return false;
        }
        if (!Objects.equals(this.premisseType, other.premisseType)) {
            return false;
        }
        if (!Objects.equals(this.premisse, other.premisse)) {
            return false;
        }
        if (!Objects.equals(this.number, other.number)) {
            return false;
        }
        if (!Objects.equals(this.information, other.information)) {
            return false;
        }
        if (!Objects.equals(this.neighbourhood, other.neighbourhood)) {
            return false;
        }
        if (!Objects.equals(this.localization, other.localization)) {
            return false;
        }
        if (!Objects.equals(this.state, other.state)) {
            return false;
        }
        if (!Objects.equals(this.country, other.country)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GumgaAddress{" + "zipCode=" + zipCode + ", premisseType=" + premisseType + ", premisse=" + premisse + ", number=" + number + ", information=" + information + ", neighbourhood=" + neighbourhood + ", localization=" + localization + ", state=" + state + ", country=" + country + '}';
    }

}
