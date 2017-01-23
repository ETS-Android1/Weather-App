package nikitin.weatherapp.com.weatherapptest3.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Влад on 30.09.2016.
 */
public class ApiClient {
    public enum Urls {OPENWEATHER, SPACEWEATHER}
    public static final String URL_OPEN_WEATHER = "http://api.openweathermap.org/data/2.5/";
    public static final String URL_SPACE_WEATHER = "http://services.swpc.noaa.gov/";

    public static Retrofit getClient(ApiClient.Urls stringUrl) {
        String base_url = "";
        if (stringUrl == Urls.OPENWEATHER) {
            base_url = URL_OPEN_WEATHER;
        } else if (stringUrl == Urls.SPACEWEATHER) {
            base_url = URL_SPACE_WEATHER;
        }

        return new Retrofit.Builder()
                    .baseUrl(base_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
    }
}