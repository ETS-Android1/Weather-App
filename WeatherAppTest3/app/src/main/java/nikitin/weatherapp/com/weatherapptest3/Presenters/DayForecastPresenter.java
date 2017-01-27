package nikitin.weatherapp.com.weatherapptest3.Presenters;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.nio.channels.AsynchronousCloseException;
import java.util.ArrayList;
import java.util.Arrays;

import nikitin.weatherapp.com.weatherapptest3.DatabaseHandler;
import nikitin.weatherapp.com.weatherapptest3.MainActivity;
import nikitin.weatherapp.com.weatherapptest3.Model.DailyForecastItem;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.CurrentWeather;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.DailyForecast;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.Forecast;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.GeoStorm;
import nikitin.weatherapp.com.weatherapptest3.Model.ForecastModel.ForecastWeather;
import nikitin.weatherapp.com.weatherapptest3.Model.WeatherModel.Weather;
import nikitin.weatherapp.com.weatherapptest3.View.DayForecastFragment;
import nikitin.weatherapp.com.weatherapptest3.Model.ForecastModel.ForecastResponse;
import nikitin.weatherapp.com.weatherapptest3.rest.ApiClient;
import nikitin.weatherapp.com.weatherapptest3.rest.OpenWeatherMapAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Влад on 26.10.2016.
 */
public class DayForecastPresenter  {

    private DayForecastPresenter presenter;
    private OpenWeatherMapAPI api;
    private DayForecastFragment fragment;
    private Activity activity;
    private ListView forecastList;
    private DatabaseHandler handler;
    private int listElementHeight;
    private int listViewHeight;
    private int firstVisibleItemNumber;
    private int firstVisibleItemBottom;
    private int topSpace;
    private int divider;
    private int selectedViewPosition;
    private int indent;

    public ArrayList <DailyForecastItem>  items = new ArrayList<DailyForecastItem>();
    private ArrayList <GeoStorm> geoStorms;
    private ArrayList <Forecast> weatherForecasts;
    private boolean weatherSet = false;
    private boolean geoStormSet = false;

    private final int FORECAST_AMOUNT = 9;
    public boolean isParametrsSet = false;

    public DayForecastPresenter(final Activity activity, DayForecastFragment fragment) {
        this.presenter = this;
        this.activity = activity;
        this.fragment = fragment;
        forecastList = fragment.getDailyForecastView();
        api = OpenWeatherMapAPI.getNewInstance(ApiClient.Urls.OPENWEATHER);
        handler = DatabaseHandler.getInstance(MainActivity.getAppContext());
    }

    public void onScroll(int firstVisibleItemNumber, int firstVisibleItemBottom) {
        this.firstVisibleItemNumber = firstVisibleItemNumber;
        this.firstVisibleItemBottom = firstVisibleItemBottom;
    }

    public void setListViewParameters(int listElementHeight, int listViewHeight, int divider, int topSpace) {
        this.listElementHeight = listElementHeight;
        this.listViewHeight = listViewHeight;
        this.divider = divider;
        this.topSpace = topSpace;

        isParametrsSet = true;
    }


    public void calculateScrollParameters(int firstElementTop) {
        //Еба формула, ахтунг. Оптимизация бы не помешала.
        //Растояние от верха listView до верхушки первого полностью видимого элемента.
        int pixelsFromTop;
        if (firstVisibleItemNumber == 0) {
            pixelsFromTop = (listViewHeight - listElementHeight) / 2 + firstElementTop;
        } else
            pixelsFromTop = (firstVisibleItemBottom - topSpace - (firstVisibleItemNumber - 1) * divider) % listElementHeight;
        int dividersSumHeight = divider * (int) Math.ceil(((double) (listViewHeight / 2) - pixelsFromTop) / (double) listElementHeight);
        selectedViewPosition = firstVisibleItemNumber + (int) Math.ceil((double) ((listViewHeight / 2) - pixelsFromTop - topSpace - dividersSumHeight) / (double) listElementHeight);
        //Отступ что бы зацентрировать выбранный элемент
        indent = (listViewHeight - listElementHeight) / 2 - (selectedViewPosition - 1) * divider - topSpace;
    }
    public int getSelectedViewPosition() {
        return selectedViewPosition;
    }
    public int getIndent() {
        return indent;
    }

