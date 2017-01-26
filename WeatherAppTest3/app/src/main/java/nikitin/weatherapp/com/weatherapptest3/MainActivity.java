package nikitin.weatherapp.com.weatherapptest3;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.jar.Attributes;

import nikitin.weatherapp.com.weatherapptest3.Adapter.CitiesAdapter;
import nikitin.weatherapp.com.weatherapptest3.Model.DailyForecastItem;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.City;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.DailyForecast;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.Forecast;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.GeoStorm;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.WeeklyForecast;
import nikitin.weatherapp.com.weatherapptest3.Presenters.MainPresenter;
import nikitin.weatherapp.com.weatherapptest3.View.CitiesFragment;
import nikitin.weatherapp.com.weatherapptest3.View.DayForecastFragment;
import nikitin.weatherapp.com.weatherapptest3.View.MainWindowFragment;
import nikitin.weatherapp.com.weatherapptest3.View.TabsPagerAdapter;
import nikitin.weatherapp.com.weatherapptest3.View.WeeklyForecastFragment;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, DataSharer {

    SearchView searchView;
    private ImageView imageView;
    MainPresenter presenter;
    TabsPagerAdapter tabsPagerAdapter;
    private int currentCityId = 629634;
    private String currentCityName;
    private static Context appContext;
    private ArrayList<Forecast> forecasts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appContext = getApplicationContext();

        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mActionBarToolbar);

        imageView = (ImageView)findViewById(R.id.mainActivityImage);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        presenter = new MainPresenter(this);
        presenter.createBackground();

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        tabsPagerAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabsPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                boolean firstActive = (position == 0);
                boolean secondActive = (position == 1);
                boolean thirdActive = (position == 2);
                boolean fourthActive = (position == 3);

                ImageView firstTab = (ImageView)findViewById(R.id.first_tab);
                firstTab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), getNavigationCircleDrawable(firstActive)));
                ImageView secondTab = (ImageView)findViewById(R.id.second_tab);
                secondTab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), getNavigationCircleDrawable(secondActive)));
                ImageView thirdTab = (ImageView)findViewById(R.id.third_tab);
                thirdTab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), getNavigationCircleDrawable(thirdActive)));
                ImageView fourthTab = (ImageView)findViewById(R.id.fourth_tab);
                fourthTab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), getNavigationCircleDrawable(fourthActive)));

                switch(position) {
                    case 0: setTitle(CitiesFragment.TITLE);
                    case 1: setTitle(MainWindowFragment.TITLE +": " +CitiesFragment.getInstance().getActiveCityName());
                    case 2: setTitle(DayForecastFragment.TITLE +": " +CitiesFragment.getInstance().getActiveCityName());
                    case 3: setTitle(WeeklyForecastFragment.TITLE +": " +CitiesFragment.getInstance().getActiveCityName());
                }
                if (position == 2) {
                    if (((DayForecastFragment)tabsPagerAdapter.getItem(2)).getData() == null) {

                        ArrayList<Forecast> pop = new ArrayList<>();
                        ArrayList<GeoStorm> pop2 = new ArrayList<GeoStorm>();
                        int [] kIndex = new int [9];
                        for (int i = 0; i < 9; i++) {

                            pop.add(new Forecast());
                            pop2.add(new GeoStorm(12, 0));
                        }
                        ((DayForecastFragment)tabsPagerAdapter.getItem(2)).setForecasts(pop);
                        ((DayForecastFragment)tabsPagerAdapter.getItem(2)).setGeoStormForecast(pop2);
                        ((DayForecastFragment)tabsPagerAdapter.getItem(2)).calculateViewSize();

                    } else {
                        ((DayForecastFragment)tabsPagerAdapter.getItem(2)).calculateViewSize();
                    }



                    System.out.println("CHANGE TO THIS PAGE");
                    ((DayForecastFragment)tabsPagerAdapter.getItem(2)).calculateViewSize();
                }
            }
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public static Context getAppContext() {
        return appContext;
    }


    private int getNavigationCircleDrawable(boolean status) {
        if (status) return R.drawable.circle2_active;
        else return R.drawable.circle2_deactive;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Cursor cursor = CitySuggestionProvider.cursor;
            cursor.move(0);
            int cityId = cursor.getInt(cursor.getColumnIndex(CitySuggestionProvider.SUGGEST_COLUMN_CITY_ID));
            ((CitiesFragment)tabsPagerAdapter.getItem(0)).addCityData(cityId);
        }
    }

    public void setImageViewBackground(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cities, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        ((EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setTextColor(Color.WHITE);
        ((EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setHintTextColor(Color.WHITE);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(
                new ComponentName(this, MainActivity.class)));
        searchView.setIconifiedByDefault(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch(menuItem.getItemId()) {
            case R.id.refresh: {
                Toast toast = Toast.makeText(getAppContext(), "Selected", Toast.LENGTH_LONG);
                toast.show();
                return true;
            }
            default:
                return false;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public void setCurrentCityId(int currentCityId) {
        this.currentCityId = currentCityId;
    }
    public int getCurrentCityId (){
        return currentCityId;
    }

    public void setCurrentCityName(String currentCityName) {
        this.currentCityName = currentCityName;
    }
    public String getCurrentCityName() {
        return this.currentCityName;
    }

    @Override
    public void shareForecast(ArrayList<Forecast> forecasts) {
        ((DayForecastFragment)tabsPagerAdapter.getItem(2)).setForecasts(forecasts);
        ((WeeklyForecastFragment)tabsPagerAdapter.getItem(3)).setForecasts(forecasts);
    }
    @Override
    public void shareCity(City city) {
        ((MainWindowFragment)tabsPagerAdapter.getItem(1)).setView(city);
    }
    @Override
    public void shareGeoStormForecast(ArrayList<GeoStorm> forecast) {
        ((DayForecastFragment)tabsPagerAdapter.getItem(2)).setGeoStormForecast(forecast);
    }
}
