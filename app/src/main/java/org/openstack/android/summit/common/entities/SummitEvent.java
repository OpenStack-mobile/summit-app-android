package org.openstack.android.summit.common.entities;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Claudio Redi on 11/4/2015.
 */
public class SummitEvent extends RealmObject implements ISummitEvent {
    @PrimaryKey
    private int    id;
    private String name;
    private Date   start;
    private Date   end;
    private String eventDescription;
    private String rsvpLink;
    private int    headCount;

    public String getRsvpLink() {
        return rsvpLink;
    }

    public void setRsvpLink(String rsvpLink) {
        this.rsvpLink = rsvpLink;
    }

    public int getHeadCount() {
        return headCount;
    }

    public void setHeadCount(int headCount) {
        this.headCount = headCount;
    }

    private Boolean allowFeedback;
    private double averageRate;
    private EventType eventType;
    private RealmList<SummitType> summitTypes = new RealmList<>();
    private RealmList<Company> sponsors       = new RealmList<>();
    private RealmList<Tag> tags               = new RealmList<>();
    private Presentation presentation;
    private Venue venue;
    private VenueRoom venueRoom;
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
        if(summitTypes == null) summitTypes = new RealmList<>();
        return summitTypes;
    }

    public void setSummitTypes(RealmList<SummitType> summitTypes) {
        this.summitTypes = summitTypes;
    }

    public RealmList<Company> getSponsors() {
        if(sponsors == null) sponsors = new RealmList<>();
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

    public RealmList<Tag> getTags() {
        if(tags == null) tags = new RealmList<>();
        return tags;
    }

    public void setTags(RealmList<Tag> tags) {
        this.tags = tags;
    }

    public Summit getSummit() {
        return summit;
    }

    public void setSummit(Summit summit) {
        this.summit = summit;
    }

    public double getAverageRate() {
        return averageRate;
    }

    public void setAverageRate(double averageRate) {
        this.averageRate = averageRate;
    }
}
