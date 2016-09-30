package org.openstack.android.summit.common.entities;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sebastian on 7/26/2016.
 */
public class VenueFloor extends RealmObject implements INamedEntity {

    @PrimaryKey
    private int id;
    private String name;
    private String pictureUrl;
    private String description;
    private Venue venue;

    public RealmList<VenueRoom> getRooms() {
        if(rooms == null) rooms = new RealmList<>();
        return rooms;
    }

    public void setRooms(RealmList<VenueRoom> rooms) {
        this.rooms = rooms;
    }

    private int number;
    private RealmList<VenueRoom> rooms = new RealmList<>();

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
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

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }
}
