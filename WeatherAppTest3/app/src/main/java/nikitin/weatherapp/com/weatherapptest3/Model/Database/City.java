package nikitin.weatherapp.com.weatherapptest3.Model.Database;

import junit.framework.Test;

/**
 * Created by Влад on 28.11.2016.
 */
public class City {
    private int id;
    private int ow_id;
    private String name;
    private String country;
    private double latitude;
    private double longitude;

    public City() {}

    public City(int id, int ow_id, String name, String country, double latitude, double longitude) {
        this.id = id;
        this.ow_id = ow_id;
        this.name = name;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public int getOw_id() {
        return ow_id;
    }
    public void setOw_id(int ow_id) {
        this.ow_id = ow_id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
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
}
