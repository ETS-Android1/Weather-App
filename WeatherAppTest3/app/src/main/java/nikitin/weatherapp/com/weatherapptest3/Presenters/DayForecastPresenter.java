package nikitin.weatherapp.com.weatherapptest3.Presenters;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import nikitin.weatherapp.com.weatherapptest3.Model.DailyForecastSimpleElement;
import nikitin.weatherapp.com.weatherapptest3.Adapter.DailyWeatherAdapter;
import nikitin.weatherapp.com.weatherapptest3.Model.WeatherModel.Data;
import nikitin.weatherapp.com.weatherapptest3.R;
import nikitin.weatherapp.com.weatherapptest3.View.DayForecastFragment;
import nikitin.weatherapp.com.weatherapptest3.Model.ForecastModel.ForecastResponse;
import nikitin.weatherapp.com.weatherapptest3.Model.ForecastModel.ForecastWeather;
import nikitin.weatherapp.com.weatherapptest3.rest.OpenWeatherMapAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Влад on 26.10.2016.
 */
public class DayForecastPresenter {
    private OpenWeatherMapAPI api;
    private DayForecastFragment view;
    private DailyWeatherAdapter adapter;
    private Activity activity;
    private int deviderHeight;

    public DayForecastPresenter(final Activity activity, DayForecastFragment view) {
        this.activity = activity;
        this.view = view;
        api = OpenWeatherMapAPI.getInstance();
        adapter = new DailyWeatherAdapter(activity, new ArrayList<ForecastWeather>());
        view.getDailyForecastView().setAdapter(adapter);
        System.out.println("Devider " + view.getDailyForecastView().getDividerHeight());
        deviderHeight = view.getDailyForecastView().getDividerHeight();

    }

    public void updateForecastList() {
        api.getDailyForecastByCityId(629634, new Callback<ForecastResponse>() {
            @Override
            public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {
                adapter.addAll(response.body().getList());
                adapter.notifyDataSetChanged();
                view.getDailyForecastView().setOnScrollListener(new AbsListView.OnScrollListener() {

                    private int ELEMENT_SIZE;
                    private int LIST_VIEW_SIZE;
                    private boolean firstLaunch = true;
                    private int firstVisibleItemNumber;
                    private int firstVisibleItemBottom;
                    int space;
                    int divider;

                    boolean userStartScroll = true;

                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {

                        switch (scrollState) {
                            case SCROLL_STATE_TOUCH_SCROLL: {
                                userStartScroll = true;
                                break;
                            }
                            case SCROLL_STATE_IDLE: {
                                System.out.println("pish " +3/2.0);
                                if (!userStartScroll) return;
                                //Растояние от верха listView до верхушки первого полностью видимого элемента.
                                int pixelsFromTop = (firstVisibleItemBottom - space - (firstVisibleItemNumber - 1) * divider) % ELEMENT_SIZE;
                                int dividersSumHeight =  divider * (int)Math.ceil(((double) (LIST_VIEW_SIZE/2) - pixelsFromTop) / (double) ELEMENT_SIZE);
                                int selectedViewPosition = firstVisibleItemNumber + (int) Math.ceil((double) ((LIST_VIEW_SIZE / 2) - pixelsFromTop - space -dividersSumHeight) / (double) ELEMENT_SIZE);
                                //Отступ что бы зацентрировать выбранный элемент
                                int indent = LIST_VIEW_SIZE / 2 - ELEMENT_SIZE / 2 - (selectedViewPosition-1)*divider - space;
                                view.setSelectionFromTop(selectedViewPosition, indent);
                                userStartScroll = false;
                                break;
                            }
                        }
                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        if (view.getChildAt(0) == null) return;

                        this.firstVisibleItemNumber = firstVisibleItem;
                        this.firstVisibleItemBottom = view.getChildAt(0).getBottom();

                        if (firstLaunch) {
                            ELEMENT_SIZE = view.getChildAt(firstVisibleItem).getHeight();
                            LIST_VIEW_SIZE = view.getHeight();
                            divider = Math.abs(view.getChildAt(firstVisibleItem).getBottom() - view.getChildAt(firstVisibleItem+1).getTop());
                            space = view.getChildAt(0).getTop();
                            firstLaunch = false;
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<ForecastResponse> call, Throwable t) {}
        });
    }

    private String createTemperatureText(double temp) {
        int temperature = convertToCelcium(temp);
        return (temperature >= 0)?"+ " +temperature : "- "+Math.abs(temperature);
    }

    private int convertToCelcium(double temp) {
        double KELVIN_TO_CELCIUM = 273.0;
        return (int) Math.round(temp - KELVIN_TO_CELCIUM);
    }
    private Data convertToCelcium(Data data) {
        double KELVIN_TO_CELCIUM = 273.0;
        data.setTemp(Math.round(data.getTemp() - KELVIN_TO_CELCIUM));
        data.setTemp_max(Math.round(data.getTemp_max() - KELVIN_TO_CELCIUM));
        data.setTemp_min(Math.round(data.getTemp_min() - KELVIN_TO_CELCIUM));
        return data;
    }
}
