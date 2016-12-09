package org.openstack.android.summit.common.entities;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Claudio Redi on 11/4/2015.
 */
public class Track extends RealmObject implements INamedEntity, ISummitOwned {
    @PrimaryKey
    private int id;
    private String name;
    private RealmList<TrackGroup> trackGroups = new RealmList<>();
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

    public RealmList<TrackGroup> getTrackGroups() {
        if(trackGroups == null) trackGroups = new RealmList<>();
        return trackGroups;
    }

    public void setTrackGroups(RealmList<TrackGroup> trackGroup) {
        this.trackGroups = trackGroup;
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
