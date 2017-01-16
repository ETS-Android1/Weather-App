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
    private int group_code;
    private String weather_detailed_type;
    private static final int KELVIN_TO_CELSIUS = 273;

    public City() {}
    public City(long id, String name, String country, double latitude, double longitude,
                int temperature, String weather_type, int humidity, double wind_speed,
                int pressure, int wind_direction, int date, int group_code, String weather_detailed_type) {
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
        this.group_code = group_code;
        this.weather_detailed_type = weather_detailed_type;
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

    public int getGroup_code() {
        return group_code;
    }
    public void setGroup_code(int group_code) {
        this.group_code = group_code;
    }

    public String getWeather_detailed_type() {
        return weather_detailed_type;
    }
    public void setWeather_detailed_type(String weather_detailed_type) {
        this.weather_detailed_type = weather_detailed_type;
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", temperature=" + temperature +
                ", weather_type='" + weather_type + '\'' +
                ", humidity=" + humidity +
                ", wind_speed=" + wind_speed +
                ", pressure=" + pressure +
                ", wind_direction=" + wind_direction +
                ", date=" + date +
                '}';
    }

    public static int kelvinToCelsius(double kelvinDegrees) {
        return (int)kelvinDegrees - KELVIN_TO_CELSIUS;
    }
}
