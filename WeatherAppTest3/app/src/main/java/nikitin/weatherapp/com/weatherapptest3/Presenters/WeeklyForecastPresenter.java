package nikitin.weatherapp.com.weatherapptest3.Presenters;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import nikitin.weatherapp.com.weatherapptest3.Model.Database.DailyForecast;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.WeeklyForecast;
import nikitin.weatherapp.com.weatherapptest3.Model.ForecastModel.ForecastResponse;
import nikitin.weatherapp.com.weatherapptest3.Model.ForecastModel.ForecastWeather;
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

    private int pop (int activeCityId) {
        OpenWeatherMapAPI openWeatherMapAPI = OpenWeatherMapAPI.getInstance();
        openWeatherMapAPI.getWeeklyForecastByCityId(activeCityId, new Callback<ForecastResponse>() {
            @Override
            public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {
                int segmentCount = response.body().getCnt();
                int segmentsPerDay = 8;
                int todaySegmentCount = segmentCount % 8;
                int notFirstDay = 0;
                int dayPassed = 0;

                ArrayList<ForecastWeather> list = new ArrayList<ForecastWeather>();

                //Тут должен быть код, но пока его нет.
            }

            @Override
            public void onFailure(Call<ForecastResponse> call, Throwable t) {

            }
        });
    }

    private WeeklyForecast findDayDataForWeeklyForecast  (ArrayList<ForecastWeather> dayForecast) {
        String dayName = findDayByDate(dayForecast.get(0).getDt());
        String weatherName = "zaglyshka";
        double maxTemp = dayForecast.get(0).getData().getTemp();
        double minTemp = dayForecast.get(0).getData().getTemp();
        for (ForecastWeather forecast : dayForecast) {
            if (forecast.getData().getTemp() > maxTemp) maxTemp = forecast.getData().getTemp();
            if (forecast.getData().getTemp() < minTemp) minTemp = forecast.getData().getTemp();
        }
        return new WeeklyForecast(dayName, weatherName, maxTemp, minTemp);
    }

    private String findDayByDate(int dt) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Timestamp(dt));
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



}
