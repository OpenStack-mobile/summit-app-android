
package org.openstack.android.summit.common.DTOs;

/**
 * Created by Claudio Redi on 11/18/2015.
 */
public class ScheduleItemDTO extends NamedDTO {
    private String time;
    private String dateTime;
    private String location;
    private String track;
    private String credentials;
    private String sponsors;
    private String eventType;
    private String color;

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
}
