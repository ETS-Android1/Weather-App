package nikitin.weatherapp.com.weatherapptest3;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;

import nikitin.weatherapp.com.weatherapptest3.Model.Database.City;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.CurrentWeather;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.DailyForecast;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.Forecast;
import nikitin.weatherapp.com.weatherapptest3.Model.WeatherModel.Weather;

/**
 * Created by Влад on 28.11.2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    private static DatabaseHandler databaseHandler;
    private static final int DATABASE_VERSION = 25;
    private static final String DATABASE_NAME = "weatherAppDatabase";

    private static final String TABLE_CITY = "city";
    private static final String KEY_CITY_ID = "id";
    private static final String KEY_CITY_NAME = "name";
    private static final String KEY_CITY_COUNTRY = "country";
    private static final String KEY_CITY_LATITUDE = "latitude";
    private static final String KEY_CITY_LONGITUDE = "longitude";
    private static final String KEY_CITY_TEMPERATURE = "temperature";
    private static final String KEY_CITY_WEATHER_TYPE = "weather_type";
    private static final String KEY_CITY_HUMIDITY = "humidity";
    private static final String KEY_CITY_WIND_SPEED = "wind_speed";
    private static final String KEY_CITY_PRESSURE = "pressure";
    private static final String KEY_CITY_WIND_DIRECTION = "wind_direction";
    private static final String KEY_CITY_DATE = "date";

    private static final String TABLE_FORECAST = "forecast";
    private static final String KEY_FORECAST_ID = "id";
    private static final String KEY_FORECAST_FK_CITY_ID = "fk_city_id";
    private static final String KEY_FORECAST_WEATHER_TYPE = "weather_type";
    private static final String KEY_FORECAST_TEMPERATURE = "temperature";
    private static final String KEY_FORECAST_HUMIDITY = "humidity";
    private static final String KEY_FORECAST_WIND_SPEED = "wind_speed";
    private static final String KEY_FORECAST_PRESSURE = "pressure";
    private static final String KEY_FORECAST_WIND_DIRECTION = "wind_direction";
    private static final String KEY_FORECAST_DATE = "date";

    private DatabaseHandler (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseHandler getInstance(Context context) {
        if (databaseHandler == null) {
            databaseHandler = new DatabaseHandler(context);
        }
        return databaseHandler;
    }

    public void onCreate(SQLiteDatabase db) {
        String CREATE_CITY_TABLE = "CREATE TABLE " +TABLE_CITY + " ("
                +KEY_CITY_ID +" INTEGER PRIMARY KEY, "
                +KEY_CITY_NAME +" TEXT, "
                +KEY_CITY_COUNTRY +" TEXT, "
                +KEY_CITY_LATITUDE +" REAL, "
                +KEY_CITY_LONGITUDE +" REAL, "
                +KEY_CITY_TEMPERATURE +" INTEGER, "
                +KEY_CITY_WEATHER_TYPE +" TEXT, "
                +KEY_CITY_HUMIDITY +" INTEGER, "
                +KEY_CITY_WIND_SPEED +" REAL, "
                +KEY_CITY_PRESSURE +" INTEGER, "
                +KEY_CITY_WIND_DIRECTION +" INTEGER"
                +KEY_CITY_DATE +" INTEGER" +")";
        db.execSQL(CREATE_CITY_TABLE);

        String CREATE_FORECAST_TABLE = "CREATE TABLE " +TABLE_FORECAST +" ("
                +KEY_FORECAST_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +KEY_FORECAST_FK_CITY_ID +" INTEGER, "
                +KEY_FORECAST_WEATHER_TYPE +" TEXT, "
                +KEY_FORECAST_TEMPERATURE +" INTEGER, "
                +KEY_FORECAST_HUMIDITY +" INTEGER, "
                +KEY_FORECAST_WIND_SPEED +" REAL, "
                +KEY_FORECAST_PRESSURE +" INTEGER, "
                +KEY_FORECAST_WIND_DIRECTION +" INTEGER, "
                +KEY_FORECAST_DATE +" INTEGER" +")";

        db.execSQL(CREATE_FORECAST_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_CITY);
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_FORECAST);
        Preferences.getInstance(MainActivity.getAppContext()).clear();
        onCreate(db);
    }

    //----------------------------------------------------------------------------------------------
    //----------------------------- Methods for work with cities -----------------------------------

    public long addCity(City city) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CITY_ID, city.getId());
        values.put(KEY_CITY_NAME, city.getName());
        values.put(KEY_CITY_COUNTRY, city.getCountry());
        values.put(KEY_CITY_LATITUDE, city.getLatitude());
        values.put(KEY_CITY_LONGITUDE, city.getLongitude());
        values.put(KEY_CITY_TEMPERATURE, city.getTemperature());
        values.put(KEY_CITY_WEATHER_TYPE, city.getWeather_type());
        values.put(KEY_CITY_HUMIDITY, city.getHumidity());
        values.put(KEY_CITY_WIND_SPEED, city.getWind_speed());
        values.put(KEY_CITY_PRESSURE, city.getPressure());
        values.put(KEY_CITY_WIND_DIRECTION, city.getWind_direction());
        values.put(KEY_CITY_DATE, city.getDate());
        return db.insert(TABLE_CITY, null, values);
    }

    public void updateCity(City city) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CITY_NAME, city.getName());
        values.put(KEY_CITY_COUNTRY, city.getCountry());
        values.put(KEY_CITY_LATITUDE, city.getLatitude());
        values.put(KEY_CITY_LONGITUDE, city.getLongitude());
        values.put(KEY_CITY_TEMPERATURE, city.getTemperature());
        values.put(KEY_CITY_WEATHER_TYPE, city.getWeather_type());
        values.put(KEY_CITY_HUMIDITY, city.getHumidity());
        values.put(KEY_CITY_WIND_SPEED, city.getWind_speed());
        values.put(KEY_CITY_PRESSURE, city.getPressure());
        values.put(KEY_CITY_WIND_DIRECTION, city.getWind_direction());
        values.put(KEY_CITY_DATE, city.getDate());
        db.update(TABLE_CITY, values, "id = " + city.getId(), null);
    }

    public City getCity(long id) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_CITY, new String[]{KEY_CITY_ID, KEY_CITY_NAME, KEY_CITY_COUNTRY,
                        KEY_CITY_LATITUDE, KEY_CITY_LONGITUDE, KEY_CITY_TEMPERATURE, KEY_CITY_WEATHER_TYPE,
                        KEY_CITY_HUMIDITY, KEY_CITY_WIND_SPEED, KEY_CITY_PRESSURE, KEY_CITY_WIND_DIRECTION,
                        KEY_CITY_DATE}, KEY_CITY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null) cursor.moveToFirst();

        City city = new City(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getDouble(3),
                cursor.getDouble(4), cursor.getInt(5), cursor.getString(6), cursor.getInt(7), cursor.getDouble(8),
                cursor.getInt(9), cursor.getInt(10), cursor.getInt(11));
        cursor.close();
        return city;
    }

    public ArrayList<City> getAllCities() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " +TABLE_CITY, null);
        cursor.moveToFirst();
        ArrayList<City> cities = new ArrayList<>();
        while (cursor.moveToNext()) {
            City city = new City(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getDouble(3),
                    cursor.getDouble(4), cursor.getInt(5), cursor.getString(6), cursor.getInt(7), cursor.getDouble(8),
                    cursor.getInt(9), cursor.getInt(10), cursor.getInt(11));
            cities.add(city);
        }
        cursor.close();
        return cities;
    }

    public void deleteCity(long id) {
        SQLiteDatabase db = getReadableDatabase();
        db.delete(TABLE_CITY, KEY_CITY_ID +" = " +id, null);
        //удалять связанную погоду
    }

    //----------------------------------------------------------------------------------------------
    //----------------------------- Methods for work with forecast ---------------------------------

    public long addForecast(Forecast forecast) {
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_FORECAST_FK_CITY_ID, forecast.getFkCityId());
        values.put(KEY_FORECAST_WEATHER_TYPE, forecast.getWeatherType());
        values.put(KEY_FORECAST_TEMPERATURE, forecast.getTemperature());
        values.put(KEY_FORECAST_HUMIDITY, forecast.getHumidity());
        values.put(KEY_FORECAST_WIND_SPEED, forecast.getWind_speed());
        values.put(KEY_FORECAST_PRESSURE, forecast.getPressure());
        values.put(KEY_FORECAST_WIND_DIRECTION, forecast.getWind_direction());
        values.put(KEY_FORECAST_DATE, forecast.getDate());
        return db.insert(TABLE_FORECAST, null, values);
    }

    public void addAllForecasts(ArrayList<Forecast> forecasts) {
        for (Forecast forecast : forecasts) {
            addForecast(forecast);
        }
    }

    public void updateAllForecasts(ArrayList<Forecast> forecasts) {
        deleteAllForecasts(forecasts.get(0).getFkCityId());
        addAllForecasts(forecasts);
    }

    public ArrayList<Forecast> getForecast(int fk_city_id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_FORECAST, new String[]{KEY_FORECAST_ID, KEY_FORECAST_FK_CITY_ID,
                KEY_FORECAST_WEATHER_TYPE, KEY_FORECAST_TEMPERATURE, KEY_FORECAST_HUMIDITY, KEY_FORECAST_WIND_SPEED,
                KEY_FORECAST_PRESSURE, KEY_FORECAST_WIND_DIRECTION, KEY_FORECAST_DATE}, KEY_FORECAST_FK_CITY_ID
                +" =?", new String[]{String.valueOf(fk_city_id)}, null, null, KEY_FORECAST_DATE + " ASC", null);
        cursor.moveToFirst();
        ArrayList<Forecast> list = new ArrayList<>();
        do {
            list.add(new Forecast(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getInt(3), cursor.getInt(4),
                    cursor.getDouble(5), cursor.getInt(6), cursor.getInt(7), cursor.getInt(8)));
        } while(cursor.moveToNext());
        cursor.close();
        return list;
    }

    public void deleteAllForecasts(int fk_city_id) {
        SQLiteDatabase db = getReadableDatabase();
        db.delete(TABLE_FORECAST, KEY_FORECAST_FK_CITY_ID +" = " +fk_city_id, null);
    }
}
