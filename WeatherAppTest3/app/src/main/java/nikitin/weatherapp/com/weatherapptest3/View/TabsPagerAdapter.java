package nikitin.weatherapp.com.weatherapptest3.View;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;


/**
 * Created by Влад on 23.07.2016.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

    public static CitiesFragment citiesFragment;
    public static MainWindowFragment mainWindowFragment;

    public int currentCityId;
    public String currentCityName;

    private Activity activity;

    public TabsPagerAdapter(FragmentManager fm, Activity activity) {
        super(fm);
        this.activity = activity;
        citiesFragment = CitiesFragment.getInstance();
        mainWindowFragment = MainWindowFragment.getInstance();
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0: {
                return citiesFragment;
            }

            case 1: {
                return mainWindowFragment;
            }
            case 2: {
                return DayForecastFragment.newInstance();
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}