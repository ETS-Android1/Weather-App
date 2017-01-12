package nikitin.weatherapp.com.weatherapptest3.Model.Database;

import junit.framework.Test;

/**
 * Created by Влад on 28.11.2016.
 */
public class City {
    private long id;
    private String name;
    private String country;
    private double latitude;
    private double longitude;
    private int temperature;
    private String weather_type;
    private int humidity;
    private double wind_speed;
    private int pressure;
    private int wind_direction;
    private int date;

    public City() {}

//    public City(int id, int ow_id, String name, String country, double latitude, double longitude) {
//        this.id = id;
//        this.ow_id = ow_id;
//        this.name = name;
//        this.country = country;
//        this.latitude = latitude;
//        this.longitude = longitude;
//    }
    public City(long id, String name, String country, double latitude, double longitude,
                int temperature, String weather_type, int humidity, double wind_speed,
                int pressure, int wind_direction, int date) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        this.temperature = temperature;
        this.weather_type = weather_type;
        this.humidity = humidity;
        this.wind_speed = wind_speed;
        this.pressure = pressure;
        this.wind_direction = wind_direction;
        this.date = date;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
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

    public int getTemperature() {
        return temperature;
    }
    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public String getWeather_type() {
        return weather_type;
    }
    public void setWeather_type(String weather_type) {
        this.weather_type = weather_type;
    }

    public int getHumidity() {
        return humidity;
    }
    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getWind_speed() {
        return wind_speed;
    }
    public void setWind_speed(double wind_speed) {
        this.wind_speed = wind_speed;
    }

    public int getPressure() {
        return pressure;
    }
    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getWind_direction() {
        return wind_direction;
    }
    public void setWind_direction(int wind_direction) {
        this.wind_direction = wind_direction;
    }

    public int getDate() {
        return date;
    }
    public void setDate(int date) {
        this.date = date;
    }

    //    public int getId() {
//        return id;
//    }
//
//    public int getOw_id() {
//        return ow_id;
//    }
//    public void setOw_id(int ow_id) {
//        this.ow_id = ow_id;
//    }
//
//    public String getName() {
//        return name;
//    }
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getCountry() {
//        return country;
//    }
//    public void setCountry(String country) {
//        this.country = country;
//    }
//
//    public double getLatitude() {
//        return latitude;
//    }
//    public void setLatitude(double latitude) {
//        this.latitude = latitude;
//    }
//
//    public double getLongitude() {
//        return longitude;
//    }
//    public void setLongitude(double longitude) {
//        this.longitude = longitude;
//    }
}
