package org.openstack.android.summit.common.entities;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by smarcet on 2/3/17.
 */
public class SummitEventWithFile extends RealmObject implements ISummitEventWithFile {

    @PrimaryKey
    private int id;
    private SummitEvent event;
    private String attachment;

    @Override
    public String getAttachment() {
        return attachment;
    }

    @Override
    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    @Override
    public void setEvent(ISummitEvent event) {
        this.event = (SummitEvent)event;
    }

    @Override
    public ISummitEvent getEvent() {
        return event;
    }

    @Override
    public Date getStart() {
        return event.getStart();
    }

    @Override
    public void setStart(Date start) {
        event.setStart(start);
    }

    @Override
    public Date getEnd() {
        return event.getEnd();
    }

    @Override
    public void setEnd(Date end) {
        event.setEnd(end);
    }

    @Override
    public String getDescription() {
        return event.getDescription();
    }

    @Override
    public void setDescription(String description) {
        event.setDescription(description);
    }

    @Override
    public Boolean getAllowFeedback() {
        return event.getAllowFeedback();
    }

    @Override
    public void setAllowFeedback(Boolean allowFeedback) {
        event.setAllowFeedback(allowFeedback);
    }

    @Override
    public EventType getType() {
        return event.getType();
    }

    @Override
    public void setType(EventType type) {
        event.setType(type);
    }

    @Override
    public RealmList<Company> getSponsors() {
        return event.getSponsors();
    }

    @Override
    public void setSponsors(RealmList<Company> sponsors) {
        event.setSponsors(sponsors);
    }

    @Override
    public Venue getVenue() {
        return event.getVenue();
    }

    @Override
    public void setVenue(Venue venue) {
        event.setVenue(venue);
    }

    @Override
    public VenueRoom getVenueRoom() {
        return event.getVenueRoom();
    }

    @Override
    public void setVenueRoom(VenueRoom venueRoom) {
        event.setVenueRoom(venueRoom);
    }

    @Override
    public RealmList<Tag> getTags() {
        return event.getTags();
    }

    @Override
    public void setTags(RealmList<Tag> tags) {
        event.setTags(tags);
    }

    @Override
    public Summit getSummit() {
        return event.getSummit();
    }

    @Override
    public void setSummit(Summit summit) {
        event.setSummit(summit);
    }

    @Override
    public Track getTrack() {
        return event.getTrack();
    }

    @Override
    public void setTrack(Track track) {
        event.setTrack(track);
    }

    @Override
    public String getClassName() {
        return event.getClassName();
    }

    @Override
    public void setClassName(String className) {
        event.setClassName(className);
    }

    @Override
    public String getName() {
        return event.getName();
    }

    @Override
    public void setName(String name) {
        event.setName(name);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
        event.setId(id);
    }
}
