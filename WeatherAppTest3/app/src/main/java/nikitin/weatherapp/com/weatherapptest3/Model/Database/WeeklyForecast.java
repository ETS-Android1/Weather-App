package nikitin.weatherapp.com.weatherapptest3.Model.Database;

/**
 * Created by Uladzislau_Nikitsin on 1/6/2017.
 */

public class WeeklyForecast {
    private String dayName;
    private String weatherName;
    private double dayTemp;
    private double nightTemp;

    public WeeklyForecast() {
        this.dayName = "none";
        this.weatherName = "none";
        this.dayTemp = 0;
        this.nightTemp = 0;
    }

    public WeeklyForecast(String dayName, String weatherName, double dayTemp, double nightTemp){
        this.dayName = dayName;
        this.weatherName = weatherName;
        this.dayTemp = dayTemp;
        this.nightTemp = nightTemp;
    }

    public String getDayName() {
        return dayName;
    }
    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public String getWeatherName() {
        return weatherName;
    }
    public void setWeatherName(String weatherName) {
        this.weatherName = weatherName;
    }

    public double getDayTemp() {
        return dayTemp;
    }
    public void setDayTemp(double dayTemp) {
        this.dayTemp = dayTemp;
    }

    public double getNightTemp() {
        return nightTemp;
    }
    public void setNightTemp(double nightTemp) {
        this.nightTemp = nightTemp;
    }
}
