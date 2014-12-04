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
public class MapView extends View {

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
        _pathPaint.setStrokeWidth(4);
        _cursorPaint.setColor(Color.BLUE);
        _cursorPaint.setStyle(Paint.Style.FILL);
    }

    public void updateLocation(Location location) {

        if (_startLocation == null) {
            _startLocation = location;
            _path.moveTo(longitudeToX(_startLocation), latitudeToY(_startLocation));
        }
        float x = longitudeToX(location);
        float y = latitudeToY(location);
        _path.lineTo(x, y);
        _currentLocation = location;
        invalidate();
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
        x1 = x+64.0f*(float)Math.cos(orientation);
        x2 = x+32.0f*(float)Math.cos(orientation+Math.toRadians(160));
        x3 = x+32.0f*(float)Math.cos(orientation+Math.toRadians(200));
        y1 = x+64.0f*(float)Math.sin(orientation);
        y2 = x+32.0f*(float)Math.sin(orientation+Math.toRadians(160));
        y3 = x+32.0f*(float)Math.sin(orientation+Math.toRadians(200));
        canvas.drawLine(x1, y1, x2, y2, _cursorPaint);
        canvas.drawLine(x2, y2, x3, y3, _cursorPaint);
        canvas.drawLine(x3, y3, x1, y1, _cursorPaint);
    }

    public float longitudeToX(Location location) {
        if (_startLocation == null) {
            return 0.0f;
        }
        return getWidth()/2 + _startLocation.distanceTo(location) *
                (float)Math.cos(-_startLocation.bearingTo(location)+90);
    }

    public float latitudeToY(Location location) {
        if (_startLocation == null) {
            return 0.0f;
        }
        return getHeight()/2 + _startLocation.distanceTo(location) *
                (float)Math.sin(-_startLocation.bearingTo(location)+90);
    }
}
