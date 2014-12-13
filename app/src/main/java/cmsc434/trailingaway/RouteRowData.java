package cmsc434.trailingaway;

import java.io.Serializable;

/**
 * Created by Bryan on 12/2/2014.
 */
public class RouteRowData implements Serializable {

    private RouteType routeType;
    private String title;
    private String folderLocation;
    private String description;

    public RouteRowData(RouteType rt, String title, String description, String folderLocation) {
        this.routeType = rt;
        this.title = title;
        this.description = description;
        this.folderLocation = folderLocation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFolderLocation() {
        return folderLocation;
    }

    public void setFolderLocation(String folderLocation) {
        this.folderLocation = folderLocation;
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