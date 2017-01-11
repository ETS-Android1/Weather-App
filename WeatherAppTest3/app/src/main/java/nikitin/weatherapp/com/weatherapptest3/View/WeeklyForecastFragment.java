package nikitin.weatherapp.com.weatherapptest3.View;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.app.Notification;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nikitin.weatherapp.com.weatherapptest3.Adapter.WeeklyWeatherAdapter;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.WeeklyForecast;
import nikitin.weatherapp.com.weatherapptest3.Presenters.WeeklyForecastPresenter;
import nikitin.weatherapp.com.weatherapptest3.R;

/**
 * Created by Uladzislau_Nikitsin on 1/6/2017.
 */

public class WeeklyForecastFragment extends Fragment {
    static WeeklyForecastFragment fragment;
    private WeeklyWeatherAdapter adapter;
    private WeeklyForecastPresenter presenter;
    private ListView weeklyListView;
    private View view;
    public static final String TITLE = "Weekly Forecast";
    public WeeklyForecastFragment() {
        adapter = WeeklyWeatherAdapter.getInstance();
        presenter = new WeeklyForecastPresenter(this);
    }

    public static WeeklyForecastFragment getInstance() {
        if (fragment == null) {
            fragment = new WeeklyForecastFragment();
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_weekly_forecast, container, false);
            weeklyListView = (ListView) view.findViewById(R.id.weekly_forecast);
            adapter = WeeklyWeatherAdapter.getInstance();
            //weeklyListView.setDivider(R.);
        }

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        weeklyListView.setAdapter(adapter);
        presenter.getWeeklyForecast(TabsPagerAdapter.currentCityId);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.setGroupVisible(R.id.main_menu_group, true);
        menu.setGroupVisible(R.id.cities_group, false);
    }

    public void updateWeeklyForecastList(ArrayList<WeeklyForecast> weeklyForecasts) {
        adapter.setData(weeklyForecasts);
    }
}
