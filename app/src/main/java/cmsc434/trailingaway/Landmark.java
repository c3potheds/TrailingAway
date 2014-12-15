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
    private Bitmap _photo;
    private LatLng _location;

    public Landmark(String name, String description, Bitmap photo, LatLng location) {
        _name = name;
        _description = description;
        _photo = photo;
        _location = location;
    }

    public String getName() {
        return _name;
    }


    public void setName(String _name) {
        this._name = _name;
    }

    public String getDescription() {
        return _description;
    }

    public void setDescription(String _description) {
        this._description = _description;
    }

    public Bitmap getPhoto() {
        return _photo;
    }

    public void setPhoto(Bitmap _photo) {
        this._photo = _photo;
    }

    public LatLng getLocation() {
        return _location;
    }

    public void setLocation(LatLng _location) {
        this._location = _location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(_photo, flags);
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
        int length = in.readInt();
        _photo = in.readParcelable(Bitmap.class.getClassLoader());
        _name = in.readString();
        _description = in.readString();
        _location = in.readParcelable(LatLng.class.getClassLoader());
    }
}
