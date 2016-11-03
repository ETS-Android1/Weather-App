package nikitin.weatherapp.com.weatherapptest3.Model;

import java.util.Date;

/**
 * Created by Влад on 22.10.2016.
 */
public class DailyForecastSimpleElement {
    private Date date;
    private String temperature;

    public DailyForecastSimpleElement(Date date, String temperature) {
        this.date = date;
        this.temperature = temperature;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public String getTemperature() {
        return temperature;
    }
    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
}
