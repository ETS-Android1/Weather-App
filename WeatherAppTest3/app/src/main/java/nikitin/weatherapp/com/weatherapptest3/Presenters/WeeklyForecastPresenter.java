package nikitin.weatherapp.com.weatherapptest3.Presenters;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

import nikitin.weatherapp.com.weatherapptest3.Model.Database.DailyForecast;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.Forecast;
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
    ArrayList<Forecast> forecasts;
    public WeeklyForecastPresenter(WeeklyForecastFragment fragment) {
        this.fragment = fragment;
    }

    public void getWeeklyForecast() {
        int segmentsPerDay = 8;
        int a = 0;
        int b = forecasts.size() % segmentsPerDay;
        ArrayList<WeeklyForecast> list = new ArrayList<WeeklyForecast>(5);
        for (int i = 0; i < 5; i ++) {
            list.add(findDayDataForWeeklyForecast(forecasts.subList(a, b)));
            a = b;
            b += segmentsPerDay;
        }
        fragment.updateWeeklyForecastList(list);
    }


    private WeeklyForecast findDayDataForWeeklyForecast  (List<Forecast> dayForecast) {
        String dayName = findDayByDate(dayForecast.get(0).getDate());
        String weatherName;
        int maxTemp = dayForecast.get(0).getTemperature();
        int minTemp = dayForecast.get(0).getTemperature();
        Map<String, Integer> weatherTypes = new TreeMap<>();
        int code = 0;
        String name = "";

        for (Forecast forecast : dayForecast) {
            //Считаю максимум и минимум температуры
            if (forecast.getTemperature() > maxTemp) maxTemp = forecast.getTemperature();
            if (forecast.getTemperature() < minTemp) minTemp = forecast.getTemperature();

            //Заношу типы погод для выбора моды
            String weatherTypeDescription = forecast.getWeatherType();
            if (weatherTypes.containsKey(weatherTypeDescription)) {
                weatherTypes.put(weatherTypeDescription, weatherTypes.get(weatherTypeDescription)+1);
            } else weatherTypes.put(weatherTypeDescription, 1);
            //Выбираю приоритный тип погоды
            if (code < prioritizeCode(forecast.getGroup_code())) {
                name = forecast.getWeatherDetailedType();
                code = prioritizeCode(forecast.getGroup_code());
            }
        }
        //Окончательный тип погоды получаю как мода типа погоды и приоритного типа погоды
        weatherName = firstPartAverageWeatherType(weatherTypes) +" with "+ name;
        return new WeeklyForecast(dayName, weatherName, maxTemp, minTemp);
    }

    private String findDayByDate(int dt) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.setTime(new Date(dt * 1000));
        if (calendar.getFirstDayOfWeek() == Calendar.SUNDAY) {
            calendar.add(Calendar.DAY_OF_WEEK, 1);
        }
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY: return "Monday";
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


    private String firstPartAverageWeatherType(Map<String, Integer> map) {
        ArrayList <Map.Entry<String, Integer>> list = new ArrayList(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b) {
                return - (a.getValue() - b.getValue());
            }
        });
        return list.get(0).getKey();
    }



    private int prioritizeCode(int code) {
        final int THUNDERSTORM_GROUP = 200;
        final int DRIZZLE_GROUP = 300;
        final int RAIN_GROUP = 500;
        final int SNOW_GROUP = 600;
        final int CLEAR_CLOUDS_GROUP = 800;

        final int THUNDERSTORM_PRIORITIES = 500;
        final int DRIZZLE_PRIORITIES = 400;
        final int RAIN_PRIORITIES = 300;
        final int SNOW_PRIORITIES = 200;
        final int CLEAR_CLOUDS_PRIORITIES = 100;

        int groupCode = ((int)(code/100))*100;
        int detailedCode = code % 100;
        int prioritizedCode = 0;
        switch (groupCode) {
            case CLEAR_CLOUDS_GROUP: prioritizedCode = CLEAR_CLOUDS_PRIORITIES; break;
            case SNOW_GROUP: prioritizedCode = SNOW_PRIORITIES; break;
            case RAIN_GROUP: prioritizedCode = RAIN_PRIORITIES; break;
            case DRIZZLE_GROUP: prioritizedCode = DRIZZLE_PRIORITIES; break;
            case THUNDERSTORM_GROUP: prioritizedCode = THUNDERSTORM_PRIORITIES; break;
        }
        return prioritizedCode + detailedCode;
    }

    public void setForecasts(ArrayList<Forecast> forecasts) {
        this.forecasts = forecasts;
    }
}
