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
    private int deviderHeight;


    private int ELEMENT_SIZE;
    private int LIST_VIEW_SIZE;
    private boolean firstLaunch = true;
    private int firstVisibleItemNumber;
    private int firstVisibleItemBottom;
    int space;
    int divider;

    boolean userStartScroll = true;

    public DayForecastPresenter(final Activity activity, DayForecastFragment view) {
        this.presenter = this;
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
                for (int i = 0; i < response.body().getList().size(); i++) {
                    System.out.println(" " +response.body().getList().get(i).getData().getTemp());
                }
                adapter.notifyDataSetChanged();
                view.getDailyForecastView().setOnScrollListener(presenter);
                //adapter.getView(0, null, null).setPadding(0, 550, 0, 0);
                //adapter.getView(0, null, null).setPadding(0, 0, 0 , 300);

               //view.getDailyForecastView().addHeaderView(pish, null, false);
                //view.getDailyForecastView().addHeaderView(pish, null, false);
                //view.getDailyForecastView().addHeaderView(pish, null, false);
                //System.out.println("count " +view.getDailyForecastView().getTop());
                //view.getDailyForecastView().getTop();


                //System.out.println(view.getDailyForecastView().getChildAt(0).getTop());
                //adapter.getView(0, null, null).setPadding(0, 250, 0, 0);
                //adapter.getView(0, null, null).set
                //spacer.setPadding(0, 250, 0, 0);
                //view.getDailyForecastView().addHeaderView(spacer);

            }

            @Override
            public void onFailure(Call<ForecastResponse> call, Throwable t) {}
        });

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        switch (scrollState) {
            case SCROLL_STATE_TOUCH_SCROLL: {
                userStartScroll = true;
                break;
            }
            case SCROLL_STATE_IDLE: {
                System.out.println("feels like " +pish(42.8, 0.89));
                System.out.println("space " +space +" firstVisibleItem " +firstVisibleItemNumber + " divider " +divider +" Element_size " +ELEMENT_SIZE + " List_view_size " +LIST_VIEW_SIZE +" bottom " +firstVisibleItemBottom);
                if (!userStartScroll) return;
                //Растояние от верха listView до верхушки первого полностью видимого элемента.
                int pixelsFromTop;
                if (firstVisibleItemNumber == 0) {
                    pixelsFromTop = (LIST_VIEW_SIZE/2 - ELEMENT_SIZE/2) + this.view.getDailyForecastView().getChildAt(0).getTop();
                } else
                pixelsFromTop = (firstVisibleItemBottom - space - (firstVisibleItemNumber - 1) * divider) % ELEMENT_SIZE;
                int dividersSumHeight =  divider * (int)Math.ceil(((double) (LIST_VIEW_SIZE/2) - pixelsFromTop) / (double) ELEMENT_SIZE);
                int selectedViewPosition = firstVisibleItemNumber + (int) Math.ceil((double) ((LIST_VIEW_SIZE / 2) - pixelsFromTop - space -dividersSumHeight) / (double) ELEMENT_SIZE);
                //Отступ что бы зацентрировать выбранный элемент
                int indent = LIST_VIEW_SIZE / 2 - ELEMENT_SIZE / 2 - (selectedViewPosition-1)*divider - space;
                //System.out.println("pixelsFromTop = "+pixelsFromTop+" = " +firstVisibleItemBottom +" - " +space +" - ( " +firstVisibleItemNumber+" - " +1 +") * " +divider+" ) % " +ELEMENT_SIZE);
                //System.out.println("dividersSumHeight = " +dividersSumHeight +" = " +divider +" * " + " (int) "+"Math.ceil(" +LIST_VIEW_SIZE/2 +" ) - " +pixelsFromTop +" ) / " +ELEMENT_SIZE);
                //System.out.println("selectedViewPosition = " +selectedViewPosition +" = " + firstVisibleItemNumber + "(int) Math.ceil( "+LIST_VIEW_SIZE/2 +" ) - " +pixelsFromTop +" - " +space +" - " +dividersSumHeight +" ) /" +ELEMENT_SIZE);
                view.setSelectionFromTop(selectedViewPosition, indent);
                //System.out.println(this.view.getDailyForecastView().getChildAt(firstVisibleItemNumber).getTop());
                userStartScroll = false;
                //pish

                ForecastWeather forecastItem = adapter.getItem(selectedViewPosition - 1);
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
        if (view.getChildAt(0) == null) return;

        this.firstVisibleItemNumber = firstVisibleItem;
        this.firstVisibleItemBottom = this.view.getDailyForecastView().getChildAt(0).getBottom();
        System.out.println("pish " +this.view.getDailyForecastView().getChildAt(0).getBottom());
        System.out.println("LIST_VIEW_SIZE/2 - ELEMENT_SIZE/2 = " +(LIST_VIEW_SIZE/2 - ELEMENT_SIZE/2));

        if (firstLaunch) {



            //this.view.getDailyForecastView().getChildAt(0).setPadding(0, 350, 0, 0);
            //this.view.getDailyForecastView().set
            ELEMENT_SIZE = view.getChildAt(firstVisibleItem).getHeight();
            LIST_VIEW_SIZE = view.getHeight();
            divider = Math.abs(view.getChildAt(firstVisibleItem +1).getBottom() - view.getChildAt(firstVisibleItem+2).getTop());
            space = view.getChildAt(0).getTop();
            firstLaunch = false;


            LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LIST_VIEW_SIZE/2 - ELEMENT_SIZE/2);
            LinearLayout linearLayout = new LinearLayout(activity);
            linearLayout.setLayoutParams(layoutParams);
            this.view.getDailyForecastView().addHeaderView(linearLayout, null, true);


            LinearLayout linearLayout2 = new LinearLayout(activity);
            linearLayout2.setLayoutParams(layoutParams);
            this.view.getDailyForecastView().addFooterView(linearLayout, null, true);
        }
    }

    public double pish (double t, double rh) {
        return -42.379 + (2.04901523 * t) + (10.14333127 * rh) - (0.22475541 * t * rh) - (6.83783 / 1000 * t * t) - (5.481717 / 100 * rh * rh) + (1.22874 / 1000 * t * t * rh) + (8.5282 / 10000 * t * rh * rh) - (1.99 * 1000000 * t * t * rh * rh);
    }



    private Data convertToCelcium(Data data) {
        double KELVIN_TO_CELCIUM = 273.0;
        data.setTemp(Math.round(data.getTemp() - KELVIN_TO_CELCIUM));
        data.setTemp_max(Math.round(data.getTemp_max() - KELVIN_TO_CELCIUM));
        data.setTemp_min(Math.round(data.getTemp_min() - KELVIN_TO_CELCIUM));
        return data;
    }
}
