package nikitin.weatherapp.com.weatherapptest3.Presenters;

import java.util.List;

import nikitin.weatherapp.com.weatherapptest3.Model.SpaceWeather.GeomagneticStorm;
import nikitin.weatherapp.com.weatherapptest3.View.ExtremeWeatherFragment;
import nikitin.weatherapp.com.weatherapptest3.rest.ApiClient;
import nikitin.weatherapp.com.weatherapptest3.rest.OpenWeatherMapAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Uladzislau_Nikitsin on 1/20/2017.
 */

public class ExtremeWeatherPresenter {
    ExtremeWeatherFragment view;
    public ExtremeWeatherPresenter(ExtremeWeatherFragment view) {
        this.view = view;
    }

    public void getSpaceWeatherData() {
        OpenWeatherMapAPI openWeatherMapAPI = OpenWeatherMapAPI.getNewInstance(ApiClient.URL_SPACE_WEATHER);
        openWeatherMapAPI.getGeomagneticStormData(new Callback<List<GeomagneticStorm>>() {
            @Override
            public void onResponse(Call<List<GeomagneticStorm>> call, Response<List<GeomagneticStorm>> response) {
                view.setGeomagneticBox(response.body().get(0));
            }

            @Override
            public void onFailure(Call<List<GeomagneticStorm>> call, Throwable t) {
            }
        });
    }



}
