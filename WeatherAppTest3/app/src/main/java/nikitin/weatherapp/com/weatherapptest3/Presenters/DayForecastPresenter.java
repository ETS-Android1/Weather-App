package nikitin.weatherapp.com.weatherapptest3.Presenters;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.ListView;

import java.nio.channels.AsynchronousCloseException;
import java.util.ArrayList;
import java.util.Arrays;

import nikitin.weatherapp.com.weatherapptest3.DatabaseHandler;
import nikitin.weatherapp.com.weatherapptest3.MainActivity;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.CurrentWeather;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.DailyForecast;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.Forecast;
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
    private ArrayList<Forecast> forecasts;
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

    private final int FORECAST_AMOUNT = 9;
    public boolean isParametrsSet = false;

    public DayForecastPresenter(final Activity activity, DayForecastFragment fragment) {
        this.presenter = this;
        this.activity = activity;
        this.fragment = fragment;
        forecastList = fragment.getDailyForecastView();
        api = OpenWeatherMapAPI.getNewInstance(ApiClient.URL_OPEN_WEATHER);
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

    public void setForecasts(ArrayList<Forecast> forecasts) {
        this.forecasts = new ArrayList<>(forecasts.subList(0, FORECAST_AMOUNT));
    }
    public ArrayList<Forecast> getForecasts() {
        return forecasts;
    }
}
