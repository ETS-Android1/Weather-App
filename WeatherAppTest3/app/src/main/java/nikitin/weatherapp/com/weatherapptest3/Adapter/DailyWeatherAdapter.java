package nikitin.weatherapp.com.weatherapptest3.Adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;

import nikitin.weatherapp.com.weatherapptest3.MainActivity;
import nikitin.weatherapp.com.weatherapptest3.Model.DailyForecastItem;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.DailyForecast;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.Forecast;
import nikitin.weatherapp.com.weatherapptest3.Model.ForecastModel.ForecastWeather;
import nikitin.weatherapp.com.weatherapptest3.Presenters.DayForecastPresenter;
import nikitin.weatherapp.com.weatherapptest3.R;

/**
 * Created by Влад on 22.10.2016.
 */
public class DailyWeatherAdapter extends ArrayAdapter<DailyForecastItem> {
    private static DayForecastPresenter presenter;
    private ArrayList<DailyForecastItem> weathers;
    private int FORECAST_AMOUNT = 9;

    public DailyWeatherAdapter(DayForecastPresenter presenter) {
        super(MainActivity.getAppContext(), 0, new ArrayList<DailyForecastItem>());
        this.presenter = presenter;

        //Добавляю пустых элементов
        for (int i = 0; i < FORECAST_AMOUNT; i++) {
            add(new DailyForecastItem(new Forecast(), 0));
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_daily_forecast, parent, false);
        }
        convertView.setTag(position);
        DailyForecastItem forecast = getItem(position);

        DateFormat format1 = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        format1.setTimeZone(TimeZone.getTimeZone("GMT"));

        ((TextView) convertView.findViewById(R.id.timeBoxDailyForecast)).setText(format1.format(forecast.getForecast().getDate() * 1000L));
        ((TextView) convertView.findViewById(R.id.temperatureBoxDailyForecast)).setText(forecast.getForecast().getTemperature() + "°");
        ((TextView) convertView.findViewById(R.id.weatherNameBoxDailyForecast)).setText(forecast.getForecast().getWeatherDetailedType().substring(0,1).toUpperCase() + forecast.getForecast().getWeatherDetailedType().substring(1));
        return convertView;
    }

    public void setData(ArrayList<DailyForecastItem> items) {
        for (int i = 0; i < 9; i ++) {
            insert(items.get(i), i);
            remove(getItem(i+1));
        }

        for (int i = 0; i < getCount(); i++) {
            System.out.println(getItem(i));
        }
//        insert();
//        clear();
//        addAll(items);
//        System.out.println("printing");
//        for (int i = 0; i < items.size(); i++) {
//            System.out.println("ololo " +items.get(i));
//        }
        notifyDataSetChanged();
    }

    public ArrayList<DailyForecastItem> getAllItems() {
        weathers.clear();
        for (int i = 0; i < weathers.size(); i++) {
            weathers.add(getItem(i));
        }
        return weathers;
    }
}
