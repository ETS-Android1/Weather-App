package nikitin.weatherapp.com.weatherapptest3.rest;

import java.util.List;

import nikitin.weatherapp.com.weatherapptest3.Model.SpaceWeather.GeomagneticResponse;
import nikitin.weatherapp.com.weatherapptest3.Model.SpaceWeather.GeomagneticStorm;
import nikitin.weatherapp.com.weatherapptest3.Model.WeatherModel.FindCityResponse;
import nikitin.weatherapp.com.weatherapptest3.Model.ForecastModel.ForecastResponse;
import nikitin.weatherapp.com.weatherapptest3.Model.WeatherModel.WeatherResponse;
import okhttp3.ResponseBody;
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
    Call<WeatherResponse> getWeatherByCityId(@Query("id") long id, @Query("appId") String appId);
    @GET("find")
    Call<FindCityResponse> findCity(@Query("q") String cityName, @Query("type") String findType, @Query("appId") String appId);
    @GET("weather")
    Call<WeatherResponse> getWeatherByCityCoordinates(@Query("lat") double latitude, @Query("lon") double longtitude, @Query("appId") String appId);
    @GET("forecast")
    Call<ForecastResponse> getForecastByCityId(@Query("id") long cityId, @Query("cnt") int count, @Query("appId") String appId);
    @GET("forecast")
    Call<ForecastResponse> getForecastByCityName(@Query("q") String cityName, @Query("appId") String appId);
    @GET("forecast")
    Call<ForecastResponse> getWeeklyForecastByCityId(@Query("id") long cityId, @Query("appId") String appId);
    @GET("forecast")
    Call<ForecastResponse> getForecast(@Query("id") long cityId, @Query("appId") String appId);
    @GET("text/3-day-geomag-forecast.txt")
    Call<ResponseBody> getGeomagneticStorm();

}
