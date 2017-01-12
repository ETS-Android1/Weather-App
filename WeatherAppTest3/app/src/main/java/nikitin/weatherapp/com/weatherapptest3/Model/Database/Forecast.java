package nikitin.weatherapp.com.weatherapptest3.Model.Database;

/**
 * Created by Uladzislau_Nikitsin on 1/12/2017.
 */

public class Forecast {
    private int id;
    private int fkCityId;
    private String weatherType;
    private int temperature;
    private int humidity;
    private double wind_speed;
    private int pressure;
    private int wind_direction;
    private int date;

    public Forecast(int id, int fkCityId, String weatherType, int temperature, int humidity,
                    double wind_speed, int pressure, int wind_direction, int date) {
        this.id = id;
        this.fkCityId = fkCityId;
        this.weatherType = weatherType;
        this.temperature = temperature;
        this.humidity = humidity;
        this.wind_speed = wind_speed;
        this.pressure = pressure;
        this.wind_direction = wind_direction;
        this.date = date;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getFkCityId() {
        return fkCityId;
    }
    public void setFkCityId(int fkCityId) {
        this.fkCityId = fkCityId;
    }

    public String getWeatherType() {
        return weatherType;
    }
    public void setWeatherType(String weatherType) {
        this.weatherType = weatherType;
    }

    public int getTemperature() {
        return temperature;
    }
    public void setTemperature(int temperature) {
        this.temperature = temperature;
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
}
