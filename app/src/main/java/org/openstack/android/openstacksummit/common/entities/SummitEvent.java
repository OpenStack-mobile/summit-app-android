package org.openstack.android.openstacksummit.common.entities;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Claudio Redi on 11/4/2015.
 */
public class SummitEvent extends RealmObject implements INamedEntity {
    private int id;
    private String name;
    private Date start;
    private Date end;
    private String eventDescription;
    private Boolean allowFeedback;
    private EventType eventType;
    private RealmList<SummitType> summitTypes;
    private RealmList<Company> sponsors;
    private Presentation presentation;
    private Venue venue;
    private VenueRoom venueRoom;

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

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public Boolean getAllowFeedback() {
        return allowFeedback;
    }

    public void setAllowFeedback(Boolean allowFeedback) {
        this.allowFeedback = allowFeedback;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public RealmList<SummitType> getSummitTypes() {
        return summitTypes;
    }

    public void setSummitTypes(RealmList<SummitType> summitTypes) {
        this.summitTypes = summitTypes;
    }

    public RealmList<Company> getSponsors() {
        return sponsors;
    }

    public void setSponsors(RealmList<Company> sponsors) {
        this.sponsors = sponsors;
    }

    public Presentation getPresentation() {
        return presentation;
    }

    public void setPresentation(Presentation presentation) {
        this.presentation = presentation;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public VenueRoom getVenueRoom() {
        return venueRoom;
    }

    public void setVenueRoom(VenueRoom venueRoom) {
        this.venueRoom = venueRoom;
    }

    /*public var summit: Summit {
        return linkingObjects(Summit.self, forProperty: "events").first!
    }*/
}
