package nikitin.weatherapp.com.weatherapptest3.Model.SpaceWeather;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Uladzislau_Nikitsin on 1/20/2017.
 */

public class GeomagneticStorm {
    @SerializedName("message")
    private String message;
    @SerializedName("product_id")
    private String date;

    GeomagneticStorm(String message, String date) {
        this.message = message;
        this.date = date;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "GeomagneticStorm{" +
                "message='" + message + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
