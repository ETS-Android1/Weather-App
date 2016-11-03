package nikitin.weatherapp.com.weatherapptest3.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;

import nikitin.weatherapp.com.weatherapptest3.Model.DailyForecastSimpleElement;
import nikitin.weatherapp.com.weatherapptest3.R;

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

        System.out.println("PRINTLN" +getItem(position).getDate().getTime());
        System.out.println("pish" +new Timestamp(1477591200).toString());
        DailyForecastSimpleElement dfse = getItem(position);
        TextView temperatureBox = (TextView) convertView.findViewById(R.id.temperatureBoxDailyForecast);
        temperatureBox.setText(Double.toString(dfse.getTemperature())+"°");
        TextView timeView = (TextView) convertView.findViewById(R.id.timeBoxDailyForecast);
        DateFormat format1 = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        format1.setTimeZone(TimeZone.getTimeZone("GMT"));
        timeView.setText(format1.format(dfse.getDate()));
        return convertView;
    }


}
