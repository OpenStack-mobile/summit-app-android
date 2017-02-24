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
    private String description;
    private String rsvpLink;
    private boolean rsvpExternal;
    private int    headCount;
    private Track  track;
    private String  class_name;
    private Boolean allowFeedback;
    private double averageRate;
    private EventType type;
    private RealmList<Company> sponsors       = new RealmList<>();
    private RealmList<Tag> tags               = new RealmList<>();
    private Presentation presentation;
    private Venue venue;
    private VenueRoom venueRoom;
    private Summit summit;
    private SummitGroupEvent groupEvent;
    private SummitEventWithFile eventWithFile;

    public String getRsvpLink() {
        return rsvpLink;
    }

    public void setRsvpLink(String rsvpLink) {
        this.rsvpLink = rsvpLink;
    }

    public boolean isRsvpExternal() {
        return rsvpExternal;
    }

    public void setRsvpExternal(boolean rsvpExternal) {
        this.rsvpExternal = rsvpExternal;
    }

    public int getHeadCount() {
        return headCount;
    }

    public void setHeadCount(int headCount) {
        this.headCount = headCount;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getAllowFeedback() {
        return allowFeedback;
    }

    public void setAllowFeedback(Boolean allowFeedback) {
        this.allowFeedback = allowFeedback;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
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

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    @Override
    public String getClassName() {
        return class_name;
    }

    @Override
    public void setClassName(String class_name) {
        this.class_name = class_name;
    }

    public void setTags(RealmList<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public Summit getSummit() {
        return summit;
    }

    @Override
    public void setSummit(Summit summit) {
        this.summit = summit;
    }

    public double getAverageRate() {
        return averageRate;
    }

    public void setAverageRate(double averageRate) {
        this.averageRate = averageRate;
    }

    public void setGroupEvent(SummitGroupEvent groupEvent){
        this.groupEvent = groupEvent;
    }

    public SummitGroupEvent getGroupEvent(){
        return this.groupEvent;
    }

    public void setSummitEventWithFile(SummitEventWithFile eventWithFile){
        this.eventWithFile = eventWithFile;
    }

    public SummitEventWithFile getSummitEventWithFile(){
        return this.eventWithFile;
    }
}
