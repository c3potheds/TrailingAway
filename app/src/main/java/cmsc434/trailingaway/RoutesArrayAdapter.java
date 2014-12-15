package cmsc434.trailingaway;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cmsc434.trailingaway.utilities.JSONUtils;

/**
 * Created by Bryan on 12/2/2014.
 */
public class RoutesArrayAdapter extends ArrayAdapter<RouteRowData> {

    private final Context context;
    private final RouteRowData[] values;

    public RoutesArrayAdapter(Context context, RouteRowData[] values) {
        super(context, R.layout.routes_list_row, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.routes_list_row, parent, false);

        //Initial item, we want to set it to add.
        if(position == 0 || values[position] == null) {

            rowView.setAlpha(1);
            //Set background to differentiate
            rowView.setBackgroundColor(Color.GREEN);
            //Set icon to "+"
            ImageView icon = (ImageView) rowView.findViewById(R.id.icon);
            icon.setImageResource(R.drawable.ic_add);

            TextView addText = (TextView) rowView.findViewById(R.id.firstLine);
            addText.setText("Add New Route");

            Button button = (Button) rowView.findViewById(R.id.routeButton);
            button.setVisibility(View.INVISIBLE);

            rowView.setHapticFeedbackEnabled(true);

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //This wil start recording a new route
                    v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                    Intent intent = new Intent(v.getContext(), MapActivity.class);
                    v.getContext().startActivity(intent);
                }
            });

            return rowView;
        }

        //Set route name
        TextView routeName = (TextView) rowView.findViewById(R.id.firstLine);
        routeName.setText(values[position].getTitle());

        TextView desc = (TextView) rowView.findViewById(R.id.secondLine);
        desc.setText(values[position].getDescription());

        //Set route icon
        ImageView icon = (ImageView) rowView.findViewById(R.id.icon);
        switch (values[position].getRouteType()) {
            case BIKE:
                //Set to bike icon
                icon.setImageResource(R.drawable.ic_bicycle);
                break;
            case WALK:
                //Set to walking icon
                icon.setImageResource(R.drawable.ic_walk);
                break;
            case HIKE:
                //Set to hiking icon
                icon.setImageResource(R.drawable.ic_hiker);
                break;
        }


        Button button = (Button) rowView.findViewById(R.id.routeButton);
        //Make this object final so that we can access it in the inner class - can be avoided if we
        //make this an actual class w/constructor

        button.setTag(values[position].getFolderLocation());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String folderLocation = (String) v.getTag();
                //TODO: This method will start an activity that will display the route located at
                Intent intent = new Intent(getContext(), DisplayRoutes.class);
                //Pass the lists to the activity
                intent.putExtra("folderLocation", folderLocation);

                getContext().startActivity(intent);
            }
        });

        return rowView;
    }

}
