package io.gumga.domain.domains;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Representa um quantidade monetária
 *
 * @author munif
 */
public class GumgaMoney extends Number implements Serializable {

    public static final BigDecimal HUNDRED = BigDecimal.TEN.multiply(BigDecimal.TEN);

    private BigDecimal value;

    public GumgaMoney() {
        value = BigDecimal.ZERO;
    }

    public GumgaMoney(BigDecimal other) {
        if (other == null) {
            value = BigDecimal.ZERO;
        } else {
            value = new BigDecimal(other.toString());
            value.setScale(other.scale());
        }
    }

    public BigDecimal getValue() {
        return value;
    }

    public static GumgaMoney valueOf(String str) {
        return new GumgaMoney(new BigDecimal(str));
    }

    public static GumgaMoney valueOf(double val) {
        return new GumgaMoney(BigDecimal.valueOf(val));
    }

    public static GumgaMoney valueOf(double val, int scale) {
        BigDecimal v = BigDecimal.valueOf(val);
        v.setScale(scale);
        return new GumgaMoney(v);
    }

    @Override
    public int intValue() {
        return value.intValue();
    }

    @Override
    public long longValue() {
        return value.longValue();
    }

    @Override
    public float floatValue() {
        return value.floatValue();
    }

    @Override
    public double doubleValue() {
        return value.doubleValue();
    }

    public GumgaMoney add(GumgaMoney val) {
        return new GumgaMoney(value.add(val.value).setScale(value.scale()));
    }

    public GumgaMoney subtract(GumgaMoney val) {
        return new GumgaMoney(value.subtract(val.value).setScale(value.scale()));
    }

    public GumgaMoney multiply(GumgaMoney val) {
        return new GumgaMoney(value.multiply(val.value).setScale(value.scale()));
    }

    public GumgaMoney divideBy(GumgaMoney val) {
        return new GumgaMoney(value.divide(val.value).setScale(value.scale()));
    }

    public GumgaMoney divideBy(GumgaMoney val, RoundingMode roundingMode) {
        return divideBy(val, value.scale(), roundingMode);
    }

    public GumgaMoney divideBy(GumgaMoney val, int scale, RoundingMode roundingMode) {
        return new GumgaMoney(value.divide(val.value).setScale(scale, roundingMode));
    }

    public GumgaMoney percentageOf(GumgaMoney val) {
        if (val == null) {
            throw new GumgaMoneyException("Base value can't be null");
        }
        BigDecimal percentage = value.divide(val.getValue()).multiply(HUNDRED).setScale(value.scale());
        return new GumgaMoney(percentage);
    }

    public GumgaMoney discountPercentage(BigDecimal discountPercentage) {
        return discountPercentage(discountPercentage, RoundingMode.CEILING);
    }

    public GumgaMoney discountPercentage(BigDecimal discountPercentage, RoundingMode roundingMode) {
        return discountPercentage(discountPercentage, value.scale(), roundingMode);
    }

    public GumgaMoney discountPercentage(BigDecimal discountPercentage, int scale, RoundingMode roundingMode) {
        GumgaMoney discountValue = new GumgaMoney(discountPercentage.divide(HUNDRED).multiply(value).setScale(scale, roundingMode));
        return subtract(discountValue);
    }

    public boolean isPositive() {
        return value.signum() >= 1;
    }

    public boolean isNegative() {
        return value.signum() == -1;
    }

    public int compareTo(GumgaMoney other) {
        return value.compareTo(other.value);
    }

//    @Override
//    public String toString() {

//        return "GumgaMoney{value: "+value+"}";
//    }


    @Override
    public String toString() {
        String val;
        if (this.value != null) {
            val = this.value.setScale(2, RoundingMode.UP).toString();
        } else {
            val = "NAN";
        }
        return "{" + "\"value\":" + val + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.value);
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
        final GumgaMoney other = (GumgaMoney) obj;
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        return true;
    }

    public void setScale(int scale) {
        if (value != null) {
            value = value.setScale(scale);
        }
    }

    public void setScale(int scale, RoundingMode roundingMode) {
        if (value != null) {
            value = value.setScale(scale, roundingMode);
        }
    }
}
