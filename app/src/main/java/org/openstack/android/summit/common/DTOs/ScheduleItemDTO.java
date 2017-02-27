
package org.openstack.android.summit.common.DTOs;

import org.joda.time.DateTime;
import org.openstack.android.summit.common.user_interface.IScheduleableItem;

/**
 * Created by Claudio Redi on 11/18/2015.
 */
public class ScheduleItemDTO extends NamedDTO implements IScheduleableItem {

    public interface IChangeStatusListener{
        void changed(ScheduleItemDTO item);
    }

    protected String time;
    protected String dateTime;
    protected String location;
    protected String room;
    protected String track;
    protected String credentials;
    protected String sponsors;
    protected String eventType;
    protected String color;
    protected String locationAddress;
    protected int summitId;
    protected boolean rsvpExternal;
    protected String rsvpLink;
    protected boolean isScheduled;
    protected boolean isFavorite;
    protected boolean isPresentation;

    public void setChangeStatusListener(IChangeStatusListener changeStatusListener) {
        this.changeStatusListener = changeStatusListener;
    }

    protected IChangeStatusListener changeStatusListener;

    public boolean isPresentation() {
        return isPresentation;
    }

    public void setPresentation(boolean presentation) {
        isPresentation = presentation;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    private DateTime startDate;

    public DateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

    private DateTime endDate;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String date) {
        this.dateTime = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public String getCredentials() {
        return credentials;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }

    public String getSponsors() {
        return sponsors;
    }

    public void setSponsors(String sponsors) {
        this.sponsors = sponsors;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getColor(){
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setSummitId(int summitId){ this.summitId = summitId;}

    public int getSummitId(){ return this.summitId;}


    @Override
    public Boolean getScheduled() {
        return isScheduled;
    }

    @Override
    public void setScheduled(Boolean scheduled) {
        this.isScheduled = scheduled;
        if(this.changeStatusListener != null)
            this.changeStatusListener.changed(this);
    }

    @Override
    public Boolean getFavorite() {
        return this.isFavorite;
    }

    @Override
    public void setFavorite(Boolean favorite) {
        this.isFavorite = favorite;
        if(this.changeStatusListener != null)
            this.changeStatusListener.changed(this);
    }

    @Override
    public boolean isExternalRSVP() {
        return this.rsvpExternal;
    }

    @Override
    public String getRSVPLink() {
        return this.rsvpLink;
    }

    @Override
    public void setExternalRSVP(boolean externalRSVP) {
        this.rsvpExternal = externalRSVP;
    }

    @Override
    public void setRSVPLink(String link) {
        this.rsvpLink = link;
    }
}