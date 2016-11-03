package nikitin.weatherapp.com.weatherapptest3.Presenters;

import android.app.Activity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import nikitin.weatherapp.com.weatherapptest3.Model.DailyForecastSimpleElement;
import nikitin.weatherapp.com.weatherapptest3.Adapter.DailyWeatherAdapter;
import nikitin.weatherapp.com.weatherapptest3.View.DayForecastFragment;
import nikitin.weatherapp.com.weatherapptest3.Model.ForecastModel.ForecastResponse;
import nikitin.weatherapp.com.weatherapptest3.Model.ForecastModel.ForecastWeather;
import nikitin.weatherapp.com.weatherapptest3.rest.OpenWeatherMapAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Влад on 26.10.2016.
 */
public class DayForecastPresenter {
    private OpenWeatherMapAPI api;
    private DayForecastFragment view;
    private DailyWeatherAdapter adapter;
    private Activity activity;

    public DayForecastPresenter(Activity activity, DayForecastFragment view) {
        this.activity = activity;
        this.view = view;
        api = OpenWeatherMapAPI.getInstance();
        adapter = new DailyWeatherAdapter(activity.getApplicationContext(), new ArrayList<DailyForecastSimpleElement>());
        view.getDailyForecastView().setAdapter(adapter);
    }

    public void updateForecastList() {
        api.getForecastByCityId(629634, new Callback<ForecastResponse>() {
            @Override
            public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {
                System.out.println("Look at this");
                System.out.println(response.body().getList().get(0).getWeathers().get(0).getMain());
                System.out.println(response.body().getList().get(1).getWeathers().get(0).getMain());
                System.out.println(response.body().getList().get(1).getWeathers().get(0).getMain());

                List<DailyForecastSimpleElement> list = new ArrayList<DailyForecastSimpleElement>();
                List<ForecastWeather> forecastWeatherList = response.body().getList();
                System.out.println("size" + response.body().getList().size());
                for (int i = 0; i < forecastWeatherList.size(); i++) {
                    Date date = new Date(response.body().getList().get(i).getDt() * 1000L);
                    double temperature = response.body().getList().get(i).getData().getTemp();
                    System.out.println(response.body().getList().get(i).getDt());

                    adapter.add(new DailyForecastSimpleElement(date, temperature));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ForecastResponse> call, Throwable t) {
            }
        });
    }
}
