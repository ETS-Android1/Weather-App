package nikitin.weatherapp.com.weatherapptest3.View;

/**
 * Created by Влад on 23.07.2016.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import nikitin.weatherapp.com.weatherapptest3.Presenters.CitiesPresenter;
import nikitin.weatherapp.com.weatherapptest3.R;

public class CitiesFragment extends Fragment {
    private final String TAG = "CitiesFragment";
    private final String TITLE = "Choose City";

    private ListView citiesList;
    private static CitiesPresenter citiesPresenter;

    //Хуйня
    public static String activeCityName;
    private static CitiesFragment citiesView;

    public CitiesFragment() {
    }

    public static CitiesFragment getInstance() {
        if (citiesView == null) {
            citiesView = new CitiesFragment();
        }
        return citiesView;
    }

    public CitiesPresenter getPresenter() {
        return citiesPresenter;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_locations, container, false);
        citiesList = (ListView) rootView.findViewById(R.id.citiesList);
        citiesPresenter = new CitiesPresenter(getActivity(), this);

        getActivity().setTitle(TITLE);
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.setGroupVisible(R.id.main_menu_group, true);
    }

    public void addNewCity(int cityId) {
        citiesPresenter.addCity(cityId);
    }

    public void deleteCity(int position) {
        citiesPresenter.deleteCity(position);
    }

    public int getActiveCityId() {
        return citiesPresenter.getActiveCityId();
    }

    public void getCityByGPS() {
        citiesPresenter.getCityByGPS();
    }


    public ListView getCitiesList() {
        return citiesList;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        citiesPresenter.saveCities();
    }
}

//    private void updateCityWeather (final int cityId, final int position) {
//
//        WeatherAPI.getInstance().getWeatherByCityId(cityId, new ResponseCallback() {
//            @Override
//            public void onDataReceived(String output) {
//                City city = preferencesAPI.restoreCity(position);
//
//                System.out.println("CITY " + city);
//                Weather weather = JSONParser.getInstance().parseWeather(output);
//                city.setWeather(weather);
//                System.out.println("CITY " + city);
//                citiesListAdapter.add(city);
//                preferencesAPI.storeExistedCity(city, position);
//            }
//        });
//    }
