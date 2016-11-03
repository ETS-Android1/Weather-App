package nikitin.weatherapp.com.weatherapptest3.Model.WeatherModel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Влад on 04.10.2016.
 */
public class Clouds {
    @SerializedName("all")
    private int all;

    public Clouds(int all) {
        this.all = all;
    }

    public int getAll() {
        return all;
    }
    public void setAll(int all) {
        this.all = all;
    }
}
