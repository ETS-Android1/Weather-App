package nikitin.weatherapp.com.weatherapptest3.Presenters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLOutput;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    final private String yearPatternString = "Issued:\\s(\\d\\d\\d\\d)";
    final private String datePatternString = "forecast\\s(\\d\\d?)\\s(\\w\\w\\w)";
    final private String timeAndIndexPattern = "(\\d\\d)-\\d\\dUT\\s+(\\d)\\s+(\\d)";
    public ExtremeWeatherPresenter(ExtremeWeatherFragment view) {
        this.view = view;
    }

    public void getSpaceWeatherData() {
        OpenWeatherMapAPI openWeatherMapAPI = OpenWeatherMapAPI.getNewInstance(ApiClient.Urls.SPACEWEATHER);
        openWeatherMapAPI.getGeomagneticStormData(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().byteStream()));
                String file = "";
                String line;
                try {
                    while ((line = reader.readLine())!= null) {
                        file += line;
                    }
                } catch (IOException ex) {
                    ex.getMessage();
                }




                Pattern yearPattern = Pattern.compile(yearPatternString);
                Pattern datePattern = Pattern.compile(datePatternString);

                Matcher m = yearPattern.matcher(file);

                m.find();
                String year = m.group(1);

                m = datePattern.matcher(file);
                m.find();
                String month = m.group(2);
                int day = Integer.parseInt(m.group(1));


                System.out.println("pish");
                System.out.println(year +" " +month +" " +day);

                String pattern = "yyyy MMM dd HH mm ss";

                SimpleDateFormat format = new SimpleDateFormat(pattern);
                try {
                    Date date = format.parse(year + " " +month +" " +day +" " +"12" +" " +"00" +" " +"00");
                    long time = date.getTime();
                    System.out.println("look at this ");
                    System.out.println(time);
                } catch (ParseException ex) {
                    System.out.println("failed");
                    System.out.println(ex.getMessage());
                }




                        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                System.out.println(timestamp.getTime());


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    private String findMatches(String file, String stringPattern) {
        Pattern pattern = Pattern.compile(stringPattern);
        Matcher matcher = pattern.matcher(file);
        matcher.find();
        String result = "";
        for (int i = 1; i <= matcher.groupCount(); i ++) {
            result +=matcher.group(i);
            if (i != matcher.groupCount()) result+=" ";
        }
        return result;
    }

    private void parseForecast(String file, String date) {
        Pattern pattern = Pattern.compile(date);
        Matcher matcher = pattern.matcher(file);
        while (matcher.find()) {

        }
    }
}
