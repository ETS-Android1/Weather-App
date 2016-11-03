package nikitin.weatherapp.com.weatherapptest3.Model.WeatherModel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Влад on 06.10.2016.
 */
public class FindCityResponse {
    @SerializedName("count")
    int count;
    @SerializedName("list")
    List <WeatherResponse> weatherList = new ArrayList<>();

    public FindCityResponse(int count, List<WeatherResponse> weatherList) {
        this.count = count;
        this.weatherList = weatherList;
    }
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }

    public List<WeatherResponse> getWeatherList() {
        return weatherList;
    }
    public void setWeatherList(List<WeatherResponse> weatherList) {
        this.weatherList = weatherList;
    }
}
