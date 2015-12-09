package org.openstack.android.summit.common.DTOs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Claudio Redi on 11/18/2015.
 */
public class MemberProfileDTO extends NamedDTO {
    private String title;
    private String pictureUrl;
    private String bio;
    private String twitter;
    private String irc;
    private String email;
    private String location;
    private Boolean isAttendee;
    private Boolean isSpeaker;
    private List<ScheduleItemDTO> scheduledEvents = new ArrayList<ScheduleItemDTO>();
    private List<ScheduleItemDTO> presentations = new ArrayList<ScheduleItemDTO>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getIrc() {
        return irc;
    }

    public void setIrc(String irc) {
        this.irc = irc;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getIsAttendee() {
        return isAttendee;
    }

    public void setIsAttendee(Boolean isAttendee) {
        this.isAttendee = isAttendee;
    }

    public Boolean getIsSpeaker() {
        return isSpeaker;
    }

    public void setIsSpeaker(Boolean isSpeaker) {
        this.isSpeaker = isSpeaker;
    }

    public List<ScheduleItemDTO> getScheduledEvents() {
        return scheduledEvents;
    }

    public void setScheduledEvents(List<ScheduleItemDTO> scheduledEvents) {
        this.scheduledEvents = scheduledEvents;
    }

    public List<ScheduleItemDTO> getPresentations() {
        return presentations;
    }

    public void setPresentations(List<ScheduleItemDTO> presentations) {
        this.presentations = presentations;
    }
}
