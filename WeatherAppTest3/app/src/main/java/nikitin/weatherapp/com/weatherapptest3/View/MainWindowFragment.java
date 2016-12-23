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

    static private TextView temperatureBox;
    static private TextView weatherNameBox;
    static private TextView humidityBox;
    static private TextView windSpeedBox;
    static private TextView pressureBox;
    static private ImageView weatherIconBox;
    private static MainWindowFragment fragment;
    private View view;

    public MainWindowFragment(){
        presenter = new MainWindowPresenter(this);
    }

    public static MainWindowFragment getInstance() {
        if (fragment == null) fragment = new MainWindowFragment();
        return fragment;
    }

    public void updateWeather(int activeCityId) {
        presenter.getWeatherData(activeCityId);
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
        }
        createBlankScreen();
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.setGroupVisible(R.id.main_menu_group, true);
    }

    public void applyWeather (WeatherResponse weatherResponse) {
        temperatureBox.setText(Double.toString(weatherResponse.getData().getTemp()));
        weatherNameBox.setText(weatherResponse.getWeathers().get(0).getMain());
        humidityBox.setText(Integer.toString(weatherResponse.getData().getHumidity()) + " %");
        pressureBox.setText(Double.toString(weatherResponse.getData().getPressure()) + " Pa");
        windSpeedBox.setText(Double.toString(weatherResponse.getWind().getSpeed()) + " km/h");
        Drawable icon = getContext().getResources().getDrawable(chooseWeatherIcon(weatherResponse.getWeathers().get(0).getMain()));
        weatherIconBox.setImageDrawable(icon);
    }

    private void createBlankScreen() {
        Data data = new Data(0, 0, 0, 0, 0, 0, 0);
        List<Weather> weathers = new ArrayList<>();
        weathers.add(new Weather(0, "unknown location", null, null));
        Wind wind = new Wind(0, 0);
        City city = new City(0, 0, "Find location by GPS", "unknown location", 0, 0);
        WeatherResponse weatherResponse = new WeatherResponse(0,0, "Find location by GPS", null, weathers, data, wind, null, 0, null);
        applyWeather(weatherResponse);
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
}
