package nikitin.weatherapp.com.weatherapptest3.View;

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

    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {
            case 0:
                return citiesFragment;

            case 1: {
                if (citiesFragment.getPresenter() == null) {
                    System.out.println("null");
                    mainWindowFragment = MainWindowFragment.getInstance(0);
                    return mainWindowFragment;
                } else {
                    System.out.println("not null");
                    mainWindowFragment = MainWindowFragment.getInstance(citiesFragment.getActiveCityId());
                    return mainWindowFragment;
                }
            }
            case 2: {
                return new DayForecastFragment();
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}