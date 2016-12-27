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
    private ArrayList<City> cities;
    private static CitiesAdapter adapter;
    private static CitiesPresenter presenter;

    final int TYPE_ITEM_CITY = 0;
    final int TYPE_ITEM_LOCATION = 1;
    final int TYPES_AMOUNT = 2;

    public CitiesAdapter(ArrayList<City> cities) {
        super(MainActivity.getAppContext(), 0, cities);
    }

    public static CitiesAdapter getInstance(CitiesPresenter p) {
        if (adapter == null) {
            adapter = new CitiesAdapter(new ArrayList<City>());
            presenter = p;
        }
        return adapter;
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
        convertView.setTag(position);

        TextView cityName = (TextView) convertView.findViewById(R.id.cityName);
        TextView cityShortWeather = (TextView) convertView.findViewById(R.id.cityShotWeather);

        City city = getItem(position);
        cityName.setText(city.getName() + ", " +city.getCountry());
        cityShortWeather.setText("zaglyshka");
        //createRadioButton(convertView, position, false);
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

    public int getActiveCityId(int selectedPosition) {
        if (selectedPosition != -1) {
            return getItem(selectedPosition).getOw_id();
        }
        return 0;
    }

    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.deleteButton:
                int position = (int)((LinearLayout)view.getParent()).getTag();
                City city = getItem(position);
                remove(city);
                notifyDataSetChanged();
                presenter.deleteCity(city.getId());
                break;
            case R.id.findLocationButton:
                presenter.getCityByGPS();
                break;
        }
    }

    public void setDatas(ArrayList<City> list) {
        cities = list;
        clear();
        addAll(cities);
        notifyDataSetChanged();
    }



//    private void createRadioButton(final View convertView, final int position, boolean isChecked) {
//        AppCompatRadioButton r = (AppCompatRadioButton)convertView.findViewById(R.id.activeCity);
//        r.setChecked(false);
//        r.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                System.out.println("rd was pressed");
//                AppCompatRadioButton rd;
//                View listElementView;
//                if (selectedPosition != -1) {
//                    listElementView = listView.getChildAt(selectedPosition);
//                    listElementView.setBackgroundResource(R.drawable.shape_rounded_inactive);
//                    rd = (AppCompatRadioButton) listElementView.findViewById(R.id.activeCity);
//                    rd.setChecked(false);
//                }
//                selectedPosition = (int) view.getTag();
//                listElementView = listView.getChildAt(selectedPosition);
//                listElementView.setBackgroundResource(R.drawable.shape_rounded_active);
//                rd = (AppCompatRadioButton) view.findViewById(R.id.activeCity);
//                rd.setChecked(true);
//            }
//        });
//    }
}
