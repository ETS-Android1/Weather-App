package nikitin.weatherapp.com.weatherapptest3.Model.Database;

/**
 * Created by Влад on 30.11.2016.
 */
public class CurrentWeather {
    private int id;
    private int fk_city_id;
    private String name;
    private double temp;
    private double humidity;
    private double wind_speed;
    private double pressure;
    private int date;

    public CurrentWeather() {}
    public CurrentWeather(int id, int fk_city_id, String name, double temp, double humidity, double wind_speed, double pressure, int date) {
        this.id = id;
        this.fk_city_id = fk_city_id;
        this.name = name;
        this.temp = temp;
        this.humidity = humidity;
        this.wind_speed = wind_speed;
        this.pressure = pressure;
        this.date = date;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getFk_city_id() {
        return fk_city_id;
    }
    public void setFk_city_id(int fk_city_id) {
        this.fk_city_id = fk_city_id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public double getTemp() {
        return temp;
    }
    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getHumidity() {
        return humidity;
    }
    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getWind_speed() {
        return wind_speed;
    }
    public void setWind_speed(double wind_speed) {
        this.wind_speed = wind_speed;
    }

    public double getPressure() {
        return pressure;
    }
    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public int getDate() {
        return date;
    }
    public void setDate(int date) {
        this.date = date;
    }
    @Override
    public String toString() {
        return "CurrentWeather{" + "id=" + id + ", fk_city_id=" + fk_city_id + ", name=" + name +
                ", temp=" + temp + ", humidity=" + humidity + ", wind_speed=" + wind_speed +
                ", pressure=" + pressure + ", date=" + date + '}';
    }
}
