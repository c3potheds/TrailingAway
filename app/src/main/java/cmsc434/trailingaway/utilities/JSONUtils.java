package cmsc434.trailingaway.utilities;

import android.util.JsonReader;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cmsc434.trailingaway.Landmark;
import cmsc434.trailingaway.RouteRowData;
import cmsc434.trailingaway.TrailingAwayPath;

/**
 * Created by Bryan on 12/9/2014.
 */
public class JSONUtils {

    public static List<RouteRowData> getRouteRowData(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            Gson gson = new Gson();
            return gson.fromJson(reader, new TypeToken<ArrayList<RouteRowData>>() {
                }.getType());
        } catch (FileNotFoundException e) {
            //no files have been created. Create one with null.
            List<RouteRowData> data = new ArrayList<RouteRowData>();
            data.add(null);
            routeDataToGson(data, fileName);
            return data;
        }
    }

    public static String routeDataToGson(List<RouteRowData> data, String fileName) {
        Gson gson = new Gson();
        try {
            FileWriter fw = new FileWriter(fileName);
            gson.toJson(data, fw);
            fw.close();
            return "";
        } catch (IOException e) {
            return "";
        }
    }

    public static void addRouteDataToGson(RouteRowData data, String fileName) {
        List list = getRouteRowData(fileName);
        list.add(data);
        routeDataToGson(list, fileName);
    }

    public static TrailingAwayPath gsonToLatLonList(String fileName) {
        Gson gson = new Gson();
        try {
            FileReader fr = new FileReader(fileName);
            return gson.fromJson(fr, new TypeToken<TrailingAwayPath>() {
            }.getType());
        } catch (FileNotFoundException e) {
            Log.e("ERROR", "Can't find file", e);
        }

        return null;
    }

    public static void latLonListToGson(String fileName, TrailingAwayPath data) {
        Gson gson = new Gson();
        try {
            FileWriter fw = new FileWriter(fileName);
            gson.toJson(data, fw);
        } catch (IOException e) {
            return;
        }
    }

    public static List<Landmark> gsonToLandmarks(String fileName) {
        //TODO: implement a landmark class and change this to load it
        Gson gson = new Gson();
        try {
            FileReader fr = new FileReader(fileName);
            return gson.fromJson(fr, new TypeToken<ArrayList<LatLng>>() {
            }.getType());
        } catch (FileNotFoundException e) {
            Log.e("ERROR", "Can't find file", e);
        }
        return new ArrayList<Landmark>();
    }

    public static void landmarksToGson(String fileName, List<Landmark> data) {
        Gson gson = new Gson();
        try {
            FileWriter fw = new FileWriter(fileName);
            gson.toJson(data, fw);
        } catch (IOException e) {
            return;
        }
    }

}
