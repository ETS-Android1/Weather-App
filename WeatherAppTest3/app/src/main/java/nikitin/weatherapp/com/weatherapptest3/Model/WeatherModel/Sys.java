package nikitin.weatherapp.com.weatherapptest3.Model.WeatherModel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Влад on 16.10.2016.
 */
public class Sys implements Serializable{
    @SerializedName("message")
    private float message;
    @SerializedName("country")
    private String country;
    @SerializedName("sunrise")
    private int sunRise;
    @SerializedName("sunset")
    private int sunSet;
    public Sys() {
        message = 0;
        country = "";
        sunRise = 0;
        sunSet = 0;
    }
    public Sys(int message, String country, int sunRise, int sunSet) {
        this.message = message;
        this.country = country;
        this.sunRise = sunRise;
        this.sunSet = sunSet;
    }

    public float getMessage() {
        return message;
    }
    public void setMessage(float message) {
        this.message = message;
    }

    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }

    public int getSunRise() {
        return sunRise;
    }
    public void setSunRise(int sunRise) {
        this.sunRise = sunRise;
    }

    public int getSunSet() {
        return sunSet;
    }
    public void setSunSet(int sunSet) {
        this.sunSet = sunSet;
    }
}
