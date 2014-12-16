package cmsc434.trailingaway;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        LocationListener,
        LandmarkFragment.OnFragmentInteractionListener {

    /*
 * Define a request code to send to Google Play services
 * This code is returned in Activity.onActivityResult
 */
    private final static int
            CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    // Milliseconds per second
    private static final int MILLISECONDS_PER_SECOND = 1000;
    // Update frequency in seconds
    public static final int UPDATE_INTERVAL_IN_SECONDS = 5;
    // Update frequency in milliseconds
    private static final long UPDATE_INTERVAL =
            MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
    // The fastest update frequency, in seconds
    private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
    // A fast frequency ceiling in milliseconds
    private static final long FASTEST_INTERVAL =
            MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;

    // Define an object that holds accuracy and frequency parameters
    LocationRequest mLocationRequest;
    LocationClient mLocationClient;
    boolean mUpdatesRequested;
    static final float PATH_WIDTH = 5f;
    private GoogleMap _map; // Might be null if Google Play services APK is not available.
    private View _hiddenPanel;

    private TrailingAwayPath _path;
    private ArrayList<Landmark> _landmarks;
    private HashMap<String, String> _markers;
    private LatLng _previous;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        _path = new TrailingAwayPath();
        _landmarks = new ArrayList<Landmark>();
        _markers = new HashMap<String, String>();
        mLocationClient = new LocationClient(this, this, this);
        mUpdatesRequested = false;
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(MILLISECONDS_PER_SECOND);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        _hiddenPanel = findViewById(R.id.layoutLandmarkPanel);
        setUpMapIfNeeded();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLocationClient.connect();
    }

    @Override
    protected void onStop(){
        mLocationClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #_map} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (_map == null) {
            // Try to obtain the map from the SupportMapFragment.
            _map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (_map != null) {
                setUpMap();
            }
        }
    }

    public void onSaveRouteClick(View view) {
        Log.i("map", "onSaveRouteClick");
        Intent intent = new Intent(this, SaveActivity.class);
        intent.putExtra("path", _path);
        //intent.putParcelableArrayListExtra("landmarks", _landmarks);
        startActivity(intent);
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #_map} is not null.
     */
    private void setUpMap() {

        _map.getUiSettings().setZoomControlsEnabled(false);
        _map.setMyLocationEnabled(true);
        _map.setInfoWindowAdapter(new TAInfoWindow());
        mLocationClient.connect();

        //Set it to last location
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> matchingProviders = locationManager.getAllProviders();
        Location bestResult = null;
        float bestAccuracy = Float.MAX_VALUE;
        float bestTime = Float.MAX_VALUE;
        float minTime = Float.MIN_VALUE;
        for (String provider: matchingProviders) {
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                float accuracy = location.getAccuracy();
                long time = location.getTime();

                if ((time > minTime && accuracy < bestAccuracy)) {
                    bestResult = location;
                    bestAccuracy = accuracy;
                    bestTime = time;
                }
                else if (time < minTime &&
                        bestAccuracy == Float.MAX_VALUE && time > bestTime){
                    bestResult = location;
                    bestTime = time;
                }
            }
        }

        if(bestResult != null) {
            LatLng last = new LatLng(bestResult.getLatitude(), bestResult.getLongitude());
            CameraUpdate center = CameraUpdateFactory.newLatLng(last);
            _map.moveCamera(center);
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
            _map.moveCamera(zoom);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
        _path.add(current);
        if (_previous == null) {
            _previous = current;
            return;
        }
        _map.addPolyline(new PolylineOptions()
                .add(_previous, current)
                .width(PATH_WIDTH)
                .color(Color.BLUE));
        _previous = current;
        LatLngBounds.Builder boundsBuilder = LatLngBounds.builder();
        for (LatLng latLng : _path) {
            boundsBuilder.include(latLng);
        }
        _map.animateCamera(CameraUpdateFactory.newLatLngBounds(
                boundsBuilder.build(), 100));

    }

    @Override
    public void onLandmarkCreated(Landmark landmark) {
        //set the location from the latest
        landmark.set_location(_previous);

        MarkerOptions mo = new MarkerOptions()
                .position(landmark.get_location())
                .title(landmark.get_name())
                .snippet(landmark.get_description());
        _landmarks.add(landmark);
        Marker m = _map.addMarker(mo);
        //If it has a photo, we'll store the image location
        if (landmark.get_photo_location() != null && !landmark.get_photo_location().equals(" "))
            _markers.put(m.getId(), landmark.get_photo_location());
    }



    // Define a DialogFragment that displays the error dialog
    public static class ErrorDialogFragment extends DialogFragment {
        // Global field to contain the error dialog
        private Dialog mDialog;
        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }
        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }
        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }

    /*
     * Handle results returned to the FragmentActivity
     * by Google Play services
     */
    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        // Decide what to do based on the original request code
        switch (requestCode) {
            case CONNECTION_FAILURE_RESOLUTION_REQUEST :
            /*
             * If the result code is Activity.RESULT_OK, try
             * to connect again
             */
                switch (resultCode) {
                    case Activity.RESULT_OK :
                    /*
                     * Try the request again
                     */
                        break;
                }
        }
    }

