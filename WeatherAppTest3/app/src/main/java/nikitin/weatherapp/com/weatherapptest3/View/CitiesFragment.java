package nikitin.weatherapp.com.weatherapptest3.View;

/**
 * Created by Влад on 23.07.2016.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import nikitin.weatherapp.com.weatherapptest3.Adapter.CitiesAdapter;
import nikitin.weatherapp.com.weatherapptest3.DataSharer;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.City;
import nikitin.weatherapp.com.weatherapptest3.Presenters.CitiesPresenter;
import nikitin.weatherapp.com.weatherapptest3.R;

public class CitiesFragment extends Fragment implements ListView.OnItemClickListener{
    public static final String TITLE = "Cities List";
    private static CitiesFragment fragment;
    private ListView citiesList;
    private CitiesPresenter presenter;
    private CitiesAdapter adapter;
    private View view;
    private int selectedItemPosition = -1;
    private DataSharer sharer;
    public static CitiesFragment getInstance() {
        if (fragment == null) fragment = new CitiesFragment();
        return fragment;
    }
    public CitiesPresenter getPresenter() {
        return presenter;
    }

    //----------------------------------------------------------------------------------------------
    //--------------------------------------- Life Cycle -------------------------------------------
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sharer = (DataSharer) context;
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
        System.out.println("Item position " +selectedItemPosition);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.setGroupVisible(R.id.cities_group, true);
        menu.setGroupVisible(R.id.main_menu_group, false);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter = null;
        view = null;
    }
    //----------------------------------------------------------------------------------------------
    //

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
        if (selectedItemPosition != -1) {
            adapterView.getChildAt(selectedItemPosition).setBackgroundResource(R.drawable.shape_rounded_inactive);
        }
        selectedItemPosition = pos;
        adapter.setSelectedPosition(selectedItemPosition);
        view.setBackgroundResource(R.drawable.shape_rounded_active);
        presenter.getCityData(adapter.getItem(pos).getId());
        presenter.getForecast(adapter.getItem(pos).getId());
    }

    public void updateGPSItem(City city, int pos) {
        adapter.remove(adapter.getItem(pos));
        adapter.insert(city, pos);
    }

    public void addCityData(int cityId) {
        presenter.addCityData(cityId);
    }
    public void addCity(City city) {
        adapter.add(city);
        adapter.notifyDataSetChanged();
    }
    public String getActiveCityName() {return adapter.getActiveCityName(selectedItemPosition);}
}