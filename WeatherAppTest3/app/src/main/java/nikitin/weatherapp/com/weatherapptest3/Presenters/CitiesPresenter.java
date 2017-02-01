package nikitin.weatherapp.com.weatherapptest3.Presenters;

import android.app.Activity;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nikitin.weatherapp.com.weatherapptest3.DataSharer;
import nikitin.weatherapp.com.weatherapptest3.DatabaseHandler;
import nikitin.weatherapp.com.weatherapptest3.MainActivity;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.City;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.Forecast;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.GeoStorm;
import nikitin.weatherapp.com.weatherapptest3.Model.ForecastModel.ForecastResponse;
import nikitin.weatherapp.com.weatherapptest3.Model.ForecastModel.ForecastWeather;
import nikitin.weatherapp.com.weatherapptest3.Preferences;
import nikitin.weatherapp.com.weatherapptest3.View.CitiesFragment;
import nikitin.weatherapp.com.weatherapptest3.Model.WeatherModel.WeatherResponse;
import nikitin.weatherapp.com.weatherapptest3.rest.ApiClient;
import nikitin.weatherapp.com.weatherapptest3.rest.OpenWeatherMapAPI;
import okhttp3.ResponseBody;
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
    private OpenWeatherMapAPI geoMagneticStormsAPI;
    private Preferences preferences;
    private final int GPS_ELEMENT_POSITION = 0;
    private DataSharer sharer;

    public CitiesPresenter(CitiesFragment view, Activity activity) {
        mainActivity = activity;
        this.view = view;
        openWeatherMapAPI = OpenWeatherMapAPI.getNewInstance(ApiClient.Urls.OPENWEATHER);
        geoMagneticStormsAPI = OpenWeatherMapAPI.getNewInstance(ApiClient.Urls.SPACEWEATHER);
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
                City city = parseCity(response.body());
                new AddCityTask().execute(city);
                view.addCity(city);
            }
            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
    public void getCityData(final long cityId) {
        openWeatherMapAPI.getWeatherByCityId(cityId, new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                City city = parseCity(response.body());
                sharer.shareCity(city);
                view.updateSmallWeather(city.getTemperature(), city.getWeather_type());
                new UpdateCityTask().execute(city);
            }
            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                new GetCityTask().execute(cityId);
            }
        });
    }

    private City parseCity(WeatherResponse weatherResponse) {
        City city = new City(weatherResponse.getId(),
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
        return city;
    }

    public void deleteCity(long position) {
        databaseHandler.deleteCity(position);
    }


    public List<City> restoreCities() {
        List<City> cities = databaseHandler.getAllCities();
        long gpsId = preferences.getGPSCityID();
        for (City city: cities) {
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
            if (mLastLocation == null){
                throw new SecurityException();
            }
            getCityByCoordinate(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        }
        catch(SecurityException ex) {
            Toast toast = Toast.makeText(MainActivity.getAppContext(), "Can't find your current position. Please make sure you turn on GPS", Toast.LENGTH_SHORT);
            toast.show();
        }
    }



    public void getForecast (final long cityId) {
        openWeatherMapAPI.getWeeklyForecastByCityId(cityId, new Callback<ForecastResponse>() {
            @Override
            public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {
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
                            forecastResponse.getWeathers().get(0).getDescription())
                    );
                }

                new UpdateForecastTask().execute(forecasts);
                System.out.println("THIS IS FORECAST");
                for (int i = 0; i < forecasts.size(); i++) {
                    System.out.println(forecasts.get(i));
                }
                sharer.shareForecast(forecasts);

                //Текущий отрезок прогноза который отдает сервер
                long current_dt = forecasts.get(0).getDate();
                getGeomagneticForecast(current_dt);
            }
            @Override
            public void onFailure(Call<ForecastResponse> call, Throwable t) {
                sharer.shareForecast(databaseHandler.getForecast(cityId, System.currentTimeMillis() / 1000));
                System.out.println("currernt mills " +(System.currentTimeMillis() / 1000));
                getGeomagneticForecast(System.currentTimeMillis() / 1000);
                List<GeoStorm> geoStorms = databaseHandler.getAllGeoStorms(System.currentTimeMillis() / 1000);
                for (GeoStorm geoStorm: geoStorms ) {
                    System.out.println("pish " +geoStorm);
                }
            }
        });
    }

    private void getGeomagneticForecast(final long current_dt) {
        geoMagneticStormsAPI.getGeomagneticStormData(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                System.out.println("there we are trying..." +response.message());
                //Считываем файл
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().byteStream()));
                String file = "";
                String line;
                try {
                    while ((line = reader.readLine())!= null) {
                        file += line;
                    }
                } catch (IOException ex) {
                    ex.getMessage();
                }


                ArrayList<GeoStorm> geoStormForecast = parseGeoStormForecast(current_dt, file);
                sharer.shareGeoStormForecast(geoStormForecast);
                new UpdateGeoStormsTask().execute(geoStormForecast);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("geomagnetic forecast offline");
                sharer.shareGeoStormForecast(databaseHandler.getAllGeoStorms(System.currentTimeMillis()/1000));
            }
        });
    }

    private ParsedDate parseDate(String file) {
        final String yearPatternString = "Issued:\\s(\\d\\d\\d\\d)";
        final String datePatternString = "forecast\\s(\\d\\d?)\\s(\\w\\w\\w)";
        Pattern p;
        Matcher m;
        //Парсим год
        p = Pattern.compile(yearPatternString);
        m = p.matcher(file);
        m.find();
        int year = Integer.parseInt(m.group(1));
        //Парсим месяц
        p = Pattern.compile(datePatternString);
        m = p.matcher(file);
        m.find();
        String month = m.group(2);
        //Парсим день
        p = Pattern.compile(datePatternString);
        m = p.matcher(file);
        m.find();
        int day = Integer.parseInt(m.group(1));
        return new ParsedDate(year, month, day);
    }

    private ArrayList<GeoStorm> parseGeoStormForecast(long current_dt, String file) {
        final String timeAndIndexPattern = "(\\d\\d)-\\d\\dUT\\s+(\\d)\\s+(\\d)\\s+(\\d)";
        ParsedDate date = parseDate(file);
        int [] kIndex = new int [24];
        long [] time = new long[24];
        SimpleDateFormat format = new SimpleDateFormat("yyyy MMM dd HH mm ss");

        Pattern p = Pattern.compile(timeAndIndexPattern);
        Matcher m = p.matcher(file);

        for (int i = 0; i < 8; i++) {
            m.find();
            kIndex[i] = Integer.parseInt(m.group(2));
            kIndex[i + 8] = Integer.parseInt(m.group(3));
            kIndex[i + 16] = Integer.parseInt(m.group(4));

            String firstDay = date.getYear() +" " +date.getMonth() +" " +date.getDay() +" " +m.group(1) +" " +"00" +" " +"00";
            String secondDay = date.getYear() +" " +date.getMonth() +" " +date.getDay()+1 +" " +m.group(1) +" " +"00" +" " +"00";
            String thirdDay = date.getYear() +" " +date.getMonth() +" " +date.getDay()+2 +" " +m.group(1) +" " +"00" +" " +"00";

            try {
                Date exactDate = format.parse(firstDay);
                time[i] = exactDate.getTime()/1000;
                exactDate = format.parse(secondDay);
                time[i + 8] = exactDate.getTime()/1000;
                exactDate = format.parse(thirdDay);
                time[i + 16] = exactDate.getTime()/1000;
            } catch(ParseException ex) {
                ex.printStackTrace();
            }
        }
        int index = 0;
        for (int i = 0; i < 24; i++) {
            if (time[i] == current_dt) {
                index = i;
                break;
            }
        }

        ArrayList<GeoStorm> geoStorms = new ArrayList<>(24);
        for (int i = index; i < 24; i++) {
            geoStorms.add(new GeoStorm(time[i], kIndex[i]));
        }
        return geoStorms;
    }

    private class ParsedDate {
        int year;
        String month;
        int day;

        ParsedDate(int year, String month, int day) {
            this.year = year;
            this.month = month;
            this.day = day;
        }

        public int getYear() {
            return year;
        }
        public void setYear(int year) {
            this.year = year;
        }

        public String getMonth() {
            return month;
        }
        public void setMonth(String month) {
            this.month = month;
        }

        public int getDay() {
            return day;
        }
        public void setDay(int day) {
            this.day = day;
        }
    }

    private void getCityByCoordinate(double latitude, double longitude) {
        openWeatherMapAPI.getWeatherByCityCoordinate(latitude, longitude, new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                City city = parseCity(response.body());
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

    private class UpdateGeoStormsTask extends AsyncTask<ArrayList<GeoStorm>, Void, Void> {
        @Override
        protected Void doInBackground(ArrayList<GeoStorm>... geoStorms) {
            databaseHandler.updateGeoStorms(geoStorms[0]);
            return null;
        }
    }
}
