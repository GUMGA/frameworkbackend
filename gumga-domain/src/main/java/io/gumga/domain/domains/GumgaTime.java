package io.gumga.domain.domains;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * Representa o tempo com alguns facilitadores
 *
 * @author munif
 */
public final class GumgaTime extends GumgaDomain {

    private Date value;

    public GumgaTime() {
        value = new Date();
    }

    public GumgaTime(Date value) {
        this.value = value;
    }

    public GumgaTime(int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        value = cal.getTime();
    }

    public GumgaTime(GumgaTime other) {
        if (other != null) {
            value = other.value;
        }
    }

    public int getOnlyTimeInSeconds() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(value);
        return cal.get(Calendar.HOUR_OF_DAY) * 3600 + cal.get(Calendar.MINUTE) * 60 + cal.get(Calendar.SECOND);
    }

    public Date getValue() {
        return value;
    }

    public void setValue(Date value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.value);
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
        final GumgaTime other = (GumgaTime) obj;
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return value.toString();
    }

}
