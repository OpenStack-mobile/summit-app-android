package org.openstack.android.summit.common.entities;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Claudio Redi on 11/4/2015.
 */
public class Member extends RealmObject implements IEntity {
    @PrimaryKey
    private int id;
    private PresentationSpeaker skeaperRole;
    private SummitAttendee attendeeRole;

    public PresentationSpeaker getSkeaperRole() {
        return skeaperRole;
    }

    public void setSkeaperRole(PresentationSpeaker skeaperRole) {
        this.skeaperRole = skeaperRole;
    }

    public SummitAttendee getAttendeeRole() {
        return attendeeRole;
    }

    public void setAttendeeRole(SummitAttendee attendeeRole) {
        this.attendeeRole = attendeeRole;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }
}
