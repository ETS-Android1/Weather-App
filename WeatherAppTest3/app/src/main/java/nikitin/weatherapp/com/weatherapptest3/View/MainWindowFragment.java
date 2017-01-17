package nikitin.weatherapp.com.weatherapptest3.View;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nikitin.weatherapp.com.weatherapptest3.Model.Database.City;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.CurrentWeather;
import nikitin.weatherapp.com.weatherapptest3.Presenters.MainWindowPresenter;
import nikitin.weatherapp.com.weatherapptest3.R;
import nikitin.weatherapp.com.weatherapptest3.Model.WeatherModel.Data;
import nikitin.weatherapp.com.weatherapptest3.Model.WeatherModel.WeatherResponse;
import nikitin.weatherapp.com.weatherapptest3.Model.WeatherModel.Wind;
import nikitin.weatherapp.com.weatherapptest3.Model.WeatherModel.Weather;

/**
 * Created by Влад on 23.07.2016.
 */
public class MainWindowFragment extends Fragment {
    public static final String TITLE = "Weather";
    private MainWindowPresenter presenter;

    private TextView temperatureBox;
    private TextView weatherNameBox;
    private TextView humidityBox;
    private TextView windSpeedBox;
    private TextView pressureBox;
    private ImageView weatherIconBox;
    private TextView apparentTemperatureBox;

    private static MainWindowFragment fragment;
    private View view;

    public MainWindowFragment(){
        presenter = new MainWindowPresenter(this);
    }

    public static MainWindowFragment getInstance() {
        if (fragment == null) fragment = new MainWindowFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_main_window, container, false);
            weatherIconBox = (ImageView) view.findViewById(R.id.weatherIconBox);
            temperatureBox = (TextView) view.findViewById(R.id.temperatureBox);
            weatherNameBox = (TextView) view.findViewById(R.id.weatherNameBox);
            humidityBox = (TextView) view.findViewById(R.id.humidityBox);
            pressureBox = (TextView) view.findViewById(R.id.pressureBox);
            windSpeedBox = (TextView) view.findViewById(R.id.windSpeedBox);
            apparentTemperatureBox = (TextView) view.findViewById(R.id.apparentTemperatureBox);
        }

        applyCityWeather(presenter.getSelectedCity());
        applyApparentTemperature(presenter.calculateApparentTemperature());

        //createBlankScreen();
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.setGroupVisible(R.id.main_menu_group, true);
        menu.setGroupVisible(R.id.cities_group, false);
    }

    public void applyCityWeather(City city) {
        temperatureBox.setText(Integer.toString(city.getTemperature())+ "°");
        weatherNameBox.setText(city.getWeather_type());
        humidityBox.setText(Integer.toString(city.getHumidity()) + " %");
        pressureBox.setText(Integer.toString(city.getPressure()) + " Pa");
        windSpeedBox.setText(Double.toString(city.getWind_speed()) + " MS");
        Drawable icon = getContext().getResources().getDrawable(chooseWeatherIcon(city.getWeather_type()));
        weatherIconBox.setImageDrawable(icon);
    }

    public void applyApparentTemperature(double temp) {
        apparentTemperatureBox.setText(Integer.toString((int)temp) + "°");
    }

    private static int chooseWeatherIcon(String weatherName) {
        switch (weatherName) {
            case "Thunderstorm":
            case "Drizzle":
            case "Rain":
            case "Snow":
                return R.drawable.ic_rain;
            case "Clouds":
                return R.drawable.ic_scattered_clouds;
            case "Clear":
            default:
                return R.drawable.ic_clear_sky;
        }
    }

    public void setView(City city) {
        presenter.setSelectedCity(city);
        applyCityWeather(city);
        applyApparentTemperature(presenter.calculateApparentTemperature());
    }

}
