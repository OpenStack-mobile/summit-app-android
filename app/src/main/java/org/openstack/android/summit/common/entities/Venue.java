package org.openstack.android.summit.common.entities;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Claudio Redi on 11/4/2015.
 */
public class Venue extends RealmObject implements INamedEntity {
    @PrimaryKey
    private int id;
    private String name;
    private String locationDescription;
    private String address;
    private String city;
    private String zipCode;
    private String state;
    private String country;
    private String lat;
    private String lng;
    private Boolean isInternal;
    private RealmList<Image> maps        = new RealmList<>();
    private RealmList<Image> images      = new RealmList<>();
    private RealmList<VenueFloor> floors = new RealmList<>();

    public RealmList<VenueFloor> getFloors() {
        if(floors == null) floors = new RealmList<>();
        return floors;
    }

    public void setFloors(RealmList<VenueFloor> floors) {
        this.floors = floors;
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

    public String getLocationDescription() {
        return locationDescription;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

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

    public Boolean getIsInternal() {
        return isInternal;
    }

    public void setIsInternal(Boolean isInternal) {
        this.isInternal = isInternal;
    }

    public RealmList<Image> getMaps() {
        if(maps == null) maps = new RealmList<>();
        return maps;
    }

    public void setMaps(RealmList<Image> maps) {
        this.maps = maps;
    }

    public RealmList<Image> getImages() {
        if(images == null) images = new RealmList<>();
        return images;
    }

    public void setImages(RealmList<Image> images) {
        this.images = images;
    }
}
