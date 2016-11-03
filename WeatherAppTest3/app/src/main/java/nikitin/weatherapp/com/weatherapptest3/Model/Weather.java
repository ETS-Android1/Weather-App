package nikitin.weatherapp.com.weatherapptest3.Model;

/**
 * Created by Влад on 22.08.2016.
 */
public class Weather {
    private int temperature;
    private String weatherName;
    private int pressure;
    private int windSpeed;
    private int humidity;

    public Weather() {
        this.temperature = 0;
        this.weatherName = "none";
        this.pressure = 0;
        this.windSpeed = 0;
        this.humidity = 0;
    }

    public Weather(int temperature, String weatherName, int pressure, int windSpeed, int humidity) {
        this.temperature = temperature;
        this.weatherName = weatherName;
        this.pressure = pressure;
        this.windSpeed = windSpeed;
        this.humidity = humidity;
    }

    @Override
    public String toString() {
        return "" +temperature +weatherName +pressure +windSpeed +humidity;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public String getWeatherName() {
        return weatherName;
    }

    public void setWeatherName(String weatherName) {
        this.weatherName = weatherName;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(int windSpeed) {
        this.windSpeed = windSpeed;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }
}
