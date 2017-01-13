/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.domain.domains;

/**
 * Representa uma posição com latitude e longitude
 *
 * Como é um domínio composto, deve possuir Columns(columns = { Column(name =
 * "location_latitude"), Column(name = "location_longitude") })
 *
 * @author munif
 */
public class GumgaGeoLocation extends GumgaDomain {

    private double latitude;
    private double longitude;

    public GumgaGeoLocation() {
    }

    public GumgaGeoLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public GumgaGeoLocation(GumgaGeoLocation other) {
        if (other != null) {
            this.latitude = other.latitude;
            this.longitude = other.longitude;
        }
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + (int) (Double.doubleToLongBits(this.latitude) ^ (Double.doubleToLongBits(this.latitude) >>> 32));
        hash = 47 * hash + (int) (Double.doubleToLongBits(this.longitude) ^ (Double.doubleToLongBits(this.longitude) >>> 32));
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
        final GumgaGeoLocation other = (GumgaGeoLocation) obj;
        if (Double.doubleToLongBits(this.latitude) != Double.doubleToLongBits(other.latitude)) {
            return false;
        }
        if (Double.doubleToLongBits(this.longitude) != Double.doubleToLongBits(other.longitude)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GumgaGeoLocation{" + "latitude=" + latitude + ", longitude=" + longitude + '}';
    }

}
