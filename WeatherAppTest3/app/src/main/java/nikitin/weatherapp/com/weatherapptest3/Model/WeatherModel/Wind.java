package nikitin.weatherapp.com.weatherapptest3.Model.WeatherModel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Влад on 04.10.2016.
 */
public class Wind implements Serializable{
    @SerializedName("speed")
    private double speed;
    @SerializedName("deg")
    private double deg;

    public Wind() {
        speed = 0;
        deg = 0;
    }

    public Wind (double speed, int deg) {
        this.speed = speed;
        this.deg = deg;
    }

    public double getSpeed() {
        return speed;
    }
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getDeg() {
        return deg;
    }
    public void setDeg(double deg) {
        this.deg = deg;
    }
}
