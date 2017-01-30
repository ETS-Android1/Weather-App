package nikitin.weatherapp.com.weatherapptest3.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import nikitin.weatherapp.com.weatherapptest3.MainActivity;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.City;
import nikitin.weatherapp.com.weatherapptest3.R;
import nikitin.weatherapp.com.weatherapptest3.Presenters.CitiesPresenter;
/**
 * Created by Влад on 28.07.2016.
 */
public class CitiesAdapter extends ArrayAdapter<City> implements Button.OnClickListener{
    final private int TYPE_ITEM_CITY = 0;
    final private int TYPE_ITEM_LOCATION = 1;
    final private int TYPES_AMOUNT = 2;

    private CitiesPresenter presenter;
    private int selectedPosition = -1;

    public CitiesAdapter(ArrayList<City> cities, CitiesPresenter presenter) {
        super(MainActivity.getAppContext(), 0, cities);
        this.presenter = presenter;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        if (convertView == null) {
            switch(type) {
                case TYPE_ITEM_CITY:
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_city, parent, false);
                    convertView.findViewById(R.id.deleteButton).setOnClickListener(this);
                    break;
                case TYPE_ITEM_LOCATION:
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_city_2, parent, false);
                    convertView.findViewById(R.id.findLocationButton).setOnClickListener(this);
                    break;
            }
        }


        convertView.setBackgroundResource(R.drawable.shape_rounded_inactive);
        if (position == selectedPosition) {
            convertView.setBackgroundResource(R.drawable.shape_rounded_active);
        }
        convertView.setTag(position);

        TextView cityName = (TextView) convertView.findViewById(R.id.cityName);
        TextView cityShortWeather = (TextView) convertView.findViewById(R.id.cityShotWeather);

        City city = getItem(position);
        cityName.setText(city.getName() + ", " +city.getCountry());
        cityShortWeather.setText(city.getTemperature() + "°, " +city.getWeather_type());
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_ITEM_LOCATION : TYPE_ITEM_CITY;
    }

    @Override
    public int getViewTypeCount() {
        return TYPES_AMOUNT;
    }

    public long getActiveCityId(int selectedPosition) {
        if (selectedPosition != -1) {
            return getItem(selectedPosition).getId();
        }
        return 0;
    }

    public String getActiveCityName(int selectedPosition) {
        if (selectedPosition != -1) {
            return getItem(selectedPosition).getName();
        }
        return "";
    }

    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.deleteButton:
                int position = (int)((LinearLayout)view.getParent()).getTag();
                City city = getItem(position);
                remove(city);
                presenter.deleteCity(city.getId());
                notifyDataSetChanged();
                break;
            case R.id.findLocationButton:
                presenter.getCityByGPS();
                break;
        }
    }

    public void setDatas(ArrayList<City> list) {
        clear();
        addAll(list);
        notifyDataSetChanged();
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }
}
