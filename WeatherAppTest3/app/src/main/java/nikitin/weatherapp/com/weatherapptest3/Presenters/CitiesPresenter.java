package nikitin.weatherapp.com.weatherapptest3.Presenters;

import android.app.Activity;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import nikitin.weatherapp.com.weatherapptest3.DatabaseHandler;
import nikitin.weatherapp.com.weatherapptest3.MainActivity;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.City;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.DailyForecast;
import nikitin.weatherapp.com.weatherapptest3.Preferences;
import nikitin.weatherapp.com.weatherapptest3.View.CitiesFragment;
import nikitin.weatherapp.com.weatherapptest3.Model.WeatherModel.Data;
import nikitin.weatherapp.com.weatherapptest3.Model.WeatherModel.WeatherResponse;
import nikitin.weatherapp.com.weatherapptest3.rest.OpenWeatherMapAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Влад on 09.10.2016.
 */
public class CitiesPresenter implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private CitiesFragment view;
    private DatabaseHandler databaseHandler;

    private Activity mainActivity;
    private GoogleApiClient googleApiClient;
    private OpenWeatherMapAPI openWeatherMapAPI;
    private Preferences preferences;
    private final int GPS_ELEMENT_POSITION = 0;

    public CitiesPresenter(CitiesFragment view, Activity activity) {
        mainActivity = activity;
        this.view = view;
        openWeatherMapAPI = OpenWeatherMapAPI.getInstance();
        preferences = Preferences.getInstance(mainActivity);
        databaseHandler = DatabaseHandler.getInstance(MainActivity.getAppContext());
    }

    //----------------------------------------------------------------------------------------------
    //---------------------------- Methods For Working with cities list ----------------------------

    public void getCityData(int cityId) {
        openWeatherMapAPI.getWeatherByCityId(cityId, new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                response.body().setData(convertToCelsius(response.body().getData()));
                City city = new City();
                city.setOw_id(response.body().getId());
                city.setName(response.body().getName());
                city.setCountry(response.body().getSys().getCountry());
                city.setLatitude(response.body().getCoordinates().getLatitude());
                city.setLongitude(response.body().getCoordinates().getLongitude());
                new AddCityTask().execute(city);
                view.addCity(city);
            }
            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void deleteCity(int position) {
        databaseHandler.deleteCity(position);
    }

    public ArrayList<City> restoreCities() {
        ArrayList<City> cities = databaseHandler.getAllCities();
        long gpsId = preferences.getGPSCityID();
        for (City city: cities) {
            if (city.getId() == gpsId) {
                cities.remove(city);
                cities.add(0, city);
                return cities;
            }
        }
        City GPSCity = new City(0, 0, "Find location by GPS", "", 0, 0);
        cities.add(0, GPSCity);
        long gpsCityId = databaseHandler.addCity(GPSCity);
        preferences.putGPSCityId(gpsCityId);
        return cities;
    }

    public Data convertToCelsius(Data data) {
        double KELVIN_TO_CELCIUM = 273.0;
        double roundedTemp = new BigDecimal(data.getTemp()-KELVIN_TO_CELCIUM).setScale(2, RoundingMode.UP).doubleValue(); //double)((int)Math.round(data.getTemp()*10)/10);
        double roundedTempMin = Math.round(data.getTemp_min()*10)/10;
        double roundedTempMax = Math.round(data.getTemp_max()*10)/10.0;

        data.setTemp(roundedTemp);
        data.setTemp_max(roundedTempMin - KELVIN_TO_CELCIUM);
        data.setTemp_min(roundedTempMax - KELVIN_TO_CELCIUM);
        return data;
    }






    //----------------------------------------------------------------------------------------------
    //---------------------------------- GPS Methods -----------------------------------------------

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
            System.out.println(mLastLocation.getLatitude() + " " +mLastLocation.getLongitude());
            if (mLastLocation != null) {
                getCityByCoordinate(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            }
        }
        catch(SecurityException ex) {
            System.out.println("EXEPTION");
        }
    }

    private void getCityByCoordinate(double latitude, double longitude) {
        openWeatherMapAPI.getWeatherByCityCoordinate(latitude, longitude, new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                City city = new City();
                city.setOw_id(response.body().getId());
                city.setName(response.body().getName());
                city.setCountry(response.body().getSys().getCountry());
                city.setLongitude(response.body().getCoordinates().getLongitude());
                city.setLatitude(response.body().getCoordinates().getLatitude());
                view.updateGPSItem(city, GPS_ELEMENT_POSITION);
            }
            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {}
        });
    }
    @Override
    public void onConnectionSuspended(int i) {}
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    class AddCityTask extends AsyncTask<City,Void,Void> {
        @Override
        protected Void doInBackground(City... f) {
            databaseHandler.addCity(f[0]);
            return null;
        }
    }
}
