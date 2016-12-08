package org.openstack.android.summit.common.entities;

import java.util.Date;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sebastian on 8/20/2016.
 */
public class PushNotification extends RealmObject implements IEntity {

    @PrimaryKey
    private int id;
    private String subject;
    private String body;
    private String type;
    private Date received;
    private boolean opened;
    private Summit summit;
    private Member owner;

    public Member getOwner() {
        return owner;
    }

    public void setOwner(Member owner) {
        this.owner = owner;
    }

    private int eventId;

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public Summit getSummit() {
        return summit;
    }

    public void setSummit(Summit summit) {
        this.summit = summit;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getReceived() {
        return received;
    }

    public void setReceived(Date received) {
        this.received = received;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public void markAsRead(){ this.setOpened(true);}
}
