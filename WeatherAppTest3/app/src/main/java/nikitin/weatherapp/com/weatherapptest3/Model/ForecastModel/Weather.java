package nikitin.weatherapp.com.weatherapptest3.Model.ForecastModel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Влад on 26.10.2016.
 */
public class Weather {
    @SerializedName("id")
    int id;
    @SerializedName("main")
    String main;
    @SerializedName("description")
    String description;
    @SerializedName("icon")
    String icon;

    Weather(int id, String main, String description, String icon) {
        this.id = id;
        this.main = main;
        this.description = description;
        this.icon = icon;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getMain() {
        return main;
    }
    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
}
