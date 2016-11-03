package nikitin.weatherapp.com.weatherapptest3.Model;

import java.util.Date;

/**
 * Created by Влад on 22.10.2016.
 */
public class DailyForecastSimpleElement {
    private Date date;
    private double temperature;

    public DailyForecastSimpleElement(Date date, double temperature) {
        this.date = date;
        this.temperature = temperature;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public double getTemperature() {
        return temperature;
    }
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}
