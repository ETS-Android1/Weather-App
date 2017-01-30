package nikitin.weatherapp.com.weatherapptest3.View;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import nikitin.weatherapp.com.weatherapptest3.Model.SpaceWeather.GeomagneticResponse;
import nikitin.weatherapp.com.weatherapptest3.Model.SpaceWeather.GeomagneticStorm;
import nikitin.weatherapp.com.weatherapptest3.Presenters.ExtremeWeatherPresenter;
import nikitin.weatherapp.com.weatherapptest3.R;
import nikitin.weatherapp.com.weatherapptest3.rest.ApiClient;
import nikitin.weatherapp.com.weatherapptest3.rest.OpenWeatherMapAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Uladzislau_Nikitsin on 1/20/2017.
 */

public class ExtremeWeatherFragment extends Fragment {
    ExtremeWeatherPresenter presenter;
    View view;
    public ExtremeWeatherFragment(){
        this.presenter = new ExtremeWeatherPresenter(this);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_extreme_weather, container, false);
        }
        presenter.getSpaceWeatherData();
        return view;
    }

    public void setGeomagneticBox(GeomagneticStorm geomagneticStorm) {
        System.out.println("message");
        System.out.println(geomagneticStorm.getMessage());
    }





}
