package nikitin.weatherapp.com.weatherapptest3.Presenters;

import android.app.Activity;
import android.content.Context;

import nikitin.weatherapp.com.weatherapptest3.DatabaseHandler;
import nikitin.weatherapp.com.weatherapptest3.MainActivity;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.CurrentWeather;
import nikitin.weatherapp.com.weatherapptest3.View.CitiesFragment;
import nikitin.weatherapp.com.weatherapptest3.View.MainWindowFragment;
import nikitin.weatherapp.com.weatherapptest3.Model.WeatherModel.WeatherResponse;
import nikitin.weatherapp.com.weatherapptest3.rest.OpenWeatherMapAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Влад on 15.10.2016.
 */
public class MainWindowPresenter {
    static MainWindowFragment view;
    private static Context context;
    private static int activeCityId;
    private DatabaseHandler databaseHandler;

    public MainWindowPresenter(MainWindowFragment view) {
        databaseHandler = DatabaseHandler.getInstance(context);
        this.view = view;
        this.context = view.getContext();
    }
    public void updateWeather(final int activeCityId) {
        OpenWeatherMapAPI openWeatherMapAPI = OpenWeatherMapAPI.getInstance();
        openWeatherMapAPI.getWeatherByCityId(activeCityId, new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                CurrentWeather currentWeather = new CurrentWeather();
                currentWeather.setFk_city_id(activeCityId);
                currentWeather.setName(response.body().getName());
                currentWeather.setPressure(response.body().getData().getPressure());
                currentWeather.setHumidity(response.body().getData().getHumidity());
                currentWeather.setWind_speed(response.body().getWind().getSpeed());
                currentWeather.setTemp(response.body().getData().getTemp());
                currentWeather.setDate(response.body().getDt());

                databaseHandler.addWeather(currentWeather);


                response.body().setData(CitiesFragment.getInstance().getPresenter().convertToCelcium(response.body().getData()));
                view.applyWeather(response.body());
            }
            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                System.out.println("error");
                t.printStackTrace();
            }
        });
    }
}
