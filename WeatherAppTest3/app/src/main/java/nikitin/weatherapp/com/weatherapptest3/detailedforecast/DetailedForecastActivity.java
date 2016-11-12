package nikitin.weatherapp.com.weatherapptest3.detailedforecast;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import nikitin.weatherapp.com.weatherapptest3.R;

/**
 * Created by Влад on 11.11.2016.
 */
public class DetailedForecastActivity extends AppCompatActivity {

    private DetailedForecastPresenter presenter;
    private ImageView backgroundView;

    @Override
    protected void onCreate(Bundle saveInstantState) {
        super.onCreate(saveInstantState);
        setContentView(R.layout.activity_detailed_forecast);
        backgroundView = (ImageView) findViewById(R.id.detailed_forecast_background);

        presenter = new DetailedForecastPresenter(this);
        createBackground();
    }

    public void createBackground() {
        Bitmap bitmap = presenter.decodeSampledBitmapFromResource(getResources());
        backgroundView.setImageBitmap(bitmap);
    }
}
