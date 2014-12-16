package cmsc434.trailingaway;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by apple on 12/14/14.
 */
public class Landmark implements Parcelable {
    private String _name;
    private String _description;
    private String _photo_location;
    private LatLng _location;

    public Landmark() {

    }

    public Landmark(String name, String description, String photo_location, LatLng location) {
        _name = name;
        _description = description;
        _photo_location = photo_location;
        _location = location;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_description() {
        return _description;
    }

    public void set_description(String _description) {
        this._description = _description;
    }

    public String get_photo_location() {
        return _photo_location;
    }

    public void set_photo_location(String _photo_location) {
        this._photo_location = _photo_location;
    }

    public LatLng get_location() {
        return _location;
    }

    public void set_location(LatLng _location) {
        this._location = _location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_photo_location);
        dest.writeString(_name);
        dest.writeString(_description);
        dest.writeParcelable(_location, flags);
    }

    public static final Parcelable.Creator<Landmark> CREATOR
            = new Parcelable.Creator<Landmark>() {
        @Override
        public Landmark createFromParcel(Parcel in) {
            return new Landmark(in);
        }

        public Landmark[] newArray(int size) {
            return new Landmark[size];
        }
    };

    private Landmark(Parcel in) {
        _photo_location = in.readString();
        _name = in.readString();
        _description = in.readString();
        _location = in.readParcelable(LatLng.class.getClassLoader());
    }
}
