package org.openstack.android.summit.common.entities;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Claudio Redi on 11/4/2015.
 */
public class SummitAttendee extends RealmObject implements IEntity {
    @PrimaryKey
    private int id;
    private Integer memberId;
    private RealmList<TicketType> ticketTypes       = new RealmList<>();
    private RealmList<SummitEvent> scheduledEvents  = new RealmList<>();
    private RealmList<SummitEvent> bookmarkedEvents = new RealmList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public RealmList<SummitEvent> getScheduledEvents() {
        return scheduledEvents;
    }

    public void setScheduledEvents(RealmList<SummitEvent> scheduledEvents) {
        this.scheduledEvents = scheduledEvents;
    }

    public RealmList<SummitEvent> getBookmarkedEvents() {
        return bookmarkedEvents;
    }

    public void setBookmarkedEvents(RealmList<SummitEvent> bookmarkedEvents) {
        this.bookmarkedEvents = bookmarkedEvents;
    }

    public RealmList<TicketType> getTicketTypes() {
        return ticketTypes;
    }

    public void setTicketTypes(RealmList<TicketType> ticketTypes) {
        this.ticketTypes = ticketTypes;
    }
}
