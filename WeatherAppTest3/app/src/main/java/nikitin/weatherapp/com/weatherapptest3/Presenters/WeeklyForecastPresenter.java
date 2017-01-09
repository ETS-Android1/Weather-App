package nikitin.weatherapp.com.weatherapptest3.Presenters;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import nikitin.weatherapp.com.weatherapptest3.Model.Database.DailyForecast;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.WeeklyForecast;
import nikitin.weatherapp.com.weatherapptest3.Model.ForecastModel.ForecastResponse;
import nikitin.weatherapp.com.weatherapptest3.Model.ForecastModel.ForecastWeather;
import nikitin.weatherapp.com.weatherapptest3.Model.WeatherModel.Data;
import nikitin.weatherapp.com.weatherapptest3.View.WeeklyForecastFragment;
import nikitin.weatherapp.com.weatherapptest3.rest.OpenWeatherMapAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Uladzislau_Nikitsin on 1/6/2017.
 */

public class WeeklyForecastPresenter {
    WeeklyForecastFragment fragment;
    public WeeklyForecastPresenter(WeeklyForecastFragment fragment) {
        this.fragment = fragment;
    }

    public void getWeeklyForecast (int activeCityId) {
        System.out.println("ACTIVE CITY ID " +activeCityId);
        OpenWeatherMapAPI openWeatherMapAPI = OpenWeatherMapAPI.getInstance();
        openWeatherMapAPI.getWeeklyForecastByCityId(activeCityId, new Callback<ForecastResponse>() {
            @Override
            public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {
                int segmentsPerDay = 8;
                int a = 0;
                int b = response.body().getList().size() % segmentsPerDay;
                ArrayList<WeeklyForecast> list = new ArrayList<WeeklyForecast>(5);
                for (int i = 0; i < 5; i ++) {
                    System.out.println("pish");
                    list.add(findDayDataForWeeklyForecast(response.body().getList().subList(a, b)));
                    a = b;
                    b += segmentsPerDay;
                }
                fragment.updateWeeklyForecastList(list);
            }

            @Override
            public void onFailure(Call<ForecastResponse> call, Throwable t) {
            }
        });
    }

    private WeeklyForecast findDayDataForWeeklyForecast  (List<ForecastWeather> dayForecast) {
        System.out.println(dayForecast.toString());
        String dayName = findDayByDate(dayForecast.get(0).getDt());
        String weatherName = "zaglyshka";
        double maxTemp = dayForecast.get(0).getData().getTemp();
        double minTemp = dayForecast.get(0).getData().getTemp();
        for (ForecastWeather forecast : dayForecast) {
            if (forecast.getData().getTemp() > maxTemp) maxTemp = forecast.getData().getTemp();
            if (forecast.getData().getTemp() < minTemp) minTemp = forecast.getData().getTemp();
        }
        return new WeeklyForecast(dayName, weatherName, convertToCelcium(maxTemp), convertToCelcium(minTemp));
    }

    private String findDayByDate(int dt) {
        Calendar calendar = Calendar.getInstance();
        System.out.println("dt is " +dt);
        calendar.setTime(new Timestamp(dt * 1000));
        System.out.println("day " +calendar.get(Calendar.DAY_OF_WEEK));
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY: return "Moday";
            case Calendar.TUESDAY: return "Tuesday";
            case Calendar.WEDNESDAY: return "Wednesday";
            case Calendar.THURSDAY: return "Thursday";
            case Calendar.FRIDAY: return "Friday";
            case Calendar.SATURDAY: return "Saturday";
            case Calendar.SUNDAY: return "Sunday";
        }
        return "error";
    }

    public int convertToCelcium (double temp) {
        double KELVIN_TO_CELCIUM = 273.0;
        return (int) Math.round(temp - KELVIN_TO_CELCIUM);
    }
}
