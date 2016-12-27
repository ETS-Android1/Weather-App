package nikitin.weatherapp.com.weatherapptest3.Model.ForecastModel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import nikitin.weatherapp.com.weatherapptest3.Model.WeatherModel.Clouds;
import nikitin.weatherapp.com.weatherapptest3.Model.WeatherModel.Data;
import nikitin.weatherapp.com.weatherapptest3.Model.WeatherModel.Sys;
import nikitin.weatherapp.com.weatherapptest3.Model.WeatherModel.Wind;

/**
 * Created by Влад on 26.10.2016.
 */
public class ForecastWeather implements Serializable{
    @SerializedName("dt")
    int dt;
    @SerializedName("sys")
    Sys sys;
    @SerializedName("clouds")
    Clouds clouds;
    @SerializedName("wind")
    Wind wind;
    @SerializedName("weather")
    ArrayList<Weather> weathers;
    @SerializedName("main")
    Data data;

    public ForecastWeather() {
        weathers = new ArrayList<>();
        weathers.add(new Weather());
        dt = 0;
        sys = new Sys();
        clouds = new Clouds();
        wind = new Wind();
        data = new Data();
    }

    ForecastWeather(int dt, Sys sys, Clouds clouds, Wind wind, ArrayList<Weather> weathers, Data data) {
        this.dt = dt;
        this.sys = sys;
        this.clouds = clouds;
        this.wind = wind;
        this.weathers = weathers;
        this.data = data;
    }

    public int getDt() {
        return dt;
    }
    public void setDt(int dt) {
        this.dt = dt;
    }

    public Sys getSys() {
        return sys;
    }
    public void setSys(Sys sys) {
        this.sys = sys;
    }

    public Clouds getClouds() {
        return clouds;
    }
    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public Wind getWind() {
        return wind;
    }
    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public ArrayList<Weather> getWeathers() {
        return weathers;
    }
    public void setWeathers(ArrayList<Weather> weathers) {
        this.weathers = weathers;
    }

    public Data getData() {
        return data;
    }
    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ForecastWeather{" +
                "dt=" + dt +
                ", sys=" + sys +
                ", clouds=" + clouds +
                ", wind=" + wind +
                ", weathers=" + weathers +
                ", data=" + data +
                '}';
    }
}
