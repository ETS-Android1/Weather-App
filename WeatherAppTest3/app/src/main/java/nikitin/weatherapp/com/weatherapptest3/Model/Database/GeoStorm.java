package nikitin.weatherapp.com.weatherapptest3.Model.Database;

/**
 * Created by Uladzislau_Nikitsin on 1/25/2017.
 */

public class GeoStorm {
    private long date;
    private int kIndex;

    public GeoStorm(long date, int kIndex) {
        this.date = date;
        this.kIndex = kIndex;
    }

    public long getDate() {
        return date;
    }
    public void setDate(long date) {
        this.date = date;
    }

    public int getkIndex() {
        return kIndex;
    }
    public void setkIndex(int kIndex) {
        this.kIndex = kIndex;
    }

    @Override
    public String toString() {
        return "GeoStorm{" +
                "date=" + date +
                ", kIndex=" + kIndex +
                '}';
    }
}
