package nikitin.weatherapp.com.weatherapptest3;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import nikitin.weatherapp.com.weatherapptest3.Model.Database.City;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.CurrentWeather;
import nikitin.weatherapp.com.weatherapptest3.Model.WeatherModel.Weather;

/**
 * Created by Влад on 28.11.2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    private static DatabaseHandler databaseHandler;
    private static final int DATABASE_VERSION = 14;
    private static final String DATABASE_NAME = "weatherAppDatabase";

    private static final String TABLE_CITIES = "cities";
    private static final String KEY_CITIES_ID = "id";
    private static final String KEY_CITIES_OW_ID = "ow_id";
    private static final String KEY_CITIES_NAME = "name";
    private static final String KEY_CITIES_COUNTRY = "country";
    private static final String KEY_CITIES_LATITUDE = "latitude";
    private static final String KEY_CITIES_LONGITUDE = "longitude";

    private static final String TABLE_CURRENT_WEATHER = "current_weathers";
    private static final String KEY_CURRENT_WEATHER_ID = "id";
    private static final String KEY_CURRENT_WEATHER_FK_CITY_ID = "fk_city_id";
    private static final String KEY_CURRENT_WEATHER_NAME = "name";
    private static final String KEY_CURRENT_WEATHER_TEMP = "temp";
    private static final String KEY_CURRENT_WEATHER_HUMIDITY = "humidity";
    private static final String KEY_CURRENT_WEATHER_WIND_SPEED = "wind_speed";
    private static final String KEY_CURRENT_WEATHER_PRESSURE = "pressure";
    private static final String KEY_CURRENT_WEATHER_DATE = "date";

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
        String CREATE_CITIES_TABLE = "CREATE TABLE " + TABLE_CITIES + " ("
                + KEY_CITIES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_CITIES_OW_ID + " INTEGER, "
                + KEY_CITIES_NAME + " TEXT, " + KEY_CITIES_COUNTRY + " TEXT, " + KEY_CITIES_LATITUDE +
                " REAL, " +KEY_CITIES_LONGITUDE +" REAL" +")";
        db.execSQL(CREATE_CITIES_TABLE);

        String CREATE_CURRENT_WEATHER_TABLE = "CREATE TABLE " +TABLE_CURRENT_WEATHER +" ("
                + KEY_CURRENT_WEATHER_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +KEY_CURRENT_WEATHER_FK_CITY_ID
                +" INTEGER, " +KEY_CURRENT_WEATHER_NAME +" TEXT, " +KEY_CURRENT_WEATHER_DATE +" INTEGER, "
                +KEY_CURRENT_WEATHER_HUMIDITY +" REAL, " +KEY_CURRENT_WEATHER_PRESSURE +" REAL, "
                +KEY_CURRENT_WEATHER_TEMP +" REAL, " +KEY_CURRENT_WEATHER_WIND_SPEED +" REAL" +")";
        db.execSQL(CREATE_CURRENT_WEATHER_TABLE);

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("protokol");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITIES);
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_CURRENT_WEATHER);
        System.out.println("pop");
        Preferences.getInstance(MainActivity.getAppContext()).clear();
        onCreate(db);
    }

    //----------------------------------------------------------------------------------------------
    //----------------------------- Methods for work with cities -----------------------------------

    public long addCity(City city) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CITIES_OW_ID, city.getOw_id());
        values.put(KEY_CITIES_NAME, city.getName());
        values.put(KEY_CITIES_COUNTRY, city.getCountry());
        values.put(KEY_CITIES_LATITUDE, city.getLatitude());
        values.put(KEY_CITIES_LONGITUDE, city.getLongitude());
        long id = db.insert(TABLE_CITIES, null, values);
        db.close();
        return id;
    }

    public void updateCity(City city) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CITIES_OW_ID, city.getOw_id());
        values.put(KEY_CITIES_NAME, city.getName());
        values.put(KEY_CITIES_COUNTRY, city.getCountry());
        values.put(KEY_CITIES_LATITUDE, city.getLatitude());
        values.put(KEY_CITIES_LONGITUDE, city.getLongitude());
        db.update(TABLE_CITIES, values, "id = " + city.getId(), null);
        db.close();
    }

    public City getCity(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CITIES, new String[]{KEY_CITIES_ID, KEY_CITIES_OW_ID, KEY_CITIES_NAME,
                        KEY_CITIES_COUNTRY, KEY_CITIES_LATITUDE, KEY_CITIES_LONGITUDE}, KEY_CITIES_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null){
            cursor.moveToFirst();
        }

        City city = new City(cursor.getInt(0), cursor.getInt(1), cursor.getString(2),
                cursor.getString(3), cursor.getDouble(4), cursor.getDouble(5));
        cursor.close();
        db.close();
        return city;
    }

    public ArrayList<City> getAllCities() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM cities", null);
        cursor.moveToFirst();
        ArrayList<City> cities = new ArrayList<>();
        while (cursor.moveToNext()) {
            City city = new City(cursor.getInt(0), cursor.getInt(1), cursor.getString(2),
                    cursor.getString(3), cursor.getDouble(4), cursor.getDouble(5));
            cities.add(city);
        }
        cursor.close();
        db.close();
    return cities;
    }

    public void deleteCity(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_CITIES, KEY_CITIES_ID + " = " + id, null);
        db.close();
    }

    //----------------------------------------------------------------------------------------------
    //----------------------------- Methods for work with weather ----------------------------------

    public long addWeather(CurrentWeather currentWeather) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_CURRENT_WEATHER_FK_CITY_ID, currentWeather.getFk_city_id());
        values.put(KEY_CURRENT_WEATHER_NAME, currentWeather.getName());
        values.put(KEY_CURRENT_WEATHER_TEMP, currentWeather.getTemp());
        values.put(KEY_CURRENT_WEATHER_HUMIDITY, currentWeather.getHumidity());
        values.put(KEY_CURRENT_WEATHER_WIND_SPEED, currentWeather.getWind_speed());
        values.put(KEY_CURRENT_WEATHER_PRESSURE, currentWeather.getPressure());
        values.put(KEY_CURRENT_WEATHER_DATE, currentWeather.getDate());
        long id = db.insert(TABLE_CURRENT_WEATHER, null, values);
        db.close();
        return id;
    }

    public void updateWeather(CurrentWeather currentWeather, long weatherId) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CURRENT_WEATHER_FK_CITY_ID, currentWeather.getFk_city_id());
        values.put(KEY_CURRENT_WEATHER_NAME, currentWeather.getName());
        values.put(KEY_CURRENT_WEATHER_TEMP, currentWeather.getTemp());
        values.put(KEY_CURRENT_WEATHER_HUMIDITY, currentWeather.getHumidity());
        values.put(KEY_CURRENT_WEATHER_WIND_SPEED, currentWeather.getWind_speed());
        values.put(KEY_CURRENT_WEATHER_PRESSURE, currentWeather.getPressure());
        values.put(KEY_CURRENT_WEATHER_DATE, currentWeather.getDate());
        db.update(TABLE_CURRENT_WEATHER, values, KEY_CURRENT_WEATHER_ID +"=" +weatherId, null);
    }

    public int findWeatherIdByCityId(long cityId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CURRENT_WEATHER, new String[]{KEY_CURRENT_WEATHER_ID, KEY_CURRENT_WEATHER_FK_CITY_ID,
                        KEY_CURRENT_WEATHER_NAME, KEY_CURRENT_WEATHER_TEMP, KEY_CURRENT_WEATHER_HUMIDITY,
                        KEY_CURRENT_WEATHER_WIND_SPEED, KEY_CURRENT_WEATHER_PRESSURE, KEY_CURRENT_WEATHER_DATE},
                KEY_CURRENT_WEATHER_FK_CITY_ID + "=?", new String[]{String.valueOf(cityId)}, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            return cursor.getInt(0);
        }
        db.close();
        return -1;
    }

    public CurrentWeather getWeather(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CURRENT_WEATHER, new String[] {KEY_CURRENT_WEATHER_ID,
                KEY_CURRENT_WEATHER_FK_CITY_ID, KEY_CURRENT_WEATHER_NAME, KEY_CURRENT_WEATHER_TEMP,
                KEY_CURRENT_WEATHER_HUMIDITY, KEY_CURRENT_WEATHER_WIND_SPEED, KEY_CURRENT_WEATHER_PRESSURE,
                KEY_CURRENT_WEATHER_DATE}, KEY_CITIES_ID +"=?", new String[] {String.valueOf(id)}, null,
                null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
        }

        CurrentWeather currentWeather = new CurrentWeather(cursor.getInt(0), cursor.getInt(1), cursor.getString(2),
                cursor.getDouble(3), cursor.getDouble(4), cursor.getDouble(5), cursor.getDouble(6), cursor.getInt(7));
        cursor.close();
        db.close();
        return currentWeather;
    }

    public ArrayList<CurrentWeather> getAllWeathers() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " +TABLE_CURRENT_WEATHER, null);
        cursor.moveToFirst();
        ArrayList<CurrentWeather> weathers = new ArrayList<>();
        do {
            CurrentWeather currentWeather = new CurrentWeather(cursor.getInt(0), cursor.getInt(1), cursor.getString(2),
                    cursor.getDouble(3), cursor.getDouble(4), cursor.getDouble(5), cursor.getDouble(6), cursor.getInt(7));
            weathers.add(currentWeather);
        } while (cursor.moveToNext());
        cursor.close();
        db.close();
        return weathers;
    }


}
