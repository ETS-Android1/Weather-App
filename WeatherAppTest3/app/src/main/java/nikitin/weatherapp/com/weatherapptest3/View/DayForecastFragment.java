package nikitin.weatherapp.com.weatherapptest3.View;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import nikitin.weatherapp.com.weatherapptest3.Presenters.DayForecastPresenter;
import nikitin.weatherapp.com.weatherapptest3.R;

/**
 * Created by Влад on 22.10.2016.
 */
public class DayForecastFragment extends Fragment {
    DayForecastPresenter presenter;
    public static final String TITLE = "Daily Forecast";
    private ListView dailyForecastView;
    public DayForecastFragment() {

    }

    public static DayForecastFragment newInstance() {
        return new DayForecastFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_day_forcast, container, false);
        dailyForecastView = (ListView) rootView.findViewById(R.id.daily_weather);
        presenter = new DayForecastPresenter(getActivity(), this);
        presenter.updateForecastList();

//        DateFormat format = new SimpleDateFormat("yyyy-mm-d H:m:s", Locale.ENGLISH);
//        Date date = new Date(343434343);
//        try {
//            date = format.parse("2016-10-23 21:00:00");
//        } catch(ParseException ex) {
//            System.out.println("can't parse");
//        }

        setHasOptionsMenu(true);
        return rootView;
    }

    public ListView getDailyForecastView() {
        return dailyForecastView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.setGroupVisible(R.id.main_menu_group, false);
    }
}
