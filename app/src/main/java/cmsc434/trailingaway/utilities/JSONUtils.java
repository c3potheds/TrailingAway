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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
            List<RouteRowData> data = gson.fromJson(reader, new TypeToken<ArrayList<RouteRowData>>() {
                }.getType());
            Collections.sort(data);
            return data;
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
            BufferedReader br = new BufferedReader(fr);
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while(line != null && !line.equals("")) {
                sb.append(line);
                line = br.readLine();
            }
//            Log.d("JSON", sb.toString());
            File f = new File(fileName);
            if(!f.exists())
                Log.e("JSON", "file doesn't exist");

            try {
                TrailingAwayPath path = gson.fromJson(sb.toString(), TrailingAwayPath.class);
//            TrailingAwayPath path = gson.fromJson(fr, new TypeToken<TrailingAwayPath>() {
//            }.getType());
                return path;
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            Log.e("ERROR", "Can't find file", e);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void latLonListToGson(String fileName, TrailingAwayPath data) {
        Gson gson = new Gson();
        try {
            File f = new File(fileName);
            if(!f.exists())
                f.createNewFile();

            FileWriter fw = new FileWriter(fileName);
            gson.toJson(data, fw);
            fw.close();
            Log.d("JSONUTILS", "Got here");
            Log.d("JSON", gson.toJson(data));
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            Log.d("JSON", line);
            TrailingAwayPath p = gson.fromJson(line, TrailingAwayPath.class);
            int i = p.describeContents();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Landmark> gsonToLandmarks(String fileName) {
        Gson gson = new Gson();
        try {
            File f = new File(fileName);
            if(!f.exists()) {
                Log.e("JSON", "file doesn't exist");
                return new ArrayList<Landmark>();
            }
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();

            ArrayList<Landmark> landmarks = gson.fromJson(line, new TypeToken<ArrayList<Landmark>>() {
            }.getType());

            return landmarks;
        } catch (FileNotFoundException e) {
            Log.e("ERROR", "Can't find file", e);
            return new ArrayList<Landmark>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<Landmark>();
        }
    }

    public static void landmarksToGson(String fileName, List<Landmark> data) {
        Gson gson = new Gson();
        try {
            File f = new File(fileName);
            if(!f.exists())
                f.createNewFile();

            FileWriter fw = new FileWriter(fileName);
            gson.toJson(data, fw);
            fw.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
