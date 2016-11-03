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
import nikitin.weatherapp.com.weatherapptest3.Presenters.MainPresenter;
import nikitin.weatherapp.com.weatherapptest3.View.CitiesFragment;
import nikitin.weatherapp.com.weatherapptest3.View.TabsPagerAdapter;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    SearchView searchView;
    private ImageView imageView;
    MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mActionBarToolbar);

        imageView = (ImageView)findViewById(R.id.mainActivityImage);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        presenter = new MainPresenter(this);
        presenter.createBackground();

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        TabsPagerAdapter tabsPagerAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabsPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    setTitle("Choose city");
                    ImageView firstTab = (ImageView)findViewById(R.id.first_tab);
                    firstTab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.circle2_active));
                    ImageView secondTab = (ImageView)findViewById(R.id.second_tab);
                    secondTab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.circle2_deactive));
                } else if (position == 1){
                    setTitle(CitiesFragment.activeCityName);
                    ImageView firstTab = (ImageView)findViewById(R.id.first_tab);
                    firstTab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.circle2_deactive));
                    ImageView secondTab = (ImageView)findViewById(R.id.second_tab);
                    secondTab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.circle2_active));
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Cursor cursor = CitySuggestionProvider.cursor;
            cursor.move(0);
            int cityId = cursor.getInt(cursor.getColumnIndex(CitySuggestionProvider.SUGGEST_COLUMN_CITY_ID));
            TabsPagerAdapter.citiesFragment.addNewCity(cityId);
        }
    }

    public void setImageViewBackground(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

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
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
