package org.openstack.android.summit.common.entities;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Claudio Redi on 1/20/2016.
 */
public class TrackGroup extends RealmObject implements INamedEntity, ISummitOwned {
    @PrimaryKey
    private int id;
    private String name;
    private String description;
    private String color;
    private RealmList<Track> tracks = new RealmList<>();
    private Summit summit;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public RealmList<Track> getTracks() {
        return tracks;
    }

    public void setTracks(RealmList<Track> tracks) {
        this.tracks = tracks;
    }

    @Override
    public Summit getSummit() {
        return summit;
    }

    @Override
    public void setSummit(Summit summit) {
        this.summit = summit;
    }
}