//
    private boolean servicesConnected() {
        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.
                        isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates",
                    "Google Play services is available.");
            // Continue
            return true;
            // Google Play services was not available for some reason.
            // resultCode holds the error code.
        } else {
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                    resultCode,
                    this,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                ErrorDialogFragment errorFragment =
                        new ErrorDialogFragment();
                // Set the dialog in the DialogFragment
                errorFragment.setDialog(errorDialog);
                // Show the error dialog in the DialogFragment
                errorFragment.show(getSupportFragmentManager(),
                        "Location Updates");
            }
        }
        return true;
    }

    /*
     * Called by Location Services when the request to connect the
     * client finishes successfully. At this point, you can
     * request the current location or start periodic updates
     */
    @Override
    public void onConnected(Bundle dataBundle) {
        mLocationClient.requestLocationUpdates(mLocationRequest, this);

    }
    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onDisconnected() {

    }
    /*
     * Called by Location Services if the attempt to
     * Location Services fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            //showErrorDialog(connectionResult.getErrorCode());
        }
    }

    //TODO: Fix the R.anim.bottom_up
    public void onAddLandmarkClick(View v) {
        if (!isPanelShown()) {
            // Show the panel
            Animation bottomUp = AnimationUtils.loadAnimation(this,
                    R.anim.bottom_down);

            _hiddenPanel.startAnimation(bottomUp);
            _hiddenPanel.setVisibility(View.VISIBLE);
        } else {
            // Hide the Panel
            Animation bottomDown = AnimationUtils.loadAnimation(this,
                    R.anim.bottom_down);

            _hiddenPanel.startAnimation(bottomDown);
            _hiddenPanel.setVisibility(View.GONE);
        }
    }

    private boolean isPanelShown() {
                return _hiddenPanel.getVisibility() == View.VISIBLE;
        }


    class TAInfoWindow implements GoogleMap.InfoWindowAdapter {

        private final View myContents;

        TAInfoWindow() {
            myContents = getLayoutInflater().inflate(R.layout.ta_info_window, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            TextView title = (TextView) myContents.findViewById(R.id.info_title);
            TextView desc = (TextView) myContents.findViewById(R.id.info_description);
            ImageView image = (ImageView) myContents.findViewById(R.id.info_icon);
            title.setText(marker.getTitle());
            desc.setText(marker.getSnippet());
            String loc;
            if ((loc = _markers.get(marker.getId())) != null) {
                Bitmap bMap = BitmapFactory.decodeFile(loc);
                image.setImageBitmap(bMap);
            } else
                image.setVisibility(View.INVISIBLE);

            return myContents;
        }

    }
}
