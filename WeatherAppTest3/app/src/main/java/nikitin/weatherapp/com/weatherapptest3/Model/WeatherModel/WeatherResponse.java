package nikitin.weatherapp.com.weatherapptest3.Model.WeatherModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Влад on 04.10.2016.
 */
public class WeatherResponse {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("cod")
    private int cod;
    @SerializedName("coord")
    private Coordinates coordinates;
    @SerializedName("weather")
    private List<Weather> weathers;
    @SerializedName("main")
    private Data data;
    @SerializedName("wind")
    private Wind wind;
    @SerializedName("clouds")
    private Clouds clouds;
    @SerializedName("sys")
    private Sys sys;

    public WeatherResponse (int id, String name, Coordinates coordinates, List<Weather> weathers, Data data, Wind wind, Clouds clouds, int cod, Sys sys) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.weathers = weathers;
        this.data = data;
        this.wind = wind;
        this.clouds = clouds;
        this.cod = cod;
        this.sys = sys;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }
    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public List<Weather> getWeathers() {
        return weathers;
    }
    public void setWeathers(List<Weather> weathers) {
        this.weathers = weathers;
    }

    public Data getData() {
        return data;
    }
    public void setData(Data data) {
        this.data = data;
    }

    public Wind getWind() {
        return wind;
    }
    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Clouds getClouds() {
        return clouds;
    }
    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getCod() {
        return cod;
    }
    public void setCod(int cod) {
        this.cod = cod;
    }

    public Sys getSys() {
        return sys;
    }
    public void setSys(Sys sys) {
        this.sys = sys;
    }
}
