package cmsc434.trailingaway;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;


/**
 * Created by Simeon on 12/4/14.
 */
public class TAMapView extends MapView {

    public TAMapView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public TAMapView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet, 0);
    }

    public TAMapView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        init(context, attributeSet, defStyle);
    }

    private void init(Context context, AttributeSet attributeSet, final int defStyle) {
        //pass
    }

    /*

    //private boolean _isRecording = true;
    private Path _path = new Path();
    private Paint _pathPaint = new Paint();
    private Paint _cursorPaint = new Paint();
    private Location _startLocation = null;
    private Location _currentLocation = null;
    private Rect _bounds = new Rect(0, 0, getWidth(), getHeight());
    private Matrix _zoomMatrix = new Matrix();
    private boolean _zooming = false;
    private ScaleGestureDetector _scaleDetector;

    public TAMapView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public TAMapView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet, 0);
    }

    public TAMapView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        init(context, attributeSet, defStyle);
    }

    private void init(Context context, AttributeSet attributeSet, final int defStyle) {
        _pathPaint.setColor(Color.CYAN);
        _pathPaint.setStyle(Paint.Style.STROKE);
        _pathPaint.setStrokeWidth(4);
        _cursorPaint.setColor(Color.BLUE);
        _cursorPaint.setStyle(Paint.Style.FILL);

        _scaleDetector = new ScaleGestureDetector(context,
                new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                _zoomMatrix.preScale(detector.getScaleFactor(), detector.getScaleFactor(),
                        detector.getFocusX(), detector.getFocusY());
                _zooming = true;
                return true;
            }
        });
    }

    public void updateLocation(Location location) {

        if (_startLocation == null) {
            _startLocation = location;
            _path.moveTo(longitudeToX(_startLocation), latitudeToY(_startLocation));
        }
        float x = longitudeToX(location);
        float y = latitudeToY(location);
        _path.lineTo(x, y);
        _bounds.union((int)x, (int)y);
        _currentLocation = location;
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate((getWidth()-_bounds.width())/2, (getHeight()-_bounds.height())/2);
        if (!_zooming) {
            canvas.scale(_bounds.width() / getWidth(), _bounds.height() / getHeight());
        }
        canvas.drawPath(_path, _pathPaint);
        if (_startLocation != null) {
            drawYouCursor(canvas, longitudeToX(_currentLocation), latitudeToY(_currentLocation),
                    _currentLocation.getBearing());

        }
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        _scaleDetector.onTouchEvent(event);
        return true;
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

*/
}
