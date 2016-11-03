package nikitin.weatherapp.com.weatherapptest3.rest;

import android.util.Log;

import java.io.IOException;

import nikitin.weatherapp.com.weatherapptest3.Model.WeatherModel.FindCityResponse;
import nikitin.weatherapp.com.weatherapptest3.Model.ForecastModel.ForecastResponse;
import nikitin.weatherapp.com.weatherapptest3.Model.WeatherModel.WeatherResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Влад on 09.10.2016.
 */
public class OpenWeatherMapAPI {
    private static OpenWeatherMapAPI openWeatherMapAPI;
    private APIWeatherInterface api;

    private final String API_KEY = "485400ed5d502b7b04378efed428665b";
    private final String TAG = "OpenWeatherMapAPI";

    OpenWeatherMapAPI() {
        api = ApiClient.getClient().create(APIWeatherInterface.class);
    }

    public static OpenWeatherMapAPI getInstance() {
        if (openWeatherMapAPI == null) {
            openWeatherMapAPI = new OpenWeatherMapAPI();
        }
        return openWeatherMapAPI;
    }

    public void getWeatherByCityId(int cityId, Callback<WeatherResponse> callback) {
        Call<WeatherResponse> call = api.getWeatherByCityId(cityId, API_KEY);
        call.enqueue(callback);
    }

    public void getWeatherByCityCoordinate(double latitude, double longtitude, Callback<WeatherResponse> callback) {
        Call<WeatherResponse> call = api.getWeatherByCityCoordinates(latitude, longtitude, API_KEY);
        call.enqueue(callback);
    }

    public Response<FindCityResponse> findCity(String query) {
        final String SEARCH_TYPE = "like";
        try {
            Call<FindCityResponse> call = api.findCity(query, SEARCH_TYPE, API_KEY);
            return call.execute();
        } catch (IOException ex) {
            Log.d(TAG,ex.getMessage());
        }
        return null;
    }

    public void getForecastByCityId(int cityId, Callback<ForecastResponse> callback) {
        Call<ForecastResponse> call = api.getForecastByCityId(cityId, API_KEY);
        call.enqueue(callback);
    }
}
