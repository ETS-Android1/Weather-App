package nikitin.weatherapp.com.weatherapptest3.Model;

/**
 * Created by Влад on 28.07.2016.
 */
public class City {
    private String cityName;
    private int cityId;
    private String country;
    private boolean active;
    private Weather weather;

    public City() {
    }

    public City(String cityName, int cityId, String country, boolean active, Weather weather) {
        this.cityName = cityName;
        this.cityId = cityId;
        this.country = country;
        this.active = active;
        this.weather = weather;
    }

    @Override
    public  String toString() {return cityName + " " +country;}
    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }
}
