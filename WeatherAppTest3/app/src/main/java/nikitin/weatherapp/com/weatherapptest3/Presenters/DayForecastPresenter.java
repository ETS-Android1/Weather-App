package nikitin.weatherapp.com.weatherapptest3.Presenters;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nikitin.weatherapp.com.weatherapptest3.MainActivity;
import nikitin.weatherapp.com.weatherapptest3.Model.DailyForecastSimpleElement;
import nikitin.weatherapp.com.weatherapptest3.Adapter.DailyWeatherAdapter;
import nikitin.weatherapp.com.weatherapptest3.Model.WeatherModel.Data;
import nikitin.weatherapp.com.weatherapptest3.Model.WeatherModel.WeatherResponse;
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
public class DayForecastPresenter implements AbsListView.OnScrollListener {
    private DayForecastPresenter presenter;
    private OpenWeatherMapAPI api;
    private DayForecastFragment view;
    private DailyWeatherAdapter adapter;
    private Activity activity;
    private ListView forecastList;
    private int listElementHeight;
    private int listViewHeight;
    private int firstVisibleItemNumber;
    private int firstVisibleItemBottom;
    private int topSpace;
    private int divider;

    private int currentCityId;

    public DayForecastPresenter(final Activity activity, DayForecastFragment view) {
        this.presenter = this;
        this.activity = activity;
        this.currentCityId = ((MainActivity)activity).getCurrentCityId();
        this.view = view;
        forecastList = this.view.getDailyForecastView();
        api = OpenWeatherMapAPI.getInstance();
        adapter = new DailyWeatherAdapter(activity, new ArrayList<ForecastWeather>());
        forecastList.setAdapter(adapter);
    }

    public void updateForecastList() {
        api.getDailyForecastByCityId(currentCityId, new Callback<ForecastResponse>() {
            @Override
            public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {
                adapter.addAll(response.body().getList());
                adapter.notifyDataSetChanged();
                forecastList.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                        forecastList.removeOnLayoutChangeListener(this);

                        //Заносим константы
                        listElementHeight = forecastList.getChildAt(0).getHeight();
                        listViewHeight = forecastList.getHeight();
                        divider = Math.abs(forecastList.getChildAt(0).getBottom() - forecastList.getChildAt(1).getTop());
                        topSpace = forecastList.getChildAt(0).getTop();
                        //Пустые вьюшки нужны что бы центрировать верхние и нижние значимые элементы списка.
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, listViewHeight / 2 - listElementHeight / 2);
                        LinearLayout indent = new LinearLayout(activity);
                        indent.setLayoutParams(layoutParams);
                        forecastList.addHeaderView(indent, null, true);
                        forecastList.addFooterView(indent, null, true);

                        forecastList.setOnScrollListener(presenter);
                    }
                });
            }

            @Override
            public void onFailure(Call<ForecastResponse> call, Throwable t) {
            }
        });
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case SCROLL_STATE_IDLE: {
                //Еба формула, ахтунг. Оптимизация бы не помешала.
                //Растояние от верха listView до верхушки первого полностью видимого элемента.
                int pixelsFromTop;
                if (firstVisibleItemNumber == 0) {
                    pixelsFromTop = (listViewHeight - listElementHeight)/ 2 + forecastList.getChildAt(0).getTop();
                } else pixelsFromTop = (firstVisibleItemBottom - topSpace - (firstVisibleItemNumber - 1) * divider) % listElementHeight;

                int dividersSumHeight =  divider * (int)Math.ceil(((double) (listViewHeight/2) - pixelsFromTop) / (double) listElementHeight);
                int selectedViewPosition = firstVisibleItemNumber + (int) Math.ceil((double) ((listViewHeight / 2) - pixelsFromTop - topSpace -dividersSumHeight) / (double) listElementHeight);
                //Отступ что бы зацентрировать выбранный элемент
                int indent = (listViewHeight - listElementHeight) / 2 - (selectedViewPosition-1)*divider - topSpace;

                view.setSelectionFromTop(selectedViewPosition, indent);
                int item = selectedViewPosition - 1;
                ForecastWeather forecastItem = adapter.getItem(item);
                this.view.setHumidityBoxText(forecastItem.getData().getHumidity());
                this.view.setPressureBoxText(forecastItem.getData().getPressure());
                this.view.setWindSpeedBoxText(forecastItem.getWind().getSpeed());
                this.view.setWindDirectionBoxText(forecastItem.getWind().getDeg());
                this.view.setWeatherIcon(forecastItem.getWeathers().get(0).getMain());
                break;
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstVisibleItemNumber = firstVisibleItem;
        this.firstVisibleItemBottom = this.view.getDailyForecastView().getChildAt(0).getBottom();
    }
}