    public void setForecasts(ArrayList<GeoStorm> geoStorms, ArrayList<Forecast> weatherForecasts) {
        if (geoStorms == null) this.weatherForecasts = weatherForecasts;
        if (weatherForecasts == null) this.geoStorms = geoStorms;
        System.out.println();
        if (this.weatherForecasts != null && this.geoStorms !=null) {
            System.out.println("shit");
            ArrayList<DailyForecastItem> items = new ArrayList<>();
            for (int i = 0; i < FORECAST_AMOUNT; i++) {
                items.add(i, new DailyForecastItem(this.weatherForecasts.get(i), this.geoStorms.get(i).getkIndex()));
                System.out.println("items " +i +"   " +items.get(i).toString());
            }

            fragment.setAdapter(items);
        }
    }

    public void setWeatherForecasts(ArrayList<Forecast> forecast) {
        this.weatherForecasts = forecast;
        weatherSet = true;

        if (geoStormSet) {
            System.out.println("worked");
            items.clear();
            for (int i = 0; i < FORECAST_AMOUNT; i++) {
                items.add(new DailyForecastItem(new Forecast(), 0));
            }
            fragment.setAdapter(items);
            weatherSet = false;
            geoStormSet = false;
        }
    }

    public void setGeoStorms(ArrayList<GeoStorm> geoStorms) {
        this.geoStorms = geoStorms;
        geoStormSet = true;
        System.out.println("worked");
        if (weatherSet) {
            items.clear();
            for (int i = 0; i < FORECAST_AMOUNT; i++) {
                items.add(new DailyForecastItem(new Forecast(), 0));
            }
            fragment.setAdapter(items);
            weatherSet = false;
            geoStormSet = false;
        }
    }



//    set
//    private void setWeatherForecast1(ArrayList<Forecast> forecasts) {
//
//        if (items.isEmpty()) {
//            for (int i = 0; i < FORECAST_AMOUNT; i++) {
//                items.add(new DailyForecastItem(forecasts.get(i), 0));
//            }
//            return;
//        }
//        for (int i = 0; i < FORECAST_AMOUNT; i++) {
//            items.get(i).setForecast(forecasts.get(i));
//        }
//        fragment.setAdapter(items);
//    }
//    private void setGeoStormForecast1(ArrayList<GeoStorm> geoStorms) {
//        if (items.isEmpty()) {
//            for (int i = 0; i < FORECAST_AMOUNT; i++) {
//                items.add(new DailyForecastItem(null, items.get(i).getkIndex()));
//            }
//            return;
//        }
//        for (int i = 0; i < FORECAST_AMOUNT; i++) {
//            items.get(i).setkIndex(geoStorms.get(i).getkIndex());
//        }
//        fragment.setAdapter(items);
//    }
//
//    public void setItems(ArrayList<DailyForecastItem> items) {
//        this.items = items;
//    }
//
//    public ArrayList<DailyForecastItem> getItems() {
//        return items;
//    }
//
//    public void setWeathForecast(ArrayList<Forecast> forecasts) {
//        this.forecasts = new ArrayList<>(forecasts.subList(0, FORECAST_AMOUNT));
//    }
//    public ArrayList<Forecast> getWeathForecast() {
//        return forecasts;
//    }
//
//
//    public ArrayList<GeoStorm> getGeoStormForecast() {
//        return geoStormForecast;
//    }
//
//    public ArrayList<DailyForecastItem> getListItems() {
//        return listItems;
//    }
//public void setWeatherForecast(ArrayList<Forecast> forecasts) {
//    if (listItems.isEmpty()) {
//        for (int i = 0; i < FORECAST_AMOUNT; i++) {
//            listItems.add(new DailyForecastItem(forecasts.get(i), 0));
//        }
//        return;
//    }
//    for (int i = 0; i < FORECAST_AMOUNT; i++) {
//        listItems.get(i).setForecast(forecasts.get(i));
//    }
//    fragment.updateView(listItems);
//}
//
//    public void setGeoStormForecast(ArrayList<GeoStorm> forecasts) {
//        if (listItems.isEmpty()) {
//            for (int i = 0; i < FORECAST_AMOUNT; i++) {
//                listItems.add(new DailyForecastItem(null, forecasts.get(i).getkIndex()));
//            }
//            return;
//        }
//        for (int i = 0; i < FORECAST_AMOUNT; i++) {
//            listItems.get(i).setkIndex(forecasts.get(i).getkIndex());
//        }
//        fragment.updateView(listItems);
//    }
}
