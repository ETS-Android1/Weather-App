package nikitin.weatherapp.com.weatherapptest3.Presenters;

import android.app.Activity;
import android.widget.ListView;

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
    }

    public void loadForecast(int currentCityId) {
        api.getDailyForecastByCityId(currentCityId, new Callback<ForecastResponse>() {
            @Override
            public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {
                System.out.println("i get list size " +response.body().getList().size());
                fragment.createForecastList(response.body().getList());
            }
            @Override
            public void onFailure(Call<ForecastResponse> call, Throwable t) {
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
}
