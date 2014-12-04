package cmsc434.trailingaway;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

            //Set background to differentiate
            rowView.setBackgroundColor(Color.GREEN);
            //Set icon to "+"
            ImageView icon = (ImageView) rowView.findViewById(R.id.icon);
            icon.setImageResource(R.drawable.ic_add);

            TextView addText = (TextView) rowView.findViewById(R.id.firstLine);
            addText.setText("Add New Route");

            Button button = (Button) rowView.findViewById(R.id.routeButton);
            button.setText("Add");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), MapActivity.class);
                    v.getContext().startActivity(intent);
                }
            });

            return rowView;
        }

        //Set route name
        TextView routeName = (TextView) rowView.findViewById(R.id.firstLine);
        routeName.setText(values[position].getTitle());

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


        return rowView;
    }

}
