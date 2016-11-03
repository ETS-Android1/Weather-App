package nikitin.weatherapp.com.weatherapptest3.Presenters;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import nikitin.weatherapp.com.weatherapptest3.Adapter.CityAdapter;
import nikitin.weatherapp.com.weatherapptest3.View.CitiesFragment;
import nikitin.weatherapp.com.weatherapptest3.PreferencesAPI;
import nikitin.weatherapp.com.weatherapptest3.Model.WeatherModel.Data;
import nikitin.weatherapp.com.weatherapptest3.Model.WeatherModel.WeatherResponse;
import nikitin.weatherapp.com.weatherapptest3.Model.WeatherModel.Wind;
import nikitin.weatherapp.com.weatherapptest3.Model.WeatherModel.Weather;
import nikitin.weatherapp.com.weatherapptest3.rest.OpenWeatherMapAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Влад on 09.10.2016.
 */
public class CitiesPresenter implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private CitiesFragment citiesView;
    private CityAdapter citiesListAdapter;

    private Activity mainActivity;
    private GoogleApiClient googleApiClient;
    private OpenWeatherMapAPI openWeatherMapAPI;
    private PreferencesAPI preferencesAPI;

    private final int GPS_ELEMENT_POSITION = 0;

    public CitiesPresenter(Activity activity, CitiesFragment citiesView) {
        this.citiesView = citiesView;
        this.mainActivity = activity;

        openWeatherMapAPI = OpenWeatherMapAPI.getInstance();
        preferencesAPI = PreferencesAPI.getInstance(activity);
        //preferencesAPI.clear();

        citiesListAdapter = CityAdapter.getInstance(activity, new ArrayList<WeatherResponse>(), citiesView.getCitiesList());
        citiesView.getCitiesList().setAdapter(citiesListAdapter);


        createFindLocationElement();
        restoreCities();
    }

    public int getActiveCityId() {
        return citiesListAdapter.getActiveCityId();
    }

    //----------------------------------------------------------------------------------------------
    //---------------------------- Methods For Working with cities list ----------------------------

    public void addCity(int cityId) {
        openWeatherMapAPI.getWeatherByCityId(cityId, new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                WeatherResponse weatherResponse = response.body();
                response.body().setData(convertToCelcium(response.body().getData()));
                citiesListAdapter.add(weatherResponse);
                citiesListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void deleteCity (int position) {
        citiesListAdapter.remove(citiesListAdapter.getItem(position));
        citiesListAdapter.notifyDataSetChanged();
    }

    private void restoreCities() {
        citiesListAdapter.addAll(preferencesAPI.restoreCities());
        citiesListAdapter.notifyDataSetChanged();
    }

    public void saveCities() {
        preferencesAPI.saveCities();
    }

    public CityAdapter getCityAdapter() {
        return citiesListAdapter;
    }

    public Data convertToCelcium(Data data) {
        double KELVIN_TO_CELCIUM = 273.0;
        double roundedTemp = new BigDecimal(data.getTemp()-KELVIN_TO_CELCIUM).setScale(2, RoundingMode.UP).doubleValue(); //double)((int)Math.round(data.getTemp()*10)/10);
        System.out.println("PISH" +roundedTemp);
        double roundedTempMin = Math.round(data.getTemp_min()*10)/10;
        double roundedTempMax = Math.round(data.getTemp_max()*10)/10.0;

        data.setTemp(roundedTemp);
        System.out.println("PISH" + data.getTemp());
        data.setTemp_max(roundedTempMin - KELVIN_TO_CELCIUM);
        data.setTemp_min(roundedTempMax-KELVIN_TO_CELCIUM);
        return data;
    }

    //----------------------------------------------------------------------------------------------
    //---------------------------------- GPS Methods -----------------------------------------------

    private void createFindLocationElement() {
        //Создаю первый элемент списка - поиск по GPS. Почти все поля - пока не определены.
        Data data = new Data(0, 0, 0, 0, 0, 0, 0);
        List<Weather> weathers = new ArrayList<>();
        weathers.add(new Weather(0, "unknown location", null, null));
        Wind wind = new Wind(0, 0);
        WeatherResponse weatherResponse = new WeatherResponse(0, "Find location by GPS", null, weathers, data, wind, null, 0, null);
        citiesListAdapter.add(weatherResponse);
        citiesListAdapter.notifyDataSetChanged();
    }

    public void getCityByGPS() {
        googleApiClient = new GoogleApiClient.Builder(mainActivity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Location mLastLocation;
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            //System.out.println(mLastLocation.getLatitude() + " " +mLastLocation.getLongitude());
            //if (mLastLocation != null) {
                getCityByCoordinate(43,43);
            //}
        }
        catch(SecurityException ex) {
            System.out.println("EXEPTION");
        }
    }

    private void getCityByCoordinate(double latitude, double longitude) {
        openWeatherMapAPI.getWeatherByCityCoordinate(latitude, longitude, new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                response.body().setData(convertToCelcium(response.body().getData()));
                citiesListAdapter.remove(citiesListAdapter.getItem(GPS_ELEMENT_POSITION));
                citiesListAdapter.insert(response.body(), GPS_ELEMENT_POSITION);
            }
            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {
        System.out.println("pish onConnected1");
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        System.out.println("pish onConnected2");
    }
}
