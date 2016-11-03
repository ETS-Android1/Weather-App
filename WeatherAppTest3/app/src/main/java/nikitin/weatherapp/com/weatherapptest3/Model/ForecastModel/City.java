package nikitin.weatherapp.com.weatherapptest3.Model.ForecastModel;

import com.google.gson.annotations.SerializedName;

import nikitin.weatherapp.com.weatherapptest3.Model.WeatherModel.Coordinates;


/**
 * Created by Влад on 26.10.2016.
 */
public class City {
    @SerializedName("id")
    int id;
    @SerializedName("name")
    String name;
    @SerializedName("coord")
    Coordinates coord;
    @SerializedName("country")
    String country;
    @SerializedName("population")
    int population;

    public City(int id, String name, Coordinates coord, String country, int population) {
        this.id = id;
        this.name = name;
        this.coord = coord;
        this.country = country;
        this.population = population;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoord() {
        return coord;
    }
    public void setCoord(Coordinates coord) {
        this.coord = coord;
    }

    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }

    public int getPopulation() {
        return population;
    }
    public void setPopulation(int population) {
        this.population = population;
    }
}
