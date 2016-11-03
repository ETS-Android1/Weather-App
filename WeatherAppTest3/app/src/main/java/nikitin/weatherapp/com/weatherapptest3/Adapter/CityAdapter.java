package nikitin.weatherapp.com.weatherapptest3.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.sql.SQLOutput;
import java.util.ArrayList;
import nikitin.weatherapp.com.weatherapptest3.Model.WeatherModel.WeatherResponse;
import nikitin.weatherapp.com.weatherapptest3.R;
import nikitin.weatherapp.com.weatherapptest3.View.CitiesFragment;
import nikitin.weatherapp.com.weatherapptest3.View.TabsPagerAdapter;
import nikitin.weatherapp.com.weatherapptest3.Presenters.CitiesPresenter;
/**
 * Created by Влад on 28.07.2016.
 */
public class CityAdapter extends ArrayAdapter<WeatherResponse> {
    public static int selectedPosition = -1;
    Activity mainActivity;
    CitiesPresenter citiesPresenter;
    private static CityAdapter cityAdapter;
    ListView listView;

    final int TYPE_ITEM_CITY = 0;
    final int TYPE_ITEM_LOCATION = 1;
    final int TYPES_AMOUNT = 2;

    private CityAdapter(final Context context, ArrayList<WeatherResponse> cities, ListView listView) {
        super(context, 0, cities);
        this.mainActivity = (Activity)context;
        this.listView = listView;

        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                for (int i = 0; i < adapterView.getCount(); i++) {
                    selectedPosition = pos;
                    RadioButton rd;
                    if (i == pos) {
                        view.setBackgroundResource(R.drawable.shape_rounded_active);
                        rd = (RadioButton) view.findViewById(R.id.activeCity);
                        rd.setChecked(i == pos);
                    } else {
                        adapterView.getChildAt(i).setBackgroundResource(R.drawable.shape_rounded_inactive);
                        rd = (RadioButton) adapterView.getChildAt(i).findViewById(R.id.activeCity);
                        rd.setChecked(i == pos);
                    }

                    if (getActiveCityId() == 0) {
                        continue;
                    }

                    TabsPagerAdapter.mainWindowFragment.updateWeather(getActiveCityId());
                }
                notifyDataSetChanged();
            }
        });
    }

    public static CityAdapter getInstance(Context context, ArrayList<WeatherResponse> cities, ListView listView) {
        if (cityAdapter == null) {
            cityAdapter = new CityAdapter(context, cities, listView);
        }
        return cityAdapter;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        System.out.println("Parent" + parent);
        int type = getItemViewType(position);
        if (convertView == null) {
            switch(type) {
                case TYPE_ITEM_CITY:
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_city, parent, false);
                    break;

                case TYPE_ITEM_LOCATION:
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_city_2, parent, false);
                    break;
            }
        }
        TextView cityName = (TextView) convertView.findViewById(R.id.cityName);
        TextView cityShortWeather = (TextView) convertView.findViewById(R.id.cityShotWeather);

        WeatherResponse city = this.getItem(position);
        cityName.setText(city.getName());
        cityShortWeather.setText(city.getData().getTemp() + ", " + city.getWeathers().get(0).getMain());
        RadioButton rd = (RadioButton) convertView.findViewById(R.id.activeCity);
        //rd.setEnabled(false);
        createRadioButton(convertView, position, false);
        if (type == TYPE_ITEM_CITY) {
            createDeleteButton(convertView, position);
        } else if (type == TYPE_ITEM_LOCATION) {
            createFindLocationButton(convertView, position);
        }

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

    public int getActiveCityId() {
        if (selectedPosition != -1) {
            return getItem(selectedPosition).getId();
        }
        return 0;
    }

    private void createRadioButton(final View convertView, final int position, boolean isChecked) {
        RadioButton r = (RadioButton)convertView.findViewById(R.id.activeCity);
        r.setTag(position);
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPosition = (int) view.getTag();

                for (int i = 0; i < getCount(); i++) {
                    LinearLayout listElement = (LinearLayout) listView.getChildAt(i);
                    listElement.setBackgroundResource(R.drawable.shape_rounded_inactive);
                    RadioButton radioButton = (RadioButton) listElement.findViewById(R.id.activeCity);
                    radioButton.setChecked(false);
                }

                LinearLayout element = (LinearLayout) view.getParent();
                element.setBackgroundResource(R.drawable.shape_rounded_active);
                RadioButton rb = (RadioButton) view;
                rb.setChecked(true);

                if (getActiveCityId() == 0) {
                    return;
                }

                TabsPagerAdapter.mainWindowFragment.updateWeather(getActiveCityId());
                cityAdapter.notifyDataSetChanged();
            }
        });
    }

    private void createDeleteButton(View convertView, int position) {
        Button deleteButton = (Button)convertView.findViewById(R.id.deleteButton);
        deleteButton.setTag(position);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CitiesFragment.getInstance().deleteCity((int) view.getTag());
            }
        });
    }

    private void createFindLocationButton(View convertView, int position) {
        Button findLocationButton = (Button)convertView.findViewById(R.id.findLocationButton);
        findLocationButton.setTag(position);
        findLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CitiesFragment.getInstance().getCityByGPS();
            }
        });
    }
}
