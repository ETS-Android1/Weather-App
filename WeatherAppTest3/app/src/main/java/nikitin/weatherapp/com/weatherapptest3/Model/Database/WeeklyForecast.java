package nikitin.weatherapp.com.weatherapptest3.Model.Database;

/**
 * Created by Uladzislau_Nikitsin on 1/6/2017.
 */

public class WeeklyForecast {
    private String dayName;
    private String weatherName;
    private int dayTemp;
    private int nightTemp;

    public WeeklyForecast() {
        this.dayName = "none";
        this.weatherName = "none";
        this.dayTemp = 0;
        this.nightTemp = 0;
    }

    public WeeklyForecast(String dayName, String weatherName, int dayTemp, int nightTemp){
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

    public int getDayTemp() {
        return dayTemp;
    }
    public void setDayTemp(int dayTemp) {
        this.dayTemp = dayTemp;
    }

    public int getNightTemp() {
        return nightTemp;
    }
    public void setNightTemp(int nightTemp) {
        this.nightTemp = nightTemp;
    }
}
