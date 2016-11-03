package nikitin.weatherapp.com.weatherapptest3;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import nikitin.weatherapp.com.weatherapptest3.Model.ForecastModel.City;
import nikitin.weatherapp.com.weatherapptest3.Model.WeatherModel.*;
import nikitin.weatherapp.com.weatherapptest3.rest.OpenWeatherMapAPI;
import retrofit2.Response;

/**
 * Created by Влад on 17.07.2016.
 */
public class CitySuggestionProvider extends ContentProvider{

    ArrayList <City> citiesList;
    final String TAG = "CitySuggestionProvider";
    public static MatrixCursor cursor;

    public static final String SUGGEST_COLUMN_CITY_ID = "city_id";
    private List<WeatherResponse> findCityList;

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        //Получаю список городов которые подходят под пользовательский поиск
        Response<FindCityResponse> callback = OpenWeatherMapAPI.getInstance().findCity(uri.getLastPathSegment());
        findCityList = callback.body().getWeatherList();

        cursor = new MatrixCursor(new String[]{
                //Обязательное поле. Влияет на порядок при отображении.
                BaseColumns._ID,
                //Обязательное поле. Отвечает за текст подсказки
                SearchManager.SUGGEST_COLUMN_TEXT_1,
                SUGGEST_COLUMN_CITY_ID,
        });
        for (int i = 0; i < findCityList.size(); i++) {
            WeatherResponse weatherResponse = findCityList.get(i);
            String row = weatherResponse.getName() + ", " +weatherResponse.getSys().getCountry();
            cursor.addRow(new Object[]{i,  row,  weatherResponse.getId()});
        }
        cursor.close();
        return cursor;
    }
    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}