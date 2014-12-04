package cmsc434.trailingaway;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MapActivity extends Activity implements LocationListener {

    private LocationManager _locationManager;
    private MapView _mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        _mapView = (MapView)findViewById(R.id.mapView);
        _locationManager = (LocationManager)this.getSystemService(
                Context.LOCATION_SERVICE);
        //Use GPS, poll every 1000 milliseconds
        _locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);

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
    public void onLocationChanged(Location location) {

        TextView locationTextView = (TextView)findViewById(R.id.textViewLocation);

        locationTextView.setText("Location: " + _mapView.longitudeToX(location) + ", " +
                _mapView.latitudeToY(location));
        TextView accuracyView = (TextView)findViewById(R.id.textViewAccuracy);
        accuracyView.setText("Accuracy: " + location.getAccuracy() + " m");
        _mapView.updateLocation(location);

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

}
