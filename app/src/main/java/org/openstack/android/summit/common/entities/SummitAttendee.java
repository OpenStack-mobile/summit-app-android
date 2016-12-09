package org.openstack.android.summit.common.entities;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Claudio Redi on 11/4/2015.
 */
public class SummitAttendee extends RealmObject implements IEntity, ISummitOwned {
    @PrimaryKey
    private int id;
    private Integer memberId;
    private RealmList<TicketType> ticketTypes       = new RealmList<>();
    private RealmList<SummitEvent> scheduledEvents  = new RealmList<>();
    private RealmList<SummitEvent> bookmarkedEvents = new RealmList<>();
    private Summit summit;
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

        if(scheduledEvents == null) scheduledEvents  = new RealmList<>();
        return scheduledEvents;
    }

    public ArrayList<Integer> getScheduleEventIds(){
        ArrayList<Integer> res = new ArrayList<>();
        for(SummitEvent event: getScheduledEvents()){
            res.add(event.getId());
        }
        return res;
    }

    public void setScheduledEvents(RealmList<SummitEvent> scheduledEvents) {
        this.scheduledEvents = scheduledEvents;
    }

    public RealmList<SummitEvent> getBookmarkedEvents() {
        if(bookmarkedEvents == null) bookmarkedEvents = new RealmList<>();
        return bookmarkedEvents;
    }

    public void setBookmarkedEvents(RealmList<SummitEvent> bookmarkedEvents) {
        this.bookmarkedEvents = bookmarkedEvents;
    }

    public RealmList<TicketType> getTicketTypes() {
        if(ticketTypes == null) ticketTypes = new RealmList<>();
        return ticketTypes;
    }

    public void setTicketTypes(RealmList<TicketType> ticketTypes) {
        this.ticketTypes = ticketTypes;
    }

    @Override
    public Summit getSummit() {
        return summit;
    }

    @Override
    public void setSummit(Summit summit) {
        this.summit = summit;
    }
}
