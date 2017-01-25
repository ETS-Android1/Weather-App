package nikitin.weatherapp.com.weatherapptest3.View;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import nikitin.weatherapp.com.weatherapptest3.Adapter.DailyWeatherAdapter;
import nikitin.weatherapp.com.weatherapptest3.Model.DailyForecastItem;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.DailyForecast;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.Forecast;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.GeoStorm;
import nikitin.weatherapp.com.weatherapptest3.Model.ForecastModel.ForecastWeather;
import nikitin.weatherapp.com.weatherapptest3.Model.WeatherModel.Weather;
import nikitin.weatherapp.com.weatherapptest3.Presenters.DayForecastPresenter;
import nikitin.weatherapp.com.weatherapptest3.R;
import nikitin.weatherapp.com.weatherapptest3.WeatherDrawable;

/**
 * Created by Влад on 22.10.2016.
 */
public class DayForecastFragment extends Fragment implements AbsListView.OnScrollListener{
    DayForecastPresenter presenter;
    private DailyWeatherAdapter adapter;
    private static DayForecastFragment fragment;
    public static final String TITLE = "Daily Forecast";
    private ListView dailyForecastView;
    private TextView pressureBox;
    private TextView windSpeedBox;
    private TextView humidityBox;
    private TextView windDirectionBox;
    private ImageView weatherIconBox;
    private View view;

    public DayForecastFragment() {
        presenter = new DayForecastPresenter(getActivity(), this);
        adapter = DailyWeatherAdapter.getInstance(presenter);
    }

    public static DayForecastFragment newInstance() {
        if (fragment == null) {
            fragment = new DayForecastFragment();
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_day_forcast, container, false);
            dailyForecastView = (ListView) view.findViewById(R.id.daily_weather);
            pressureBox = (TextView) view.findViewById(R.id.pressureBox);
            windSpeedBox = (TextView) view.findViewById(R.id.windSpeedBox);
            humidityBox = (TextView) view.findViewById(R.id.humidityBox);
            windDirectionBox = (TextView) view.findViewById(R.id.pishBox);
            weatherIconBox = (ImageView) view.findViewById(R.id.weatherIconBox);
        }
        createForecastList(presenter.getForecasts(), presenter.getGeoStormForecast());
        setHasOptionsMenu(true);
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        dailyForecastView.setAdapter(adapter);

    }

    public ListView getDailyForecastView() {
        return dailyForecastView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.setGroupVisible(R.id.main_menu_group, true);
        menu.setGroupVisible(R.id.cities_group, false);
    }

    public void createForecastList(ArrayList<Forecast> weathers, ArrayList<GeoStorm> geoStormForecast) {
        if (presenter.getForecasts() == null) return;

        ArrayList<DailyForecastItem> list = new ArrayList<>();

        for (int i = 0; i < weathers.size(); i++) {
            list.add(new DailyForecastItem(weathers.get(i), geoStormForecast.get(i).getkIndex()));
        }
        adapter.setData(list);
        if (presenter.isParametrsSet) return;
        view.post(new Runnable() {
            @Override
            public void run() {
                int listElementHeight = dailyForecastView.getChildAt(0).getHeight();
                int listViewHeight = dailyForecastView.getHeight();
                int divider = Math.abs(dailyForecastView.getChildAt(0).getBottom() - dailyForecastView.getChildAt(1).getTop());
                int topSpace = dailyForecastView.getChildAt(0).getTop();
                presenter.setListViewParameters(listElementHeight, listViewHeight, divider, topSpace);

                //Пустые вьюшки нужны что бы центрировать верхние и нижние значимые элементы списка.
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, listViewHeight / 2 - listElementHeight / 2);
                LinearLayout indent = new LinearLayout(getActivity());
                indent.setLayoutParams(layoutParams);
                dailyForecastView.addHeaderView(indent, null, true);
                dailyForecastView.addFooterView(indent, null, true);
                dailyForecastView.setOnScrollListener(fragment);
                dailyForecastView.scrollListBy(30);
            }
        });
    }
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        presenter.onScroll(firstVisibleItem, dailyForecastView.getChildAt(0).getBottom());
    }
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case SCROLL_STATE_IDLE: {
                presenter.calculateScrollParameters(dailyForecastView.getChildAt(0).getTop());
                view.setSelectionFromTop(presenter.getSelectedViewPosition(), presenter.getIndent());
                int item = presenter.getSelectedViewPosition() - 1;
                DailyForecastItem forecast = adapter.getItem(item);
                pressureBox.setText(Integer.toString(forecast.getForecast().getPressure()));
                humidityBox.setText(Integer.toString(forecast.getForecast().getHumidity()));
                windSpeedBox.setText(Double.toString(forecast.getForecast().getWind_speed()));
                windDirectionBox.setText(Integer.toString(forecast.getForecast().getWind_direction()));
                Drawable icon = getContext().getResources().getDrawable(WeatherDrawable.getDrawable(forecast.getForecast().getWeatherType()), getActivity().getTheme());
                weatherIconBox.setImageDrawable(icon);
                break;
            }
        }
    }

    public void setForecasts(ArrayList<Forecast> forecasts) {
        presenter.setForecasts(forecasts);
    }

    public void setGeoStormForecast(ArrayList<GeoStorm> forecast) {
        System.out.println("it's setting");
        presenter.setGeoStormForecast(forecast);
    }
}
