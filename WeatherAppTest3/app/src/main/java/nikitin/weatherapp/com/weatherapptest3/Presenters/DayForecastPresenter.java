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
import nikitin.weatherapp.com.weatherapptest3.Model.ForecastModel.ForecastWeather;
import nikitin.weatherapp.com.weatherapptest3.Model.WeatherModel.Weather;
import nikitin.weatherapp.com.weatherapptest3.View.DayForecastFragment;
import nikitin.weatherapp.com.weatherapptest3.Model.ForecastModel.ForecastResponse;
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

    public boolean isParametrsSet = false;

    public DayForecastPresenter(final Activity activity, DayForecastFragment fragment) {
        this.presenter = this;
        this.activity = activity;
        this.fragment = fragment;
        forecastList = fragment.getDailyForecastView();
        api = OpenWeatherMapAPI.getInstance();
        handler = DatabaseHandler.getInstance(MainActivity.getAppContext());
    }

    public void loadForecast(final int currentCityId) {
        System.out.println("Starting");
        api.getDailyForecastByCityId(currentCityId, new Callback<ForecastResponse>() {
            @Override
            public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {
                //Иногда сервер глючит и выдает прогноз на больше чем 9 запрашиваемых позиций. Так что тут я обрезаю лишние
                // и заодно конвертирую в подходящий тип.
                ArrayList<DailyForecast>  list = new ArrayList<DailyForecast>();
                for (int i = 0; i<9; i++) {
                    list.add(new DailyForecast(response.body().getList().get(i), currentCityId));
                }
                if (handler.isDailyForecastExists(currentCityId)) {
                    System.out.println("update");
                    new updateDBTask().execute(list);
                } else {
                    System.out.println("new");
                    new dbAddTask().execute(list);
                }
                fragment.createForecastList(list);
                System.out.println(handler.getDailyForecastAll());
            }
            @Override
            public void onFailure(Call<ForecastResponse> call, Throwable t) {
                fragment.createForecastList(handler.getDailyForecast(currentCityId));
                System.out.println(handler.getDailyForecastAll());
            }
        });
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

    public void getForecastData(int currentCityId) {
        if (currentCityId != -1) {
            presenter.loadForecast(currentCityId);
        }
    }

    public int convertToCelcium (double temp) {
        double KELVIN_TO_CELCIUM = 273.0;
        return (int) Math.round(temp - KELVIN_TO_CELCIUM);
    }

    class dbAddTask extends AsyncTask<ArrayList<DailyForecast>,Void,Void> {
        @Override
        protected Void doInBackground(ArrayList<DailyForecast>... f) {
            DatabaseHandler.getInstance(MainActivity.getAppContext()).addDailyForecastList(f[0]);
            return null;
        }
    }

    class updateDBTask extends AsyncTask<ArrayList<DailyForecast>,Void,Void> {
        @Override
        protected Void doInBackground(ArrayList<DailyForecast>... f) {
            int fk_city_id = f[0].get(0).getFk_city_ow_id();
            DatabaseHandler.getInstance(MainActivity.getAppContext()).updateDailyForecast(fk_city_id, f[0]);
            return null;
        }
    }
}
