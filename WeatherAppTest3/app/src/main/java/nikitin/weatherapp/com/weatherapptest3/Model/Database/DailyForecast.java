package nikitin.weatherapp.com.weatherapptest3.Model.Database;

import nikitin.weatherapp.com.weatherapptest3.Model.ForecastModel.ForecastWeather;

/**
 * Created by Влад on 28.12.2016.
 */
public class DailyForecast {
    private int id;
    private int fk_city_ow_id;
    private int date;
    private String weather_name;
    private int temperature;
    private int humidity;
    private int wind_speed;
    private int pressure;
    private int wind_direction;

    public DailyForecast(int id, int fk_city_ow_id, int date, String weather_name, int temperature, int humidity, int wind_speed, int pressure, int wind_direction) {
        this.id = id;
        this.fk_city_ow_id = fk_city_ow_id;
        this.date = date;
        this.weather_name = weather_name;
        this.temperature = temperature;
        this.humidity = humidity;
        this.wind_speed = wind_speed;
        this.wind_direction = wind_direction;
        this.pressure = pressure;
    }

    public DailyForecast(ForecastWeather weather, int fk_city_ow_id) {
        this.fk_city_ow_id = fk_city_ow_id;
        this.date = weather.getDt();
        this.weather_name = weather.getWeathers().get(0).getMain();
        this.temperature = (int)weather.getData().getTemp();
        this.humidity = weather.getData().getHumidity();
        this.wind_speed = (int)weather.getWind().getSpeed();
        this.pressure = (int)weather.getData().getPressure();
        this.wind_direction = (int)weather.getWind().getDeg();
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getFk_city_ow_id() {
        return fk_city_ow_id;
    }
    public void setFk_city_ow_id(int fk_city_ow_id) {
        this.fk_city_ow_id = fk_city_ow_id;
    }

    public int getDate() {
        return date;
    }
    public void setDate(int date) {
        this.date = date;
    }

    public String getWeather_name() {
        return weather_name;
    }
    public void setWeather_name(String weather_name) {
        this.weather_name = weather_name;
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

    public int getWind_speed() {
        return wind_speed;
    }
    public void setWind_speed(int wind_speed) {
        this.wind_speed = wind_speed;
    }

    public int getWind_direction() {
        return wind_direction;
    }
    public void setWind_direction(int wind_direction) {
        this.wind_direction = wind_direction;
    }

    public int getPressure() {
        return pressure;
    }
    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    @Override
    public String toString() {
        return "DailyForecast{" +
                "id=" + id +
                ", fk_city_ow_id=" + fk_city_ow_id +
                ", date=" + date +
                ", weather_name='" + weather_name + '\'' +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", wind_speed=" + wind_speed +
                ", pressure=" + pressure +
                ", wind_direction=" + wind_direction +
                '}';
    }
}
