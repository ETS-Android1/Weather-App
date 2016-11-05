package nikitin.weatherapp.com.weatherapptest3.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;

import nikitin.weatherapp.com.weatherapptest3.Model.DailyForecastSimpleElement;
import nikitin.weatherapp.com.weatherapptest3.R;
import nikitin.weatherapp.com.weatherapptest3.WeatherDrawable;

/**
 * Created by Влад on 22.10.2016.
 */
public class DailyWeatherAdapter extends ArrayAdapter<DailyForecastSimpleElement> {

    public DailyWeatherAdapter(Context context, ArrayList<DailyForecastSimpleElement> cities) {
        super(context, 0, new ArrayList<DailyForecastSimpleElement>());
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_daily_forecast, parent, false);

        DailyForecastSimpleElement forecast = getItem(position);

        DateFormat format1 = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        format1.setTimeZone(TimeZone.getTimeZone("GMT"));

        ((TextView) convertView.findViewById(R.id.timeBoxDailyForecast)).setText(format1.format(forecast.getDate()));
        ((TextView) convertView.findViewById(R.id.temperatureBoxDailyForecast)).setText(forecast.getTemperature()+"°");

        //getContext().getResources().getDrawable(WeatherDrawable.getDrawable(forecast.getWeatherName()), getContext().getTheme());
        ((ImageView) convertView.findViewById(R.id.iconBoxDailyForecast)).setImageDrawable(getContext().getResources().getDrawable(WeatherDrawable.getDrawable(forecast.getWeatherName())));
        return convertView;
    }


}
