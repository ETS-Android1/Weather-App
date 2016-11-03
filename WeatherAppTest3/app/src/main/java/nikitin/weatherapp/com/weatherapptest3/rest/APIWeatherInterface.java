package nikitin.weatherapp.com.weatherapptest3.rest;

import nikitin.weatherapp.com.weatherapptest3.Model.WeatherModel.FindCityResponse;
import nikitin.weatherapp.com.weatherapptest3.Model.ForecastModel.ForecastResponse;
import nikitin.weatherapp.com.weatherapptest3.Model.WeatherModel.WeatherResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Влад on 01.10.2016.
 */
public interface APIWeatherInterface {
    @GET("weather")
    Call<WeatherResponse> getWeatherByCityName(@Query("q") String cityName, @Query("appId") String appId);
    @GET("weather")
    Call<WeatherResponse> getWeatherByCityId(@Query("id") int id, @Query("appId") String appId);
    @GET("find")
    Call<FindCityResponse> findCity(@Query("q") String cityName, @Query("type") String findType, @Query("appId") String appId);
    @GET("weather")
    Call<WeatherResponse> getWeatherByCityCoordinates(@Query("lat") double latitude, @Query("lon") double longtitude, @Query("appId") String appId);
    @GET("forecast")
    Call<ForecastResponse> getForecastByCityId(@Query("id") int cityId, @Query("appId") String appId);
    @GET("forecast")
    Call<ForecastResponse> getForecastByCityName(@Query("q") String cityName, @Query("appId") String appId);
}
