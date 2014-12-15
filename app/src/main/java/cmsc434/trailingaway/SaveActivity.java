package cmsc434.trailingaway;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;

import cmsc434.trailingaway.utilities.JSONUtils;

/**
 * Created by Sam on 12/11/2014.
 */
public class SaveActivity extends Activity {

    private RouteRowData data;
    private RouteType routeType = RouteType.WALK;
    private TrailingAwayPath _path;
    private ArrayList<Landmark> _landmarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        _path = getIntent().getParcelableExtra("path");
        //TODO:Fix this parcelable stuff
//        _landmarks = getIntent().getParcelableArrayListExtra("landmarks");


        data = new RouteRowData(null, null, null, null);

        final Spinner spinner = (Spinner) findViewById(R.id.route_type);
        final ImageView img = (ImageView) findViewById(R.id.route_type_image);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spinner.getSelectedItem().toString().equals("Walk")) {
                    routeType = RouteType.WALK;
                    img.setImageResource(R.drawable.ic_walk);
                }
                else if (spinner.getSelectedItem().toString().equals("Bicycle")) {
                    routeType = RouteType.BIKE;
                    img.setImageResource(R.drawable.ic_bicycle);
                }
                else if (spinner.getSelectedItem().toString().equals("Hike")) {
                    routeType = RouteType.HIKE;
                    img.setImageResource(R.drawable.ic_hiker);
                }

                data.setRouteType(routeType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onSaveClick(View view) {
        EditText name = (EditText) findViewById(R.id.route_name);
        name = (EditText) findViewById(R.id.route_name);
        data.setTitle(name.getText().toString());
        EditText desc = (EditText) findViewById(R.id.description);
        data.setDescription(desc.getText().toString());
        data.setFolderLocation(getFilesDir() + "/" + data.getTitle() + "/");
        File dir = new File(data.getFolderLocation());
        dir.mkdir();
        JSONUtils.addRouteDataToGson(data, getFilesDir() + "/" + getString(R.string.route_file));
        JSONUtils.latLonListToGson(data.getFolderLocation() + getString(R.string.latlng_file), _path);
        JSONUtils.landmarksToGson(data.getFolderLocation() + R.string.landmark_file, _landmarks);
        Intent intent = new Intent(this, RoutesActivity.class);
        startActivity(intent);
    }

    public void onCancelClick(View view) {
        finish();
    }
}
