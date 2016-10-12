package org.openstack.android.summit.common.DTOs;

import java.util.List;

/**
 * Created by Claudio Redi on 11/18/2015.
 */
public class VenueDTO extends VenueListItemDTO {

    private String address;
    private List<String> maps;
    private List<String> images;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getMaps() {
        return maps;
    }

    public void setMaps(List<String> maps) {
        this.maps = maps;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
