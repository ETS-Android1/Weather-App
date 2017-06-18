package nikitin.weatherapp.com.weatherapptest3.Presenters;

import android.util.Log;
import android.widget.Toast;

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
import nikitin.weatherapp.com.weatherapptest3.Model.Database.Forecast;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.WeeklyForecast;
import nikitin.weatherapp.com.weatherapptest3.View.WeeklyForecastFragment;

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

/*        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(forecasts.get(0).getDate() * 1000);*/

        Date date = new Date(forecasts.get(0).getDate() * 1000L);


        int lastDayOfWeek = date.getDay();
        final ArrayList<WeeklyForecast> list = new ArrayList<WeeklyForecast>(5);
        final ArrayList<Forecast> day = new ArrayList<>(segmentsPerDay);
        for (int i = 0; i < forecasts.size(); i++) {
            Date date2 = new Date(forecasts.get(i).getDate() * 1000L);
            final int currentDayNumber = date2.getDay();
            if (lastDayOfWeek != currentDayNumber || i + 1 == forecasts.size()) {
                list.add(findDayDataForWeeklyForecast(day));
                day.clear();
                lastDayOfWeek = currentDayNumber;
            }
            day.add(forecasts.get(i));
        }

        fragment.updateWeeklyForecastList(list);
    }


    private WeeklyForecast findDayDataForWeeklyForecast  (List<Forecast> dayForecast) {
        Log.d("size", ""+dayForecast.size());
        String dayName = findDayByDate(dayForecast.get(0).getDate());
        String weatherName;
        int maxTemp = dayForecast.get(0).getTemperature();
        int minTemp = dayForecast.get(0).getTemperature();
        Map<Key, Integer> weatherTypes = new TreeMap<>();
        int foregroundCode = 0;

        String prioritizedWeatherDetailedType = "";
        String prioritizedWeatherType = "";

        for (Forecast forecast : dayForecast) {
            //Считаю максимум и минимум температуры
            if (forecast.getTemperature() > maxTemp) maxTemp = forecast.getTemperature();
            if (forecast.getTemperature() < minTemp) minTemp = forecast.getTemperature();
            Key key = new Key(forecast.getGroup_code(), forecast.getWeatherType(), forecast.getWeatherDetailedType());
            if (weatherTypes.containsKey(key)) {
                weatherTypes.put(key, weatherTypes.get(key)+1);
            } else weatherTypes.put(key, 1);

            //Выбираю приоритный тип погоды
            if (foregroundCode < prioritizeCode(forecast.getGroup_code())) {
                prioritizedWeatherDetailedType = forecast.getWeatherDetailedType();
                prioritizedWeatherType = forecast.getWeatherType();
                foregroundCode = prioritizeCode(forecast.getGroup_code());
            }
        }
        Key modeWeather = firstPartAverageWeatherType(weatherTypes);
        if (modeWeather.getGroupName() == prioritizedWeatherType) {
            weatherName = modeWeather.getWeatherDetailedType().substring(0,1).toUpperCase() + modeWeather.getWeatherDetailedType().substring(1);
        } else {
            weatherName = modeWeather.getGroupName() + " with " +prioritizedWeatherDetailedType;
        }
        return new WeeklyForecast(dayName, modeWeather.getGroupName(), weatherName, maxTemp, minTemp);
    }

    private String findDayByDate(long dt) {
        Date date = new Date(dt * 1000L);

        Log.d("date", ""+date.getDay());

        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.setTime(new Date(dt * 1000L));
        if (calendar.getFirstDayOfWeek() == Calendar.SUNDAY) {
            calendar.add(Calendar.DAY_OF_WEEK, 1);
        }

        System.out.println("olololololololooloool" +calendar.getFirstDayOfWeek());
        switch (date.getDay()) {
            case 1: return "Monday";
            case 2: return "Tuesday";
            case 3: return "Wednesday";
            case 4: return "Thursday";
            case 5: return "Friday";
            case 6: return "Saturday";
            case 7:
            case 0: return "Sunday";
        }
        return "error";
    }

    private Key firstPartAverageWeatherType(Map<Key, Integer> map) {
        ArrayList <Map.Entry<Key, Integer>> list = new ArrayList(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Key, Integer>>() {
            @Override
            public int compare(Map.Entry<Key, Integer> a, Map.Entry<Key, Integer> b) {
                return - (a.getValue() - b.getValue());
            }
        });
        return new Key(list.get(0).getKey().getGroupCode(), list.get(0).getKey().getGroupName(), list.get(0).getKey().getWeatherDetailedType());
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

        int groupCode = (code/100)*100;
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

    public class Key implements Comparable{
        int groupCode;
        String groupName;
        String weatherDetailedType;

        Key(int groupCode, String groupName, String weatherDetailedType) {
            this.groupCode = groupCode;
            this.groupName = groupName;
            this.weatherDetailedType = weatherDetailedType;
        }

        public int getGroupCode() {
            return groupCode;
        }
        public void setGroupCode(int groupCode) {
            this.groupCode = groupCode;
        }

        public String getGroupName() {
            return groupName;
        }
        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getWeatherDetailedType() {
            return weatherDetailedType;
        }
        public void setWeatherDetailedType(String weatherDetailedType) {
            this.weatherDetailedType = weatherDetailedType;
        }

        @Override
        public int compareTo(Object o) {
            return (this.groupName.equals(((Key)o).getGroupName()) ? 0: -1);
        }
    }
}
