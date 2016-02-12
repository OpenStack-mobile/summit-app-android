package org.openstack.android.summit.common.DTOs;

/**
 * Created by Claudio Redi on 11/18/2015.
 */
public class VenueListItemDTO extends NamedDTO {
    private String lat;
    private String lng;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
