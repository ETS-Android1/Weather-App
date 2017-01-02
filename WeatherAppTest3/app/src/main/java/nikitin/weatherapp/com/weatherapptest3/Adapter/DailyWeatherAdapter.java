package nikitin.weatherapp.com.weatherapptest3.Adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;

import nikitin.weatherapp.com.weatherapptest3.MainActivity;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.DailyForecast;
import nikitin.weatherapp.com.weatherapptest3.Model.ForecastModel.ForecastWeather;
import nikitin.weatherapp.com.weatherapptest3.Presenters.DayForecastPresenter;
import nikitin.weatherapp.com.weatherapptest3.R;

/**
 * Created by Влад on 22.10.2016.
 */
public class DailyWeatherAdapter extends ArrayAdapter<DailyForecast> {

    private static DailyWeatherAdapter adapter;
    private static DayForecastPresenter presenter;
    private ArrayList<DailyForecast> weathers;

    private DailyWeatherAdapter() {
        super(MainActivity.getAppContext(), 0, new ArrayList<DailyForecast>());
    }

    public static DailyWeatherAdapter getInstance(DayForecastPresenter p) {
        if (adapter == null) {
            presenter = p;
            adapter = new DailyWeatherAdapter();
        }
        return adapter;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_daily_forecast, parent, false);
        }
        convertView.setTag(position);
        DailyForecast forecast = getItem(position);

        DateFormat format1 = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        format1.setTimeZone(TimeZone.getTimeZone("GMT"));

        ((TextView) convertView.findViewById(R.id.timeBoxDailyForecast)).setText(format1.format(forecast.getDate() * 1000L));
        ((TextView) convertView.findViewById(R.id.temperatureBoxDailyForecast)).setText(presenter.convertToCelcium(forecast.getTemperature()) + "°");
        ((TextView) convertView.findViewById(R.id.weatherNameBoxDailyForecast)).setText(forecast.getWeather_name());
        return convertView;
    }

    public void setData(ArrayList<DailyForecast> weathers) {
        System.out.println("weathers " + weathers.size());
        clear();
        notifyDataSetChanged();
        System.out.println("weathers " + getCount());
        this.weathers = weathers;
        addAll(weathers);
        System.out.println("count " + getCount());
    }
}
