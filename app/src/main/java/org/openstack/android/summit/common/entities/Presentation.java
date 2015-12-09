package org.openstack.android.summit.common.entities;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Claudio Redi on 11/4/2015.
 */
public class Presentation extends RealmObject implements IEntity {
    @PrimaryKey
    private int id;
    private String level;
    private Track track;
    private RealmList<PresentationSpeaker> speakers = new RealmList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public RealmList<PresentationSpeaker> getSpeakers() {
        return speakers;
    }

    public void setSpeakers(RealmList<PresentationSpeaker> speakers) {
        this.speakers = speakers;
    }

    /*public var event: SummitEvent {
        return linkingObjects(SummitEvent.self, forProperty: "presentation").first!
    }*/
}
