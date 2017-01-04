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
import nikitin.weatherapp.com.weatherapptest3.Model.WeatherModel.Weather;

/**
 * Created by Влад on 28.11.2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    private static DatabaseHandler databaseHandler;
    private static final int DATABASE_VERSION = 25;
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

    private static final String TABLE_DAILY_FORECAST = "daily_forecast";
    private static final String KEY_DAILY_FORECAST_ID = "id";
    private static final String KEY_DAILY_FORECAST_FK_CITY_OW_ID = "city_ow_id";
    private static final String KEY_DAILY_FORECAST_DATE = "date";
    private static final String KEY_DAILY_FORECAST_WEATHER_NAME = "name";
    private static final String KEY_DAILY_FORECAST_TEMPERATURE = "temperature";
    private static final String KEY_DAILY_FORECAST_HUMIDITY = "humidity";
    private static final String KEY_DAILY_FORECAST_WIND_SPEED = "wind_speed";
    private static final String KEY_DAILY_FORECAST_PRESSURE = "pressure";
    private static final String KEY_DAILY_FORECAST_WIND_DIRECTION = "wind_direction";


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

        String CREATE_DAILY_FORECAST_TABLE = "CREATE TABLE " +TABLE_DAILY_FORECAST + " ("
                +KEY_DAILY_FORECAST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_DAILY_FORECAST_FK_CITY_OW_ID
                +" INTEGER, " +KEY_DAILY_FORECAST_DATE +" INTEGER, " +KEY_DAILY_FORECAST_WEATHER_NAME +" TEXT, "
                +KEY_DAILY_FORECAST_TEMPERATURE +" INTEGER, " +KEY_DAILY_FORECAST_HUMIDITY +" INTEGER, "
                +KEY_DAILY_FORECAST_WIND_SPEED +" INTEGER, " +KEY_DAILY_FORECAST_PRESSURE +" INTEGER, " +KEY_DAILY_FORECAST_WIND_DIRECTION +" INTEGER)";
        db.execSQL(CREATE_DAILY_FORECAST_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_CITIES);
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_CURRENT_WEATHER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAILY_FORECAST);
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
    return cities;
    }

    public void deleteCity(int id) {
        System.out.println("Delete id " + id);
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_CITIES, KEY_CITIES_ID + " = " + id, null);
        db.close();
        System.out.println("Delete id " + id);
        deleteWeatherByCityId(id);
    }

    public int getCityIdByOw_id(int ow_id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_CITIES, new String[]{KEY_CITIES_ID}, KEY_CITIES_OW_ID + "=?", new String[]{String.valueOf(ow_id)}, null, null, null, null);
        cursor.moveToFirst();
        int cityId = cursor.getInt(0);
        cursor.close();
        return cityId;
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
        db.update(TABLE_CURRENT_WEATHER, values, KEY_CURRENT_WEATHER_ID + "=" + weatherId, null);
    }

    public int findWeatherIdByCityId(long cityId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CURRENT_WEATHER, new String[]{KEY_CURRENT_WEATHER_ID},
                KEY_CURRENT_WEATHER_FK_CITY_ID + "=?", new String[]{String.valueOf(cityId)}, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            return cursor.getInt(0);
        }
        return -1;
    }

    public CurrentWeather getWeatherByCityId(int cityId) {
        System.out.println("cityId = " +cityId);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_CURRENT_WEATHER, new String[]{KEY_CURRENT_WEATHER_ID,
                        KEY_CURRENT_WEATHER_FK_CITY_ID, KEY_CURRENT_WEATHER_NAME, KEY_CURRENT_WEATHER_TEMP,
                        KEY_CURRENT_WEATHER_HUMIDITY, KEY_CURRENT_WEATHER_WIND_SPEED, KEY_CURRENT_WEATHER_PRESSURE,
                        KEY_CURRENT_WEATHER_DATE}, KEY_CURRENT_WEATHER_FK_CITY_ID + "=?", new String[]{String.valueOf(cityId)}, null,
                null, null, null);
        cursor.moveToFirst();
        System.out.println("cursor size = " + cursor.getCount());
        CurrentWeather weather = new CurrentWeather(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getDouble(3),
                cursor.getDouble(4), cursor.getDouble(5), cursor.getDouble(6), cursor.getInt(7));
        cursor.close();
        return weather;
    }

    public void deleteWeatherByCityId(int cityId) {
        System.out.println("city id " +cityId);
        System.out.println("weather " +findWeatherIdByCityId(cityId));
        SQLiteDatabase db = this.getReadableDatabase();
        int deleteRes = db.delete(TABLE_CURRENT_WEATHER, KEY_CURRENT_WEATHER_FK_CITY_ID + " = " + cityId, null);
        System.out.println("delete res " +deleteRes);
    }

//    public CurrentWeather getWeather(int id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.query(TABLE_CURRENT_WEATHER, new String[] {KEY_CURRENT_WEATHER_ID,
//                KEY_CURRENT_WEATHER_FK_CITY_ID, KEY_CURRENT_WEATHER_NAME, KEY_CURRENT_WEATHER_TEMP,
//                KEY_CURRENT_WEATHER_HUMIDITY, KEY_CURRENT_WEATHER_WIND_SPEED, KEY_CURRENT_WEATHER_PRESSURE,
//                KEY_CURRENT_WEATHER_DATE}, KEY_CITIES_ID +"=?", new String[] {String.valueOf(id)}, null,
//                null, null, null);
//        if (cursor != null){
//            cursor.moveToFirst();
//        }
//
//        CurrentWeather currentWeather = new CurrentWeather(cursor.getInt(0), cursor.getInt(1), cursor.getString(2),
//                cursor.getDouble(3), cursor.getDouble(4), cursor.getDouble(5), cursor.getDouble(6), cursor.getInt(7));
//        cursor.close();
//        db.close();
//        return currentWeather;
//    }

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
        return weathers;
    }

    //----------------------------------------------------------------------------------------------
    //----------------------------- Methods for work with daily forecast ---------------------------

    public void addDailyForecast(DailyForecast forecast) {
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DAILY_FORECAST_FK_CITY_OW_ID, forecast.getFk_city_ow_id());
        values.put(KEY_DAILY_FORECAST_DATE, forecast.getDate());
        values.put(KEY_DAILY_FORECAST_WEATHER_NAME, forecast.getWeather_name());
        values.put(KEY_DAILY_FORECAST_TEMPERATURE, forecast.getTemperature());
        values.put(KEY_DAILY_FORECAST_HUMIDITY, forecast.getHumidity());
        values.put(KEY_DAILY_FORECAST_WIND_SPEED, forecast.getWind_speed());
        values.put(KEY_DAILY_FORECAST_PRESSURE, forecast.getPressure());
        values.put(KEY_DAILY_FORECAST_WIND_DIRECTION, forecast.getWind_direction());
        db.insert(TABLE_DAILY_FORECAST, null, values);
    }

    public void addDailyForecastList(ArrayList<DailyForecast> forecastList) {
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();
        for (DailyForecast forecast : forecastList) {
            values.put(KEY_DAILY_FORECAST_FK_CITY_OW_ID, forecast.getFk_city_ow_id());
            values.put(KEY_DAILY_FORECAST_DATE, forecast.getDate());
            values.put(KEY_DAILY_FORECAST_WEATHER_NAME, forecast.getWeather_name());
            values.put(KEY_DAILY_FORECAST_TEMPERATURE, forecast.getTemperature());
            values.put(KEY_DAILY_FORECAST_HUMIDITY, forecast.getHumidity());
            values.put(KEY_DAILY_FORECAST_WIND_SPEED, forecast.getWind_speed());
            values.put(KEY_DAILY_FORECAST_PRESSURE, forecast.getPressure());
            values.put(KEY_DAILY_FORECAST_WIND_DIRECTION, forecast.getWind_direction());
            System.out.println("this");
            System.out.println(values.getAsString(KEY_DAILY_FORECAST_FK_CITY_OW_ID + " " + values.getAsString(KEY_DAILY_FORECAST_DATE)));
            db.insert(TABLE_DAILY_FORECAST, null, values);
            values.clear();
        }
    }

    public ArrayList<DailyForecast> getDailyForecast(int fk_city_id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_DAILY_FORECAST, new String[]{KEY_DAILY_FORECAST_ID,
                        KEY_DAILY_FORECAST_FK_CITY_OW_ID, KEY_DAILY_FORECAST_DATE, KEY_DAILY_FORECAST_WEATHER_NAME,
                        KEY_DAILY_FORECAST_TEMPERATURE, KEY_DAILY_FORECAST_HUMIDITY, KEY_DAILY_FORECAST_WIND_SPEED,
                        KEY_DAILY_FORECAST_PRESSURE, KEY_DAILY_FORECAST_WIND_DIRECTION}, KEY_DAILY_FORECAST_FK_CITY_OW_ID + "=?", new String[]{String.valueOf(fk_city_id)}, null,
                null, KEY_DAILY_FORECAST_DATE + " ASC", null);
        cursor.moveToFirst();
        ArrayList<DailyForecast> list = new ArrayList<>();
        do {
//            System.out.println(" " +cursor.getInt(0) +" " +cursor.getInt(1) +" " +cursor.getInt(2) +" " +cursor.getString(3)
//                    +" " +cursor.getInt(4) +" " +cursor.getInt(5) +" " +cursor.getInt(6) +" " +cursor.getInt(7) +" ");
            list.add( new DailyForecast(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getString(3),
                    cursor.getInt(4), cursor.getInt(5), cursor.getInt(6), cursor.getInt(7), cursor.getInt(8)));
        } while(cursor.moveToNext());
        cursor.close();
        return list;
    }

    public ArrayList<DailyForecast> getDailyForecastAll() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_DAILY_FORECAST, null);
        cursor.moveToFirst();
        ArrayList<DailyForecast> list = new ArrayList<>();
        do {
            list.add( new DailyForecast(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getString(3),
                    cursor.getInt(4), cursor.getInt(5), cursor.getInt(6), cursor.getInt(7), cursor.getInt(8)));
        } while (cursor.moveToNext());
        cursor.close();
        return list;
    }

    public void updateDailyForecast(int fk_city_ow_id, ArrayList<DailyForecast> forecastList) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_DAILY_FORECAST, new String[]{KEY_DAILY_FORECAST_ID,
                        KEY_DAILY_FORECAST_FK_CITY_OW_ID, KEY_DAILY_FORECAST_DATE, KEY_DAILY_FORECAST_WEATHER_NAME,
                        KEY_DAILY_FORECAST_TEMPERATURE, KEY_DAILY_FORECAST_HUMIDITY, KEY_DAILY_FORECAST_WIND_SPEED,
                        KEY_DAILY_FORECAST_PRESSURE, KEY_DAILY_FORECAST_WIND_DIRECTION}, KEY_DAILY_FORECAST_FK_CITY_OW_ID + "=?", new String[]{String.valueOf(fk_city_ow_id)}, null,
                null, KEY_DAILY_FORECAST_DATE + " ASC", null);
        cursor.moveToFirst();
        ContentValues values = new ContentValues();
        int i = 0;
        do {
            System.out.println("oi");
            values.put(KEY_DAILY_FORECAST_DATE, forecastList.get(i).getDate());
            values.put(KEY_DAILY_FORECAST_WEATHER_NAME, forecastList.get(i).getWeather_name());
            values.put(KEY_DAILY_FORECAST_TEMPERATURE, forecastList.get(i).getTemperature());
            values.put(KEY_DAILY_FORECAST_HUMIDITY, forecastList.get(i).getHumidity());
            values.put(KEY_DAILY_FORECAST_WIND_SPEED, forecastList.get(i).getWind_speed());
            values.put(KEY_DAILY_FORECAST_PRESSURE, forecastList.get(i).getPressure());
            values.put(KEY_DAILY_FORECAST_WIND_DIRECTION, forecastList.get(i).getWind_direction());
            db.update(TABLE_DAILY_FORECAST, values, "id = " + cursor.getInt(0), null);
            i++;
            values.clear();
        } while (cursor.moveToNext());
        cursor.close();
    }

    public boolean isDailyForecastExists(int fk_city_id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_DAILY_FORECAST, new String[]{KEY_DAILY_FORECAST_ID},
                KEY_DAILY_FORECAST_FK_CITY_OW_ID + "=?", new String[]{String.valueOf(fk_city_id)}, null,
                null, KEY_DAILY_FORECAST_DATE + " ASC", null);
        int count = cursor.getCount();
        System.out.println(count);
        cursor.close();
        return count > 0;
    }
}
