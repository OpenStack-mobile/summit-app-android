package org.openstack.android.summit.common.entities;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Claudio Redi on 11/4/2015.
 */
public class VenueRoom extends RealmObject implements INamedEntity {
    @PrimaryKey
    private int id;
    private String name;
    private int capacity;
    private String locationDescription;
    private Venue venue;
    private VenueFloor floor;

    public VenueFloor getFloor() {
        return floor;
    }

    public void setFloor(VenueFloor floor) {
        this.floor = floor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    /*public var venue: Venue {
        return linkingObjects(Venue.self, forProperty: "venueRooms").first!
    }

    public var events: [SummitEvent] {
        return linkingObjects(SummitEvent.self, forProperty: "venueRoom")
    }*/
}
