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
import java.util.List;

import nikitin.weatherapp.com.weatherapptest3.Model.Database.City;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.Forecast;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.GeoStorm;
import nikitin.weatherapp.com.weatherapptest3.Model.WeatherModel.Weather;

/**
 * Created by Влад on 28.11.2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    private static DatabaseHandler databaseHandler;
    private static final int DATABASE_VERSION = 29;
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
    private static final String KEY_CITY_GROUP_CODE = "group_code";
    private static final String KEY_CITY_WEATHER_DETAILED_TYPE = "weather_detailed_type";

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
    private static final String KEY_FORECAST_GROUP_CODE = "group_code";
    private static final String KEY_FORECAST_WEATHER_DETAILED_TYPE = "weather_detailed_type";
    private static final String KEY_FORECAST_K_INDEX = "k_index";

    private static final String TABLE_GEOSTORM = "geostorm";
    private static final String KEY_GEOSTORM_DATE = "date";
    private static final String KEY_GEOSTORM_KINDEX = "k_index";

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
                +KEY_CITY_WIND_DIRECTION +" INTEGER, "
                +KEY_CITY_DATE +" INTEGER, "
                +KEY_CITY_GROUP_CODE +" INTEGER, "
                +KEY_CITY_WEATHER_DETAILED_TYPE +" TEXT"+")";
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
                +KEY_FORECAST_DATE +" INTEGER, "
                +KEY_FORECAST_GROUP_CODE +" INTEGER, "
                +KEY_FORECAST_WEATHER_DETAILED_TYPE +" TEXT, "
                +KEY_FORECAST_K_INDEX +" INTEGER"+")";
        db.execSQL(CREATE_FORECAST_TABLE);

        String CREATE_GEOSTORM_TABLE = "CREATE TABLE " +TABLE_GEOSTORM +" ("
                +KEY_GEOSTORM_DATE +" INTEGER PRIMARY KEY, "
                +KEY_GEOSTORM_KINDEX +" INTEGER"+")";
        db.execSQL(CREATE_GEOSTORM_TABLE);


    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_CITY);
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_FORECAST);
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_GEOSTORM);
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
        values.put(KEY_CITY_GROUP_CODE, city.getGroup_code());
        values.put(KEY_CITY_WEATHER_DETAILED_TYPE, city.getWeather_detailed_type());
        return db.insert(TABLE_CITY, null, values);
    }

    public void updateCity(City city) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CITY_TEMPERATURE, city.getTemperature());
        values.put(KEY_CITY_WEATHER_TYPE, city.getWeather_type());
        values.put(KEY_CITY_HUMIDITY, city.getHumidity());
        values.put(KEY_CITY_WIND_SPEED, city.getWind_speed());
        values.put(KEY_CITY_PRESSURE, city.getPressure());
        values.put(KEY_CITY_WIND_DIRECTION, city.getWind_direction());
        values.put(KEY_CITY_DATE, city.getDate());
        values.put(KEY_CITY_GROUP_CODE, city.getGroup_code());
        values.put(KEY_CITY_WEATHER_DETAILED_TYPE, city.getWeather_detailed_type());
        db.update(TABLE_CITY, values, "id = " + city.getId(), null);
    }

    public City getCity(long id) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_CITY, new String[]{KEY_CITY_ID, KEY_CITY_NAME, KEY_CITY_COUNTRY,
                        KEY_CITY_LATITUDE, KEY_CITY_LONGITUDE, KEY_CITY_TEMPERATURE, KEY_CITY_WEATHER_TYPE,
                        KEY_CITY_HUMIDITY, KEY_CITY_WIND_SPEED, KEY_CITY_PRESSURE, KEY_CITY_WIND_DIRECTION,
                        KEY_CITY_DATE, KEY_CITY_GROUP_CODE, KEY_CITY_WEATHER_DETAILED_TYPE}, KEY_CITY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null) cursor.moveToFirst();

        City city = new City(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getDouble(3),
                cursor.getDouble(4), cursor.getInt(5), cursor.getString(6), cursor.getInt(7), cursor.getDouble(8),
                cursor.getInt(9), cursor.getInt(10), cursor.getInt(11), cursor.getInt(12), cursor.getString(13));
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
                    cursor.getInt(9), cursor.getInt(10), cursor.getInt(11), cursor.getInt(12), cursor.getString(13));
            cities.add(city);
        }
        cursor.close();
        return cities;
    }

    public void deleteCity(long id) {
        SQLiteDatabase db = getReadableDatabase();
        db.delete(TABLE_CITY, KEY_CITY_ID +" = " +id, null);
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
        values.put(KEY_FORECAST_GROUP_CODE, forecast.getGroup_code());
        values.put(KEY_FORECAST_WEATHER_DETAILED_TYPE, forecast.getWeatherDetailedType());
        return db.insert(TABLE_FORECAST, null, values);
    }

    public void addAllForecasts(ArrayList<Forecast> forecasts) {
        System.out.println("Forecast size = " +forecasts.size());
        for (Forecast forecast : forecasts) {
            System.out.println("pish  " +forecast);
            addForecast(forecast);
        }
    }

    public void updateAllForecasts(ArrayList<Forecast> forecasts) {
        deleteAllForecasts(forecasts.get(0).getFkCityId());
        addAllForecasts(forecasts);
    }

    public ArrayList<Forecast> getForecast(long fk_city_id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_FORECAST, new String[]{KEY_FORECAST_ID, KEY_FORECAST_FK_CITY_ID,
                KEY_FORECAST_WEATHER_TYPE, KEY_FORECAST_TEMPERATURE, KEY_FORECAST_HUMIDITY, KEY_FORECAST_WIND_SPEED,
                KEY_FORECAST_PRESSURE, KEY_FORECAST_WIND_DIRECTION, KEY_FORECAST_DATE, KEY_FORECAST_GROUP_CODE, KEY_FORECAST_WEATHER_DETAILED_TYPE}, KEY_FORECAST_FK_CITY_ID
                +" =?", new String[]{String.valueOf(fk_city_id)}, null, null, KEY_FORECAST_DATE + " ASC", null);
        cursor.moveToFirst();
        ArrayList<Forecast> list = new ArrayList<>();
        do {
            list.add(new Forecast(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getInt(3), cursor.getInt(4),
                    cursor.getDouble(5), cursor.getInt(6), cursor.getInt(7), cursor.getInt(8), cursor.getInt(9), cursor.getString(10)));
        } while(cursor.moveToNext());
        cursor.close();
        return list;
    }

    public void deleteAllForecasts(long fk_city_id) {
        SQLiteDatabase db = getReadableDatabase();
        db.delete(TABLE_FORECAST, KEY_FORECAST_FK_CITY_ID +" = " +fk_city_id, null);
    }

    //----------------------------------------------------------------------------------------------
    //----------------------------- Methods for work with geomagnetic Activity ---------------------

    public void addGeoStorm(GeoStorm geoStorm) {
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_GEOSTORM_DATE, geoStorm.getDate());
        values.put(KEY_GEOSTORM_KINDEX, geoStorm.getkIndex());
        db.insert(TABLE_GEOSTORM, null, values);
    }

    public GeoStorm getGeoStorm(int geoStormDate) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_GEOSTORM, new String[]{KEY_GEOSTORM_DATE, KEY_GEOSTORM_KINDEX}, KEY_GEOSTORM_DATE +"=?", new String[]{String.valueOf(geoStormDate)}, null, null, null, null);
        return new GeoStorm(cursor.getInt(0), cursor.getInt(1));
    }

    public void deleteGeoStorm(int date) {
        SQLiteDatabase db = getReadableDatabase();
        db.delete(TABLE_GEOSTORM, KEY_GEOSTORM_DATE +" = "+date, null);
    }

    public void deleteAllOldGeoStorms(long date) {
        SQLiteDatabase db = getReadableDatabase();
        db.delete(TABLE_GEOSTORM, KEY_GEOSTORM_DATE +" < " +date, null);
    }

    public void addOrIgnoreGeoStorm(ArrayList<GeoStorm> geoStorms) {
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();

        for (GeoStorm geoStorm : geoStorms) {
            System.out.println(geoStorm.getDate() +"        " +geoStorm.getkIndex());
            values.put(KEY_GEOSTORM_DATE, geoStorm.getDate());
            values.put(KEY_GEOSTORM_KINDEX, geoStorm.getkIndex());
            db.insertWithOnConflict(TABLE_GEOSTORM, null, values, SQLiteDatabase.CONFLICT_IGNORE);
            values.clear();
        }
    }

    public List<GeoStorm> getAllGeoStorms() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " +TABLE_GEOSTORM, null);
        cursor.moveToFirst();
        ArrayList <GeoStorm> geoStorms = new ArrayList<>();
        while (cursor.moveToNext()) {
            GeoStorm geoStorm = new GeoStorm(cursor.getInt(0), cursor.getInt(1));
            geoStorms.add(geoStorm);
        }
        cursor.close();
        return geoStorms;
    }
    public void updateGeoStorms(ArrayList<GeoStorm> geoStorms) {
        addOrIgnoreGeoStorm(geoStorms);
        deleteAllOldGeoStorms(geoStorms.get(0).getDate());
    }
}
