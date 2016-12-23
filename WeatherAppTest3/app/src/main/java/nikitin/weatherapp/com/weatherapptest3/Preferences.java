package nikitin.weatherapp.com.weatherapptest3;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

/**
 * Created by Влад on 04.08.2016.
 */
public class Preferences {
    String TAG = "PreferencesAPI";
    final String GPS_CITY_ID = "gps_city_id";

    static Preferences preferences;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public int citiesAmount = 0;
    private Gson gson;

    private Preferences(Activity activity) {
        sharedPreferences = activity.getPreferences(activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();
    }

    public static Preferences getInstance(Context context) {
        if (preferences == null) {
            preferences = new Preferences((Activity)context);
        }
        return preferences;
    }

    public void putGPSCityId(long id) {
        editor.putLong(GPS_CITY_ID, id);
        editor.apply();
    }

    public long getGPSCityID() {
        return sharedPreferences.getLong(GPS_CITY_ID, -1);
    }

    public void clear() {
        editor.clear();
        editor.apply();
    }
}
