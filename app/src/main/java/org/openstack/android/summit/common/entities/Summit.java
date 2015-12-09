package org.openstack.android.summit.common.entities;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Claudio Redi on 11/4/2015.
 */
public class Summit extends RealmObject implements INamedEntity {
    @PrimaryKey
    private int id;
    private String name;
    private Date startDate;
    private Date endDate;
    private String timeZone;
    private Date initialDataLoadDate;
    private RealmList<SummitType> types = new RealmList<>();
    private RealmList<TicketType> ticketTypes = new RealmList<>();
    private RealmList<Venue> venues = new RealmList<>();
    private RealmList<SummitEvent> events = new RealmList<>();
    private RealmList<Track> tracks = new RealmList<>();
    private RealmList<EventType> eventTypes = new RealmList<>();

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
        return types;
    }

    public void setTypes(RealmList<SummitType> types) {
        this.types = types;
    }

    public RealmList<TicketType> getTicketTypes() {
        return ticketTypes;
    }

    public void setTicketTypes(RealmList<TicketType> ticketTypes) {
        this.ticketTypes = ticketTypes;
    }

    public RealmList<Venue> getVenues() {
        return venues;
    }

    public void setVenues(RealmList<Venue> venues) {
        this.venues = venues;
    }

    public RealmList<SummitEvent> getEvents() {
        return events;
    }

    public void setEvents(RealmList<SummitEvent> events) {
        this.events = events;
    }

    public RealmList<Track> getTracks() {
        return tracks;
    }

    public void setTracks(RealmList<Track> tracks) {
        this.tracks = tracks;
    }

    public Date getInitialDataLoadDate() {
        return initialDataLoadDate;
    }

    public void setInitialDataLoadDate(Date initialDataLoadDate) {
        this.initialDataLoadDate = initialDataLoadDate;
    }

    public RealmList<EventType> getEventTypes() {
        return eventTypes;
    }

    public void setEventTypes(RealmList<EventType> eventTypes) {
        this.eventTypes = eventTypes;
    }
}
