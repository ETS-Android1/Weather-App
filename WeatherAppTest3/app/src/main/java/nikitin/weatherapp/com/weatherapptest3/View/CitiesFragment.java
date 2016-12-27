package nikitin.weatherapp.com.weatherapptest3.View;

/**
 * Created by Влад on 23.07.2016.
 */

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import nikitin.weatherapp.com.weatherapptest3.Adapter.CitiesAdapter;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.City;
import nikitin.weatherapp.com.weatherapptest3.Presenters.CitiesPresenter;
import nikitin.weatherapp.com.weatherapptest3.R;

public class CitiesFragment extends Fragment implements ListView.OnItemClickListener{
    private final String TAG = "CitiesFragment";
    public static final String TITLE = "Choose City";
    private static CitiesFragment fragment;
    private ListView citiesList;
    private CitiesPresenter presenter;
    private CitiesAdapter adapter;
    private View view;
    private int selectedPosition = -1;

    public static CitiesFragment getInstance() {
        if (fragment == null) fragment = new CitiesFragment();
        return fragment;
    }

    public CitiesPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        presenter = new CitiesPresenter(this, getActivity());
        adapter = CitiesAdapter.getInstance(presenter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_locations, container, false);
            citiesList = (ListView) view.findViewById(R.id.citiesList);
            adapter.setDatas(presenter.restoreCities());
            citiesList.setOnItemClickListener(this);
            citiesList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
        if (selectedPosition != -1) {
            adapterView.getChildAt(selectedPosition).setBackgroundResource(R.drawable.shape_rounded_inactive);
        }
        selectedPosition = pos;
        //Тут подумай
        //mainActivity.setCurrentCityId(adapter.getItem(selectedPosition).getOw_id());
        view.setBackgroundResource(R.drawable.shape_rounded_active);
        //rd = (RadioButton) view.findViewById(R.id.activeCity);
        //rd.setChecked(true);
        TabsPagerAdapter.currentCityId = getActiveCityId();
        TabsPagerAdapter.mainWindowFragment.updateWeather(getActiveCityId());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.setGroupVisible(R.id.cities_group, true);
    }

    public void addCity(City city) {
        adapter.add(city);
        adapter.notifyDataSetChanged();
    }
    public void updateGPSItem(City city, int pos) {
        adapter.remove(adapter.getItem(pos));
        adapter.insert(city, pos);
    }
    public void getCityData(int cityId) {
        presenter.getCityData(cityId);
    }

    public int getActiveCityId() {
        return adapter.getActiveCityId(selectedPosition);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter = null;
        view = null;
    }
}

