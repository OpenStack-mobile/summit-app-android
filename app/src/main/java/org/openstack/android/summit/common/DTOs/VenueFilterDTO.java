package org.openstack.android.summit.common.DTOs;

public class VenueFilterDTO extends NamedDTO {

    private boolean hasRooms;

    public boolean isHasRooms() {
        return hasRooms;
    }

    public void setHasRooms(boolean hasRooms) {
        this.hasRooms = hasRooms;
    }
}
