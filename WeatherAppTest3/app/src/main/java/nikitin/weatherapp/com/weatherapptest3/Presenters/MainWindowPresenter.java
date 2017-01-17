package nikitin.weatherapp.com.weatherapptest3.Presenters;

import android.content.Context;
import android.os.AsyncTask;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import nikitin.weatherapp.com.weatherapptest3.DatabaseHandler;
import nikitin.weatherapp.com.weatherapptest3.MainActivity;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.City;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.CurrentWeather;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.DailyForecast;
import nikitin.weatherapp.com.weatherapptest3.Model.WeatherModel.Data;
import nikitin.weatherapp.com.weatherapptest3.Model.WeatherModel.Weather;
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
    private DatabaseHandler databaseHandler;
    private City city = new City(0, "None", "", 0, 0, 0, "No city selected", 0, 0, 0, 0, 0, 0, "");

    public MainWindowPresenter(MainWindowFragment view) {
        databaseHandler = DatabaseHandler.getInstance(MainActivity.getAppContext());
        this.view = view;
        this.context = view.getContext();
    }


    public void setSelectedCity(City city) {
        this.city = city;
    }
    public City getSelectedCity() {
        return city;
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
    public double calculateApparentTemperature() {
        if (city.getTemperature() < 10) {
            return calculateWindChillDegree(city.getTemperature(), city.getWind_speed());
        } else if (city.getTemperature() > 27) {
            return calculateHeatIndexDegree(city.getTemperature(), city.getHumidity());
        } else return city.getTemperature();
    }

    public void onTemperatureBoxClicked() {

    }
}
