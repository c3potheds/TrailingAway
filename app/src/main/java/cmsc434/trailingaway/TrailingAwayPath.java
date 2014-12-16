package cmsc434.trailingaway;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by apple on 12/12/14.
 */
public class TrailingAwayPath implements Parcelable, Iterable<LatLng> {

    public List<LatLng> get_points() {
        return _points;
    }

    public void set_points(List<LatLng> _points) {
        this._points = _points;
    }

    private List<LatLng> _points;

    public TrailingAwayPath() {
        _points = new ArrayList<LatLng>();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeInt(_points.size());
        for (LatLng point : _points) {
            out.writeDouble(point.latitude);
            out.writeDouble(point.longitude);
        }
    }

    public void add(LatLng point) {
        _points.add(point);
    }

    public static final Parcelable.Creator<TrailingAwayPath> CREATOR
            = new Parcelable.Creator<TrailingAwayPath>() {
        public TrailingAwayPath createFromParcel(Parcel in) {
            return new TrailingAwayPath(in);
        }

        public TrailingAwayPath[] newArray(int size) {
            return new TrailingAwayPath[size];
        }
    };


    private TrailingAwayPath(Parcel in) {
        _points = new ArrayList<LatLng>();
        int length = in.readInt();
        for (int i=0;i<length;i++) {
            double latitude = in.readDouble();
            double longitude = in.readDouble();
            _points.add(new LatLng(latitude, longitude));
        }
    }

    @Override
    public Iterator iterator() {
        return _points.iterator();
    }
}
