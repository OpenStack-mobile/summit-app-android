package org.openstack.android.summit.common.entities;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Claudio Redi on 11/4/2015.
 */
public class Summit extends RealmObject implements INamedEntity {

    public Date getScheduleStartDate() {
        return scheduleStartDate;
    }

    public void setScheduleStartDate(Date scheduleStartDate) {
        this.scheduleStartDate = scheduleStartDate;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getSchedulePageUrl() {
        return schedulePageUrl;
    }

    public void setSchedulePageUrl(String schedulePageUrl) {
        this.schedulePageUrl = schedulePageUrl;
    }

    public String getScheduleEventDetailUrl() {
        return scheduleEventDetailUrl;
    }

    public void setScheduleEventDetailUrl(String scheduleEventDetailUrl) {
        this.scheduleEventDetailUrl = scheduleEventDetailUrl;
    }

    @PrimaryKey
    private int id;
    private String name;
    private Date startDate;
    private Date endDate;
    private String timeZone;
    private Date initialDataLoadDate;
    private Date startShowingVenuesDate;
    private Date scheduleStartDate;
    private String pageUrl;
    private String schedulePageUrl;
    private String scheduleEventDetailUrl;
    private boolean isScheduleLoaded;

    public boolean isScheduleLoaded() {
        return isScheduleLoaded;
    }

    public void setScheduleLoaded(boolean scheduleLoaded) {
        isScheduleLoaded = scheduleLoaded;
    }

    private RealmList<SummitType> types             = new RealmList<>();
    private RealmList<TicketType> ticketTypes       = new RealmList<>();
    private RealmList<Venue> venues                 = new RealmList<>();
    private RealmList<VenueRoom> venueRooms         = new RealmList<>();
    private RealmList<SummitEvent> events           = new RealmList<>();
    private RealmList<TrackGroup> trackGroups       = new RealmList<>();
    private RealmList<Track> tracks                 = new RealmList<>();
    private RealmList<EventType> eventTypes         = new RealmList<>();
    private RealmList<Company> sponsors             = new RealmList<>();
    private RealmList<PresentationSpeaker> speakers = new RealmList<>();

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
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public RealmList<SummitType> getTypes() {
        if(types == null) types = new RealmList<>();
        return types;
    }

    public void setTypes(RealmList<SummitType> types) {
        this.types = types;
    }

    public RealmList<TicketType> getTicketTypes() {
        if(ticketTypes == null) ticketTypes = new RealmList<>();
        return ticketTypes;
    }

    public void setTicketTypes(RealmList<TicketType> ticketTypes) {
        this.ticketTypes = ticketTypes;
    }

    public RealmList<Venue> getVenues() {
        if(venues == null) venues = new RealmList<>();
        return venues;
    }

    public void setVenues(RealmList<Venue> venues) {
        this.venues = venues;
    }

    public RealmList<SummitEvent> getEvents() {
        if(events == null) events = new RealmList<>();
        return events;
    }

    public void setEvents(RealmList<SummitEvent> events) {
        this.events = events;
    }

    public RealmList<TrackGroup> getTrackGroups() {
        if(trackGroups == null) trackGroups = new RealmList<>();
        return trackGroups;
    }

    public void setTrackGroups(RealmList<TrackGroup> trackGroups) {
        this.trackGroups = trackGroups;
    }

    public Date getInitialDataLoadDate() {
        return initialDataLoadDate;
    }

    public void setInitialDataLoadDate(Date initialDataLoadDate) {
        this.initialDataLoadDate = initialDataLoadDate;
    }

    public RealmList<EventType> getEventTypes() {
        if(eventTypes == null) eventTypes = new RealmList<>();
        return eventTypes;
    }

    public void setEventTypes(RealmList<EventType> eventTypes) {
        this.eventTypes = eventTypes;
    }

    public Date getStartShowingVenuesDate() {
        return startShowingVenuesDate;
    }

    public void setStartShowingVenuesDate(Date startShowingVenuesDate) {
        this.startShowingVenuesDate = startShowingVenuesDate;
    }

    public RealmList<Track> getTracks() {
        if(tracks == null) tracks = new RealmList<>();
        return tracks;
    }

    public void setTracks(RealmList<Track> tracks) {
        this.tracks = tracks;
    }

    public RealmList<VenueRoom> getVenueRooms() {
        if(venueRooms == null) venueRooms = new RealmList<>();
        return venueRooms;
    }

    public void setVenueRooms(RealmList<VenueRoom> venueRooms) {
        this.venueRooms = venueRooms;
    }

    public RealmList<Company> getSponsors() {
        return sponsors;
    }

    public void setSponsors(RealmList<Company> sponsors) {
        this.sponsors = sponsors;
    }

    public RealmList<PresentationSpeaker> getSpeakers() {
        return speakers;
    }

    public void setSpeakers(RealmList<PresentationSpeaker> speakers) {
        this.speakers = speakers;
    }
}
