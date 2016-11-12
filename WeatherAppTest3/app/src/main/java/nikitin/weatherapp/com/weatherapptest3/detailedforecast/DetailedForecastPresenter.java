package nikitin.weatherapp.com.weatherapptest3.detailedforecast;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import nikitin.weatherapp.com.weatherapptest3.R;

/**
 * Created by Влад on 11.11.2016.
 */
public class DetailedForecastPresenter {
    private DetailedForecastActivity view;
    private int maxWidth = 1080;
    private int maxHeight = 1920;
    DetailedForecastPresenter(DetailedForecastActivity view) {
        this.view = view;
    }


    public Bitmap decodeSampledBitmapFromResource(Resources res) {
        int resId = R.drawable.main_background3;
        final BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);

        options.inJustDecodeBounds = false;
        options.inPreferredConfig =  Bitmap.Config.RGB_565;

        Bitmap bm = BitmapFactory.decodeResource(res, resId, options);
        return bm;
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
}
