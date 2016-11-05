package nikitin.weatherapp.com.weatherapptest3.Model;

import java.util.Date;

/**
 * Created by Влад on 22.10.2016.
 */
public class DailyForecastSimpleElement {
    private final Date date;
    private final String temperature;
    private final String weatherName;

    public DailyForecastSimpleElement(Date date, String temperature, String weatherName) {
        this.date = date;
        this.temperature = temperature;
        this.weatherName = weatherName;
    }

    public Date getDate() {
        return date;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getWeatherName() {
        return weatherName;
    }
}
