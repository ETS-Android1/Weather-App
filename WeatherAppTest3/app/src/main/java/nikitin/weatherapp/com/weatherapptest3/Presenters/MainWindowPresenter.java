package nikitin.weatherapp.com.weatherapptest3.Presenters;

import android.content.Context;
import android.os.AsyncTask;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import nikitin.weatherapp.com.weatherapptest3.DatabaseHandler;
import nikitin.weatherapp.com.weatherapptest3.MainActivity;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.CurrentWeather;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.DailyForecast;
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
    int weatherId;

    public MainWindowPresenter(MainWindowFragment view) {
        databaseHandler = DatabaseHandler.getInstance(MainActivity.getAppContext());
        this.view = view;
        this.context = view.getContext();
    }
    public void getWeatherData(final long activeCityId) {
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

                weatherId = databaseHandler.findWeatherIdByCityId(cityId);
                if (weatherId != -1) {
                    System.out.println("WEATHER update");
                    new updateWeatherTask().execute(currentWeather);
                } else {
                    System.out.println("WEATHER add");
                    new addWeatherTask().execute(currentWeather);
                }
                response.body().setData(CitiesFragment.getInstance().getPresenter().convertToCelsius(response.body().getData()));
                view.applyWeather(currentWeather);
                view.applyApparentTemperature(calculateApparentTemperature(currentWeather.getTemp(), currentWeather.getHumidity(), currentWeather.getWind_speed()));
            }
            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                int cityId = databaseHandler.getCityIdByOw_id(activeCityId);
                CurrentWeather weather = databaseHandler.getWeatherByCityId(cityId);
                view.applyWeather(weather);
                view.applyApparentTemperature(calculateApparentTemperature(weather.getTemp(), weather.getHumidity(), weather.getWind_speed()));
            }
        });
    }

    public double convertToCelsius(double temp) {
        double KELVIN_TO_CELCIUM = 273.0;
        double roundedTemp = new BigDecimal(temp-KELVIN_TO_CELCIUM).setScale(2, RoundingMode.UP).doubleValue(); //double)((int)Math.round(data.getTemp()*10)/10);
        return roundedTemp;
    }
    private double convertTempCtoF(double tempCelsius) {
        return tempCelsius * 1.8 + 32;
    }
    private double convertTempFtoC(double tempFahrenheit) {
        return (tempFahrenheit - 32) / 1.8;
    }
    private double convertWindSpeedMStoMPH(double windSpeedMS) {
        return windSpeedMS * 2.2369362920544;
    }
    private double calculateWindChillDegree(double tempCelsius, double windSpeedMS) {
        double tempFahrenheit = convertTempCtoF(tempCelsius);
        double windSpeedMPH = convertWindSpeedMStoMPH(windSpeedMS);
        return convertTempFtoC(35.74 + 0.6215 * tempFahrenheit - 35.75 * Math.pow(windSpeedMPH, 0.16) +
                0.4275 * tempFahrenheit * Math.pow(windSpeedMPH, 0.16));
    }
    private double calculateHeatIndexDegree(double tempCelsius, double humidity) {
        double c1 = -42.379, c2 = 2.04901523, c3 = 10.14333127, c4 = -0.22475541,
                c5 = -6.83783 * Math.pow(10, -1), c6 = -5.481717 * Math.pow(10, -2),
                c7 = 1.22874 * Math.pow(10, -3), c8 = 8.5282 * Math.pow(10, -4),
                c9 = -1.99 * Math.pow(10, -6);
        double tempFahrenheit = convertTempCtoF(tempCelsius);
        return convertTempFtoC(c1 + c2*tempFahrenheit + c3*humidity + c4*humidity*tempFahrenheit +
                c5 * tempFahrenheit*tempFahrenheit + c6*humidity*humidity +
                c7*tempFahrenheit*tempFahrenheit*humidity + c8*tempFahrenheit*humidity*humidity +
                c9*tempFahrenheit*tempFahrenheit*humidity*humidity);
    }
    private double calculateApparentTemperature(double tempCelsius, double humidity, double windSpeed) {
        if (tempCelsius < 10) {return calculateWindChillDegree(tempCelsius, windSpeed);}
            else if (tempCelsius > 27) {return calculateHeatIndexDegree(tempCelsius, humidity);}
            else return tempCelsius;
    }


    class addWeatherTask extends AsyncTask<CurrentWeather,Void,Void> {
        @Override
        protected Void doInBackground(CurrentWeather... weathers) {
            databaseHandler.addWeather(weathers[0]);
            return null;
        }
    }

    class updateWeatherTask extends AsyncTask<CurrentWeather,Void,Void> {
        @Override
        protected Void doInBackground(CurrentWeather... weathers) {
            databaseHandler.updateWeather(weathers[0], weatherId);
            return null;
        }
    }
}
