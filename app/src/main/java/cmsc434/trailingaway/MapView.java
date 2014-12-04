package cmsc434.trailingaway;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Simeon on 12/4/14.
 */
public class MapView extends View implements LocationListener {

    private boolean _isRecording = true;
    private Path _path = new Path();
    private Paint _pathPaint = new Paint();
    private Paint _cursorPaint = new Paint();
    private Location _startLocation = null;
    private Location _currentLocation = null;

    public MapView(Context context) {
        super(context);
        init(null, 0);
    }

    public MapView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(attributeSet, 0);
    }

    public MapView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        init(attributeSet, defStyle);
    }

    private void init(AttributeSet attributeSet, int defStyle) {
        _pathPaint.setColor(Color.CYAN);
        _pathPaint.setStyle(Paint.Style.STROKE);
        _cursorPaint.setColor(Color.BLUE);
        _cursorPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onLocationChanged(Location location) {

        TextView locationTextView = (TextView)findViewById(R.id.textViewLocation);

        if (locationTextView == null) {
            Log.i("a", "qwertyui");
            return;
        }
        locationTextView.setText("Location: " + location.getLatitude() + ", " +
                location.getLongitude());
        TextView accuracyView = (TextView)findViewById(R.id.textViewAccuracy);
        accuracyView.setText("Accuracy: " + location.getAccuracy() + " m");
        if (_startLocation == null) {
            _startLocation = location;
            _path.moveTo(longitudeToX(_startLocation), latitudeToY(_startLocation));
        }
        float x = longitudeToX(location);
        float y = latitudeToY(location);
        _path.lineTo(x, y);
        _currentLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        TextView statusTextView = (TextView)findViewById(R.id.textViewGPSStatus);
        switch (status) {
            case LocationProvider.AVAILABLE:
                statusTextView.setText("GPS Status: Available");
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                statusTextView.setText("GPS Status: Temporarily unavailable");
                break;
            case LocationProvider.OUT_OF_SERVICE:
                statusTextView.setText("GPS Status: Out of service");
                break;
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        System.out.println(provider + " enabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        System.out.println(provider + " disabled");
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(_path, _pathPaint);
        if (_startLocation != null) {
            drawYouCursor(canvas, longitudeToX(_currentLocation), latitudeToY(_currentLocation),
                    _currentLocation.getBearing());

        }
    }

    private void drawYouCursor(Canvas canvas, float x, float y, float orientation) {
        float x1, x2, x3, y1, y2, y3;
        x1 = x+32.0f*(float)Math.cos(orientation);
        x2 = x+18.0f*(float)Math.cos(orientation+Math.toRadians(160));
        x3 = x+18.0f*(float)Math.cos(orientation+Math.toRadians(200));
        y1 = x+32.0f*(float)Math.sin(orientation);
        y2 = x+18.0f*(float)Math.sin(orientation+Math.toRadians(160));
        y3 = x+18.0f*(float)Math.sin(orientation+Math.toRadians(200));
        canvas.drawLine(x1, y1, x2, y2, _cursorPaint);
        canvas.drawLine(x2, y2, x3, y3, _cursorPaint);
        canvas.drawLine(x3, y3, x1, y1, _cursorPaint);
    }

    private float longitudeToX(Location location) {
        return getWidth()/2+_startLocation.distanceTo(location) *
                (float)Math.cos(-_startLocation.bearingTo(location)+90);
    }

    private float latitudeToY(Location location) {
        return getHeight()/2+_startLocation.distanceTo(location) *
                (float)Math.sin(-_startLocation.bearingTo(location)+90);
    }
}
