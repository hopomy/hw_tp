package com.example.deliveryapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;

import java.io.IOException;
import java.util.List;

public class Address extends AppCompatActivity {
    MapView mapview;
    private Geocoder geocoder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Bundle arguments = getIntent().getExtras();
        String address = arguments.getString("address");

        geocoder = new Geocoder(this);
        Point point;
        try {
            List<android.location.Address> addresses = geocoder.getFromLocationName(address, 1);
            android.location.Address address1 = addresses.get(0);
            point = new Point(address1.getLatitude(), address1.getLongitude());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        mapview = (MapView) findViewById(R.id.mapview);
        mapview.getMap().move(
                new CameraPosition(point, 16.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);

        mapview.getMap().getMapObjects().addPlacemark(point, ImageProvider.fromBitmap(getBitmapFromVectorDrawable(this, R.drawable.baseline_location_on_24)));
    }
    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }@Override
    protected void onStop() {
        mapview.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    protected void onStart() {
        mapview.onStart();
        MapKitFactory.getInstance().onStart();
        super.onStart();
    }
}
