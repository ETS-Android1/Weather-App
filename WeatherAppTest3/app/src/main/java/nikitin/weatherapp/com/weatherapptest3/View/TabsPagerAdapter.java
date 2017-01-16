package nikitin.weatherapp.com.weatherapptest3.View;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;



/**
 * Created by Влад on 23.07.2016.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

    public static CitiesFragment citiesFragment;
    public static MainWindowFragment mainWindowFragment;

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
        citiesFragment = CitiesFragment.getInstance();
        mainWindowFragment = MainWindowFragment.getInstance();
    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {
            case 0: return citiesFragment;
            case 1: return mainWindowFragment;
            case 2: return DayForecastFragment.newInstance();
            case 3: return WeeklyForecastFragment.getInstance();
        }
        return null;
    }
    @Override
    public int getCount() {
        return 4;
    }
}