package nikitin.weatherapp.com.weatherapptest3;

/**
 * Created by Влад on 05.11.2016.
 */
public final class WeatherDrawable {
    private static final int RAIN = R.drawable.ic_rain;
    private static final int CLOUDS = R.drawable.ic_scattered_clouds;
    private static final int CLEAR = R.drawable.ic_clear_sky;

    public static int getDrawable(String weatherName) {
        weatherName = weatherName.toUpperCase();
        switch (weatherName) {
            case "THUNDERSTORM":
            case "DRIZZLE":
            case "RAIN":
            case "SNOW":
                return RAIN;
            case "CLOUDS":
                return CLOUDS;
            case "CLEAR":
                return CLEAR;
        }
        return CLEAR;
    }
}
