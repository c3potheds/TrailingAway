package cmsc434.trailingaway;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;


public class MapActivity extends Activity implements GoogleMap.OnMyLocationChangeListener, OnMapReadyCallback {

    public static final float PATH_WIDTH = 5.0f;

    private TAMapView _TA_mapView;
    private List<LatLng> _path;
    private LatLng _previous;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        _TA_mapView = (TAMapView)findViewById(R.id.mapView);
        _TA_mapView.getMapAsync(this);
        _path = new ArrayList<LatLng>();
    }


    public void onSaveRouteClick(View view) {
        Log.i("map", "onSaveRouteClick");
        Intent intent = new Intent(this, SaveActivity.class);
        intent.putExtra("path", _path.toArray());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMyLocationChange(Location location) {
        LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
        _path.add(current);
        if (_previous == null) {
            _previous = current;
            return;
        }
        _TA_mapView.getMap().addPolyline(new PolylineOptions()
                .add(_previous, current)
                .width(PATH_WIDTH)
                .color(Color.BLUE));
        LatLngBounds.Builder boundsBuilder = LatLngBounds.builder();
        for (LatLng latLng : _path) {
            boundsBuilder.include(latLng);
        }
        _TA_mapView.getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(
                boundsBuilder.build(), _TA_mapView.getWidth()*3/4, _TA_mapView.getHeight()*3/4,10));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setOnMyLocationChangeListener(this);
    }

    /*
    @Override
    public void onLocationChanged(Location location) {

        TextView locationTextView = (TextView)findViewById(R.id.textViewLocation);

        locationTextView.setText("Location: " + _TA_mapView.longitudeToX(location) + ", " +
                _TA_mapView.latitudeToY(location));
        TextView accuracyView = (TextView)findViewById(R.id.textViewAccuracy);
        accuracyView.setText("Accuracy: " + location.getAccuracy() + " m");
        _TA_mapView.updateLocation(location);

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
    */

}
