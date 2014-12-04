package cmsc434.trailingaway;

import java.io.Serializable;

/**
 * Created by Bryan on 12/2/2014.
 */
public class RouteRowData implements Serializable {

    private RouteType routeType;
    private String title;
    private double distance;

    public RouteRowData(RouteType rt, String title, double distance) {
        this.routeType = rt;
        this.title = title;
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public RouteType getRouteType() {
        return routeType;
    }

    public void setRouteType(RouteType routeType) {
        this.routeType = routeType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}

enum RouteType {
    BIKE,
    WALK,
    HIKE;
}