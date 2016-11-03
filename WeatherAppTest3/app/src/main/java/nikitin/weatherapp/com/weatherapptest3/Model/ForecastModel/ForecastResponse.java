package nikitin.weatherapp.com.weatherapptest3.Model.ForecastModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Влад on 26.10.2016.
 */
public class ForecastResponse {
    @SerializedName("city")
    City city;
    @SerializedName("cod")
    String cod;
    @SerializedName("message")
    double message;
    @SerializedName("cnt")
    int cnt;
    @SerializedName("list")
    List<ForecastWeather> list;

    ForecastResponse(City city, String cod, double message, int cnt, List <ForecastWeather> list) {
        this.city = city;
        this.message = message;
        this.cnt = cnt;
        this.list = list;
    }

    public City getCity() {
        return city;
    }
    public void setCity(City city) {
        this.city = city;
    }

    public String getCod() {
        return cod;
    }
    public void setCod(String cod) {
        this.cod = cod;
    }

    public double getMessage() {
        return message;
    }
    public void setMessage(double message) {
        this.message = message;
    }

    public int getCnt() {
        return cnt;
    }
    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public List<ForecastWeather> getList() {
        return list;
    }
    public void setList(List<ForecastWeather> list) {
        this.list = list;
    }
}
