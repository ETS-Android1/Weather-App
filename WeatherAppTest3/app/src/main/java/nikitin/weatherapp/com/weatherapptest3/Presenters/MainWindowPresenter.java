package nikitin.weatherapp.com.weatherapptest3.Presenters;

import android.content.Context;

import java.math.BigDecimal;
import java.math.RoundingMode;

import nikitin.weatherapp.com.weatherapptest3.DatabaseHandler;
import nikitin.weatherapp.com.weatherapptest3.MainActivity;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.CurrentWeather;
import nikitin.weatherapp.com.weatherapptest3.Model.WeatherModel.Data;
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
    //private static int activeCityId;
    private DatabaseHandler databaseHandler;

    public MainWindowPresenter(MainWindowFragment view) {
        databaseHandler = DatabaseHandler.getInstance(MainActivity.getAppContext());
        this.view = view;
        this.context = view.getContext();
    }
    public void getWeatherData(final int activeCityId) {
        OpenWeatherMapAPI openWeatherMapAPI = OpenWeatherMapAPI.getInstance();
        openWeatherMapAPI.getWeatherByCityId(activeCityId, new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {

                System.out.println("ID_OW = " +activeCityId);
                CurrentWeather currentWeather = new CurrentWeather();
                int cityId = databaseHandler.getCityIdByOw_id(activeCityId);
                currentWeather.setFk_city_id(cityId);
                currentWeather.setName(response.body().getName());
                currentWeather.setPressure(response.body().getData().getPressure());
                currentWeather.setHumidity(response.body().getData().getHumidity());
                currentWeather.setWind_speed(response.body().getWind().getSpeed());
                currentWeather.setTemp(convertToCelsius(response.body().getData().getTemp()));
                currentWeather.setDate(response.body().getDt());

                int weatherId = databaseHandler.findWeatherIdByCityId(cityId);
                if (weatherId != -1) {
                    System.out.println("WEATHER update");
                    databaseHandler.updateWeather(currentWeather, weatherId);
                } else {
                    System.out.println("WEATHER add");
                    databaseHandler.addWeather(currentWeather);
                }
                response.body().setData(CitiesFragment.getInstance().getPresenter().convertToCelsius(response.body().getData()));
                view.applyWeather(response.body());
            }
            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                int cityId = databaseHandler.getCityIdByOw_id(activeCityId);
                CurrentWeather weather = databaseHandler.getWeatherByCityId(cityId);
                view.applyWeather(weather);
            }
        });
    }

    public double convertToCelsius(double temp) {
        double KELVIN_TO_CELCIUM = 273.0;
        double roundedTemp = new BigDecimal(temp-KELVIN_TO_CELCIUM).setScale(2, RoundingMode.UP).doubleValue(); //double)((int)Math.round(data.getTemp()*10)/10);
        return roundedTemp;
    }
}
