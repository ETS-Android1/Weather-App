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
import nikitin.weatherapp.com.weatherapptest3.Model.ForecastModel.ForecastWeather;
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
        setHasOptionsMenu(true);
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        dailyForecastView.setAdapter(adapter);
        presenter.getForecastData(TabsPagerAdapter.currentCityId);
    }

    public ListView getDailyForecastView() {
        return dailyForecastView;
    }
    public void setPressureBoxText(double pressure) {
        pressureBox.setText(" " + (int)Math.round(pressure));
    }
    public void setWindSpeedBoxText(double windSpeed) {
        windSpeedBox.setText(" " +(int) Math.round(windSpeed));
    }
    public void setHumidityBoxText(double humidity) {
        humidityBox.setText(" " + (int) Math.round(humidity));
    }
    public void setWindDirectionBoxText(double windDirection) {
        windDirectionBox.setText(" " + (int) Math.round(windDirection));
    }
    public void setWeatherIcon(String name) {
        Drawable icon = getContext().getResources().getDrawable(WeatherDrawable.getDrawable(name), getActivity().getTheme());
        weatherIconBox.setImageDrawable(icon);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.setGroupVisible(R.id.main_menu_group, false);
    }

    public void createForecastList(ArrayList<ForecastWeather> weathers) {
        adapter.setData(weathers);
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
                ForecastWeather forecastItem = adapter.getItem(item);
                setHumidityBoxText(forecastItem.getData().getHumidity());
                setPressureBoxText(forecastItem.getData().getPressure());
                setWindSpeedBoxText(forecastItem.getWind().getSpeed());
                setWindDirectionBoxText(forecastItem.getWind().getDeg());
                setWeatherIcon(forecastItem.getWeathers().get(0).getMain());
                break;
            }
        }
    }
}
