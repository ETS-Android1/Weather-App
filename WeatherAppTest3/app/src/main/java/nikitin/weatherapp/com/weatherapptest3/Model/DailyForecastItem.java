package nikitin.weatherapp.com.weatherapptest3.Model;


import nikitin.weatherapp.com.weatherapptest3.Model.Database.Forecast;

/**
 * Created by Uladzislau_Nikitsin on 1/25/2017.
 */

public class DailyForecastItem {
    private Forecast forecast;
    private int kIndex;

    public DailyForecastItem(Forecast forecast, int kIndex) {
        this.forecast = forecast;
        this.kIndex = kIndex;
    }

    public Forecast getForecast() {
        return forecast;
    }
    public void setForecast(Forecast forecast) {
        this.forecast = forecast;
    }

    public int getkIndex() {
        return kIndex;
    }
    public void setkIndex(int kIndex) {
        this.kIndex = kIndex;
    }

    @Override
    public String toString() {
        return "DailyForecastItem{" +
                "forecast=" + forecast +
                ", kIndex=" + kIndex +
                '}';
    }
}
