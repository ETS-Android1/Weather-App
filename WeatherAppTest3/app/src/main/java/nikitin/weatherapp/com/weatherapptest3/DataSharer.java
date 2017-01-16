package nikitin.weatherapp.com.weatherapptest3;

import java.util.ArrayList;

import nikitin.weatherapp.com.weatherapptest3.Model.Database.City;
import nikitin.weatherapp.com.weatherapptest3.Model.Database.Forecast;

/**
 * Created by Uladzislau_Nikitsin on 1/13/2017.
 */

public interface DataSharer {
    void shareForecast(ArrayList<Forecast> forecast);
    void shareCity(City city);
}
