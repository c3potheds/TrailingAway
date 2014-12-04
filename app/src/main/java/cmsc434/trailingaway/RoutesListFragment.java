package cmsc434.trailingaway;

import android.app.ListFragment;
import android.os.Bundle;

import java.util.ArrayList;

/**
 * Created by Bryan on 12/3/2014.
 */
public class RoutesListFragment extends ListFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RouteRowData[] values = new RouteRowData[] {
                null,
                new RouteRowData(RouteType.BIKE, "Hello", 0),
                new RouteRowData(RouteType.WALK, "Walking route", 10)};
        ArrayList<RouteRowData> list = new ArrayList<RouteRowData>();
        for(int i = 0; i < values.length; i++) {
            list.add(values[i]);
        }
        RoutesArrayAdapter adapter = new RoutesArrayAdapter(getActivity(), values);
        setListAdapter(adapter);
    }
}
