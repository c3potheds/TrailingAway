package cmsc434.trailingaway;

import android.graphics.Bitmap;
import android.media.Image;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by apple on 12/14/14.
 */
public class Landmark {
    private String _name;
    private String _description;
    private Bitmap _photo;
    private LatLng _location;

    public Landmark(String name, String description, Bitmap photo, LatLng location) {
        _name = name;
        _description = description;
        _photo = photo;
        _location = location;
    }

    public LatLng getLocation() {
        return _location;
    }

    public String getName() {
        return _name;
    }

    public String getDescription() {
        return _description;
    }

    public Bitmap getPhoto() {
        return _photo;
    }
}
