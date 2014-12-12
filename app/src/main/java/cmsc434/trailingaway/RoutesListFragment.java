package cmsc434.trailingaway;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cmsc434.trailingaway.utilities.JSONUtils;

/**
 * Created by Bryan on 12/3/2014.
 */
public class RoutesListFragment extends ListFragment {

    String filesDir;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null)
            filesDir = bundle.getString("filesDir") + "/";

        //TODO: Remove this placeholder once we can save routes
        RouteRowData[] values = new RouteRowData[] {
                null,
                new RouteRowData(RouteType.BIKE, "Hello", "Hello"),
                new RouteRowData(RouteType.WALK, "Walking route", "Hello")};
        ArrayList<RouteRowData> list = new ArrayList<RouteRowData>();

        for(int i = 0; i < values.length; i++) {
            list.add(values[i]);
        }

        String result = JSONUtils.routeDataToGson(list, filesDir + R.string.route_file);

        List<RouteRowData> fromFile = JSONUtils.getRouteRowData(filesDir + R.string.route_file);
        RouteRowData[] vals = (RouteRowData[]) fromFile.toArray(values);
        RoutesArrayAdapter adapter = new RoutesArrayAdapter(getActivity(), vals);
        setListAdapter(adapter);
    }
}
