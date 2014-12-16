package cmsc434.trailingaway;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cmsc434.trailingaway.utilities.JSONUtils;

/**
 * Created by Bryan on 12/3/2014.
 */
public class RoutesListFragment extends ListFragment {

    private String filesDir;
    private String buttonMessage;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            filesDir = bundle.getString("filesDir") + "/";
            buttonMessage = bundle.getString("buttonMessage");

        }

        List<RouteRowData> fromFile = JSONUtils.getRouteRowData(filesDir + getString(R.string.route_file));
        RouteRowData[] vals = (RouteRowData[]) fromFile.toArray(new RouteRowData[]{});

        if(getListAdapter() == null) {
            RoutesArrayAdapter adapter = new RoutesArrayAdapter(getActivity(), vals, buttonMessage);
            setListAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            Log.d("RoutesListFragment", "Adapter already made");
            RoutesArrayAdapter adapter = (RoutesArrayAdapter) getListAdapter();
            adapter.clear();
            adapter.addAll(vals);
        }
    }
}
