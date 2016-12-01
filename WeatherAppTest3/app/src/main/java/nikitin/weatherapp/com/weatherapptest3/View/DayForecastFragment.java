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
import android.widget.ListView;
import android.widget.TextView;

import nikitin.weatherapp.com.weatherapptest3.DatabaseHandler;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.City;
import nikitin.weatherapp.com.weatherapptest3.Presenters.DayForecastPresenter;
import nikitin.weatherapp.com.weatherapptest3.R;
import nikitin.weatherapp.com.weatherapptest3.WeatherDrawable;

/**
 * Created by Влад on 22.10.2016.
 */
public class DayForecastFragment extends Fragment {
    DayForecastPresenter presenter;
    public static final String TITLE = "Daily Forecast";
    private ListView dailyForecastView;
    private TextView pressureBox;
    private TextView windSpeedBox;
    private TextView humidityBox;
    private TextView windDirectionBox;
    private ImageView weatherIconBox;
    public DayForecastFragment() {

    }

    public static DayForecastFragment newInstance() {
        return new DayForecastFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_day_forcast, container, false);
        dailyForecastView = (ListView) rootView.findViewById(R.id.daily_weather);
        pressureBox = (TextView) rootView.findViewById(R.id.pressureBox);
        windSpeedBox = (TextView) rootView.findViewById(R.id.windSpeedBox);
        humidityBox = (TextView) rootView.findViewById(R.id.humidityBox);
        windDirectionBox = (TextView) rootView.findViewById(R.id.pishBox);
        weatherIconBox = (ImageView) rootView.findViewById(R.id.weatherIconBox);

        presenter = new DayForecastPresenter(getActivity(), this);
        presenter.updateForecastList();

        DatabaseHandler db = new DatabaseHandler(getContext());

//        System.out.println("shit");
//        City city = new City(656565, "Brest", "By", 43.5, 30.6);
//        db.addTestCity(city);
//
//        System.out.println("pISH" +db.getTestCity(1).getName());

        setHasOptionsMenu(true);
        return rootView;
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
}
