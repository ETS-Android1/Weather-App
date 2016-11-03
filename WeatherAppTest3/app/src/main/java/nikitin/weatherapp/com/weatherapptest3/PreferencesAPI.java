package nikitin.weatherapp.com.weatherapptest3;

import android.app.Activity;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import nikitin.weatherapp.com.weatherapptest3.Adapter.CityAdapter;
import nikitin.weatherapp.com.weatherapptest3.View.CitiesFragment;
import nikitin.weatherapp.com.weatherapptest3.Model.WeatherModel.WeatherResponse;

/**
 * Created by Влад on 04.08.2016.
 */
public class PreferencesAPI {
    String TAG = "PreferencesAPI";
    final String CITIES_AMOUNT = "Amount";
    final String CITY_ITEM = "city_%1$s";

    static PreferencesAPI preferencesAPI;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    public int citiesAmount = 0;
    private Gson gson;


    private PreferencesAPI(Activity activity) {
        preferences = activity.getPreferences(activity.MODE_PRIVATE);
        editor = preferences.edit();
        //citiesAmount = restoreCitiesAmount();
        gson = new Gson();
    }

    public static PreferencesAPI getInstance(Activity activity) {
        if (preferencesAPI == null) {
            preferencesAPI = new PreferencesAPI(activity);
        }
        return preferencesAPI;
    }

    public void saveCities() {
        CityAdapter cityAdapter = CitiesFragment.getInstance().getPresenter().getCityAdapter();
        editor.putInt(CITIES_AMOUNT, cityAdapter.getCount()-1);
        for (int i = 1; i < cityAdapter.getCount(); i++) {
            WeatherResponse cityItem = cityAdapter.getItem(i);
            String cityItemJSON = gson.toJson(cityItem, WeatherResponse.class);
            editor.putString(String.format(CITY_ITEM, i), cityItemJSON);
        }
        editor.apply();
    }

    public List <WeatherResponse> restoreCities() {
        List<WeatherResponse> citiesList = new ArrayList<>();
        int cityAmount = preferences.getInt(CITIES_AMOUNT, 0);
        for (int i = 1; i <= cityAmount; i++) {
            String cityItemJSON = preferences.getString(String.format(CITY_ITEM, i), "");
            WeatherResponse cityItem = gson.fromJson(cityItemJSON, WeatherResponse.class);
            citiesList.add(cityItem);
        }
        return citiesList;
    }

    public void clear() {
        editor.clear();
        editor.apply();
    }
}
