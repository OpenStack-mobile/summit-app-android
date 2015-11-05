package org.openstack.android.openstacksummit.common.entities;

import io.realm.RealmObject;

/**
 * Created by Claudio Redi on 11/4/2015.
 */
public class VenueRoom extends RealmObject implements INamedEntity {
    private int id;
    private String name;
    private int capacity;

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

    /*public var venue: Venue {
        return linkingObjects(Venue.self, forProperty: "venueRooms").first!
    }

    public var events: [SummitEvent] {
        return linkingObjects(SummitEvent.self, forProperty: "venueRoom")
    }*/
}
