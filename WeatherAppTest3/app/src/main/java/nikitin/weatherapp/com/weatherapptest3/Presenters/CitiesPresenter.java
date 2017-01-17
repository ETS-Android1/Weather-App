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

import nikitin.weatherapp.com.weatherapptest3.DataSharer;
import nikitin.weatherapp.com.weatherapptest3.DatabaseHandler;
import nikitin.weatherapp.com.weatherapptest3.MainActivity;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.City;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.DailyForecast;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.Forecast;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.WeeklyForecast;
import nikitin.weatherapp.com.weatherapptest3.Model.ForecastModel.ForecastResponse;
import nikitin.weatherapp.com.weatherapptest3.Model.ForecastModel.ForecastWeather;
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
    private DataSharer sharer;
    public CitiesPresenter(CitiesFragment view, Activity activity) {
        mainActivity = activity;
        this.view = view;
        openWeatherMapAPI = OpenWeatherMapAPI.getInstance();
        preferences = Preferences.getInstance(mainActivity);
        sharer = (DataSharer) activity;
        databaseHandler = DatabaseHandler.getInstance(MainActivity.getAppContext());
    }

    //----------------------------------------------------------------------------------------------
    //---------------------------- Methods For Working with cities list ----------------------------

    public void addCityData(final long cityId) {
        openWeatherMapAPI.getWeatherByCityId(cityId, new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                System.out.println("cityId " +cityId);
                System.out.println("message " +response.code() +" " +response.message() +" " +response.errorBody() +" " +response.isSuccessful() +" " +response.headers() +" " +response.raw());
                WeatherResponse weatherResponse = response.body();
                City city = new City(weatherResponse.getId(),
                        weatherResponse.getName(),
                        weatherResponse.getSys().getCountry(),
                        weatherResponse.getCoordinates().getLatitude(),
                        weatherResponse.getCoordinates().getLongitude(),
                        City.kelvinToCelsius((int)weatherResponse.getData().getTemp()),
                        weatherResponse.getWeathers().get(0).getMain(),
                        weatherResponse.getData().getHumidity(),
                        weatherResponse.getWind().getSpeed(),
                        (int)weatherResponse.getData().getPressure(),
                        (int)weatherResponse.getWind().getDeg(),
                        weatherResponse.getDt(),
                        weatherResponse.getWeathers().get(0).getId(),
                        weatherResponse.getWeathers().get(0).getDescription());
                new AddCityTask().execute(city);
                view.addCity(city);
            }
            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) { t.printStackTrace();
            }
        });
    }
    public void getCityData(final long cityId) {
        openWeatherMapAPI.getWeatherByCityId(cityId, new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                System.out.println("cityId " +cityId);
                System.out.println("message " +response.code() +" " +response.message() +" " +response.errorBody() +" " +response.isSuccessful() +" " +response.headers() +" " +response.raw());
                WeatherResponse weatherResponse = response.body();
                City city = new City(cityId,
                        weatherResponse.getName(),
                        weatherResponse.getSys().getCountry(),
                        weatherResponse.getCoordinates().getLatitude(),
                        weatherResponse.getCoordinates().getLongitude(),
                        City.kelvinToCelsius(weatherResponse.getData().getTemp()),
                        weatherResponse.getWeathers().get(0).getMain(),
                        weatherResponse.getData().getHumidity(),
                        weatherResponse.getWind().getSpeed(),
                        (int)weatherResponse.getData().getPressure(),
                        (int)weatherResponse.getWind().getDeg(),
                        weatherResponse.getDt(),
                        weatherResponse.getWeathers().get(0).getId(),
                        weatherResponse.getWeathers().get(0).getDescription());
                sharer.shareCity(city);
                new UpdateCityTask().execute(city);
            }
            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                new GetCityTask().execute(cityId);
            }
        });
    }

    public void deleteCity(long position) {
        databaseHandler.deleteCity(position);
    }


    public ArrayList<City> restoreCities() {
        ArrayList<City> cities = databaseHandler.getAllCities();
        for (City city: cities) {
            System.out.println("pop");
            System.out.println(city);
        }
        long gpsId = preferences.getGPSCityID();
        for (City city: cities) {
            System.out.println("printing cities");
            System.out.println(city);
            if (city.getId() == gpsId) {
                cities.remove(city);
                cities.add(0, city);
                return cities;
            }
        }
        City GPSCity = new City(0, "GPS", "", 0, 0, 0, "Find location by GPS", 0, 0, 0, 0, 0, 0, "");
        cities.add(0, GPSCity);
        long gpsCityId = databaseHandler.addCity(GPSCity);
        preferences.putGPSCityId(gpsCityId);
        return cities;
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
        }
    }

    public void getForecast (final long cityId) {
        openWeatherMapAPI.getWeeklyForecastByCityId(cityId, new Callback<ForecastResponse>() {
            @Override
            public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {
                System.out.println("cityId " +cityId);
                System.out.println("message " +response.code() +" " +response.message() +" " +response.errorBody() +" " +response.isSuccessful() +" " +response.headers() +" " +response.raw());
                ArrayList<Forecast> forecasts = new ArrayList<>(40);
                for (ForecastWeather forecastResponse : response.body().getList()) {
                    forecasts.add(new Forecast(0, cityId,
                            forecastResponse.getWeathers().get(0).getMain(),
                            Forecast.kelvinToCelsius((int)forecastResponse.getData().getTemp()),
                            forecastResponse.getData().getHumidity(),
                            forecastResponse.getWind().getSpeed(),
                            (int)forecastResponse.getData().getPressure(),
                            (int)forecastResponse.getWind().getDeg(),
                            forecastResponse.getDt(),
                            forecastResponse.getWeathers().get(0).getId(),
                            forecastResponse.getWeathers().get(0).getDescription()));
                }
                new UpdateForecastTask().execute(forecasts);
                sharer.shareForecast(forecasts);
            }
            @Override
            public void onFailure(Call<ForecastResponse> call, Throwable t) {
                sharer.shareForecast(databaseHandler.getForecast(cityId));
            }
        });
    }

    private void getCityByCoordinate(double latitude, double longitude) {
        openWeatherMapAPI.getWeatherByCityCoordinate(latitude, longitude, new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                WeatherResponse weatherResponse = response.body();
                City city = new City(weatherResponse.getId(),
                        weatherResponse.getName(),
                        weatherResponse.getSys().getCountry(),
                        weatherResponse.getCoordinates().getLatitude(),
                        weatherResponse.getCoordinates().getLongitude(),
                        City.kelvinToCelsius((int)weatherResponse.getData().getTemp()),
                        weatherResponse.getWeathers().get(0).getMain(),
                        weatherResponse.getData().getHumidity(),
                        weatherResponse.getWind().getSpeed(),
                        (int)weatherResponse.getData().getPressure(),
                        (int)weatherResponse.getWind().getDeg(),
                        weatherResponse.getDt(),
                        weatherResponse.getWeathers().get(0).getId(),
                        weatherResponse.getWeathers().get(0).getDescription());
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


    //----------------------------------------------------------------------------------------------
    //------------------------------ AsyncTasks for db ---------------------------------------------
    class AddCityTask extends AsyncTask<City,Void,Void> {
        @Override
        protected Void doInBackground(City... f) {
            databaseHandler.addCity(f[0]);
            return null;
        }
    }
    private class UpdateCityTask extends AsyncTask<City, Void, Void> {
        @Override
        protected Void doInBackground(City... f) {
            databaseHandler.updateCity(f[0]);
            return null;
        }
    }
    private class UpdateForecastTask extends AsyncTask<ArrayList<Forecast>, Void, Void> {
        @Override
        protected Void doInBackground(ArrayList<Forecast>... forecasts) {
            databaseHandler.updateAllForecasts(forecasts[0]);
            return null;
        }
    }
    private class GetCityTask extends AsyncTask<Long, Void, City> {
        @Override
        protected City doInBackground(Long... cityIds) {
            return databaseHandler.getCity(cityIds[0]);
        }
        @Override
        protected void onPostExecute(City city) {
            sharer.shareCity(city);
        }
    }
}
