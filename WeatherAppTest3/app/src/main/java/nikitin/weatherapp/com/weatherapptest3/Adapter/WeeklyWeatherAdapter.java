package nikitin.weatherapp.com.weatherapptest3.Adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import nikitin.weatherapp.com.weatherapptest3.MainActivity;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.City;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.WeeklyForecast;
import nikitin.weatherapp.com.weatherapptest3.R;
import nikitin.weatherapp.com.weatherapptest3.View.MainWindowFragment;

/**
 * Created by Uladzislau_Nikitsin on 1/6/2017.
 */

public class WeeklyWeatherAdapter extends ArrayAdapter<WeeklyForecast> {
    ArrayList<WeeklyForecast> forecasts = new ArrayList<>();
    private static WeeklyWeatherAdapter adapter;

    WeeklyWeatherAdapter() {
        super(MainActivity.getAppContext(), 0, new ArrayList<WeeklyForecast>());
    }

    public static WeeklyWeatherAdapter getInstance() {
        if (adapter == null) {
            adapter = new WeeklyWeatherAdapter();
        }
        return adapter;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater.from(getContext()).inflate(R.layout.item_city, parent, false);
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_weekly_forecast, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.weather_name_weekly_forecast)).setText(getItem(position).getWeatherName());
        ((TextView) convertView.findViewById(R.id.day_name_weekly_forecast)).setText(getItem(position).getDayName());
        ((TextView) convertView.findViewById(R.id.max_temp_weekly_forecast)).setText(Integer.toString(getItem(position).getDayTemp()) +"°");
        ((TextView) convertView.findViewById(R.id.min_temp_weekly_forecast)).setText(Integer.toString(getItem(position).getNightTemp()) +"°");
        Drawable icon = getContext().getResources().getDrawable(MainWindowFragment.chooseWeatherIcon(getItem(position).getMainWeatherType()));
        ((ImageView) convertView.findViewById(R.id.weatherIconBox)).setImageDrawable(icon);
        return convertView;
    }

    public void setData(ArrayList<WeeklyForecast> list) {
        forecasts = list;
        clear();
        addAll(forecasts);
        notifyDataSetChanged();
    }
}
