package nikitin.weatherapp.com.weatherapptest3.rest;

import android.util.Log;

import java.io.IOException;
import java.util.List;

import nikitin.weatherapp.com.weatherapptest3.Model.SpaceWeather.GeomagneticResponse;
import nikitin.weatherapp.com.weatherapptest3.Model.SpaceWeather.GeomagneticStorm;
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

    OpenWeatherMapAPI(String base_url) {
        api = ApiClient.getClient(base_url).create(APIWeatherInterface.class);
    }

    public static OpenWeatherMapAPI getNewInstance(String base_url) {
        return new OpenWeatherMapAPI(base_url);
    }

    public void getWeatherByCityId(long cityId, Callback<WeatherResponse> callback) {
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

    public void getDailyForecastByCityId(long cityId, Callback<ForecastResponse> callback) {
        int hoursSectionCount = 9;
        Call<ForecastResponse> call = api.getForecastByCityId(cityId, hoursSectionCount, API_KEY);
        call.enqueue(callback);
    }

    public void getWeeklyForecastByCityId(long cityId, Callback<ForecastResponse> callback) {
        Call<ForecastResponse> call = api.getWeeklyForecastByCityId(cityId, API_KEY);
        call.enqueue(callback);
    }

    public void getForecast(long cityId, Callback<ForecastResponse> callback) {
        Call<ForecastResponse> call = api.getForecast(cityId, API_KEY);
        call.enqueue(callback);
    }

    public void getGeomagneticStormData(Callback<List<GeomagneticStorm>> callback) {
        Call<List<GeomagneticStorm>> call = api.getGeomagneticStorm();
        call.enqueue(callback);
    }


//
//    public void getWeaklyForecastByCityId(int cityId, Callback<ForecastResponse> callback) {
//        int hoursSection = 9;
//        Call<ForecastResponse> call = api.getForecastByCityId(cityId, hoursSection, API_KEY);
//        call.enqueue(callback);
//    }
}
