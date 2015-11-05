package org.openstack.android.openstacksummit.common.entities;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Claudio Redi on 11/4/2015.
 */
public class Summit extends RealmObject implements INamedEntity {
    private int id;
    private String name;
    private Date startDate;
    private Date endDate;
    private String timeZone;
    private RealmList<SummitType> types;
    private RealmList<TicketType> ticketTypes;
    private RealmList<Venue> venues;
    private RealmList<SummitEvent> events;
    private RealmList<Track> tracks;

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
}
