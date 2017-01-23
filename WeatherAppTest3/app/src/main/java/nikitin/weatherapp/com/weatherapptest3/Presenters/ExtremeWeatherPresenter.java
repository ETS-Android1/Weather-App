package nikitin.weatherapp.com.weatherapptest3.Presenters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nikitin.weatherapp.com.weatherapptest3.Model.SpaceWeather.GeomagneticStorm;
import nikitin.weatherapp.com.weatherapptest3.View.ExtremeWeatherFragment;
import nikitin.weatherapp.com.weatherapptest3.rest.ApiClient;
import nikitin.weatherapp.com.weatherapptest3.rest.OpenWeatherMapAPI;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Uladzislau_Nikitsin on 1/20/2017.
 */

public class ExtremeWeatherPresenter {
    ExtremeWeatherFragment view;
    public ExtremeWeatherPresenter(ExtremeWeatherFragment view) {
        this.view = view;
    }

    public void getSpaceWeatherData() {
        OpenWeatherMapAPI openWeatherMapAPI = OpenWeatherMapAPI.getNewInstance(ApiClient.Urls.SPACEWEATHER);
        openWeatherMapAPI.getGeomagneticStormData(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().byteStream()));
                String result = "";
                String line;
                try {
                    while ((line = reader.readLine())!= null) {
                        result += line;
                    }
                } catch (IOException ex) {
                    ex.getMessage();
                }
                Pattern yearPattern = Pattern.compile("Issued:\\s(\\d\\d\\d\\d)");
                Pattern datePattern = Pattern.compile("forecast\\s(\\d\\d?)\\s(\\w\\w\\w)");
                Pattern timeAndValuePattern = Pattern.compile("(\\d\\d)-\\d\\dUT\\s+(\\d)\\s+(\\d)");
                Matcher m = datePattern.matcher(result);
                m.find();
                System.out.println(m.group(1));
                System.out.println(m.group(2));


                m = timeAndValuePattern.matcher(result);
                System.out.println(result);
                for (int i = 0; i < 8; i ++) {
                    m.find();
                    System.out.println(m.group(1));
                    System.out.println(m.group(2));
                    System.out.println(m.group(3));
                }





                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                System.out.println(timestamp.getTime());


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }



}
