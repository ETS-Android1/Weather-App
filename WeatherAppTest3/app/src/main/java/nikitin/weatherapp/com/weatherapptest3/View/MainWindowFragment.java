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
    static private TextView temperatureBox;
    static private TextView weatherNameBox;
    static private TextView humidityBox;
    static private TextView windSpeedBox;
    static private TextView pressureBox;
    static private ImageView weatherIconBox;
    private static MainWindowFragment mainWindowView;

    private MainWindowPresenter presenter;

    private final String TITLE = "Weather";
    private int activeCityId;

    public MainWindowFragment(){
        presenter = new MainWindowPresenter(this);
    }

    public MainWindowFragment(int activeCityId) {
        presenter = new MainWindowPresenter(this);
        this.activeCityId = activeCityId;
    }

    public static MainWindowFragment getInstance(int activeCityId) {
        if (activeCityId == 0) {
            mainWindowView = new MainWindowFragment();
        } else {
            mainWindowView = new MainWindowFragment(activeCityId);
        }
        return mainWindowView;
    }

    public void updateWeather(int activeCityId) {
        presenter.updateWeather(activeCityId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainWindowView = inflater.inflate(R.layout.fragment_main_window, container, false);

        getActivity().setTitle(TITLE);

        weatherIconBox = (ImageView) mainWindowView.findViewById(R.id.weatherIconBox);
        temperatureBox = (TextView) mainWindowView.findViewById(R.id.temperatureBox);
        weatherNameBox = (TextView) mainWindowView.findViewById(R.id.weatherNameBox);
        humidityBox = (TextView) mainWindowView.findViewById(R.id.humidityBox);
        pressureBox = (TextView) mainWindowView.findViewById(R.id.pressureBox);
        windSpeedBox = (TextView) mainWindowView.findViewById(R.id.windSpeedBox);

        createBlankScreen();

        setHasOptionsMenu(true);
        return mainWindowView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.setGroupVisible(R.id.main_menu_group, false);
    }

    public void applyWeather (WeatherResponse weatherResponse) {
        temperatureBox.setText(Double.toString(weatherResponse.getData().getTemp()));
        weatherNameBox.setText(weatherResponse.getWeathers().get(0).getMain());
        humidityBox.setText(Integer.toString(weatherResponse.getData().getHumidity()) + " %");
        pressureBox.setText(Double.toString(weatherResponse.getData().getPressure()) + " Pa");
        windSpeedBox.setText(Double.toString(weatherResponse.getWind().getSpeed()) + " km/h");
        System.out.println(getActivity() + "   " + weatherResponse.getWeathers().get(0).getMain());
        Drawable icon = getContext().getResources().getDrawable(chooseWeatherIcon(weatherResponse.getWeathers().get(0).getMain()));
        weatherIconBox.setImageDrawable(icon);
    }

    private void createBlankScreen() {
        //Создаю первый элемент списка - поиск по GPS. Почти все поля - пока не определены.
        Data data = new Data(0, 0, 0, 0, 0, 0, 0);
        List<Weather> weathers = new ArrayList<>();
        weathers.add(new Weather(0, "unknown location", null, null));
        Wind wind = new Wind(0, 0);
        WeatherResponse weatherResponse = new WeatherResponse(0, "Find location by GPS", null, weathers, data, wind, null, 0, null);
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

    public int getActiveCityId() {
        return activeCityId;
    }
}
