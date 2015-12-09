package org.openstack.android.summit.common.DTOs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Claudio Redi on 11/18/2015.
 */
public class EventDetailDTO extends ScheduleItemDTO {
    private int venueId;
    private int venueRoomId;
    private String eventDescription;
    private String tags;
    private List<PresentationSpeakerDTO> speakers = new ArrayList<PresentationSpeakerDTO>();
    private Boolean finished;
    private Boolean allowFeedback;

    public int getVenueId() {
        return venueId;
    }

    public void setVenueId(int venueId) {
        this.venueId = venueId;
    }

    public int getVenueRoomId() {
        return venueRoomId;
    }

    public void setVenueRoomId(int venueRoomId) {
        this.venueRoomId = venueRoomId;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public List<PresentationSpeakerDTO> getSpeakers() {
        return speakers;
    }

    public void setSpeakers(List<PresentationSpeakerDTO> speakers) {
        this.speakers = speakers;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public Boolean getAllowFeedback() {
        return allowFeedback;
    }

    public void setAllowFeedback(Boolean allowFeedback) {
        this.allowFeedback = allowFeedback;
    }
}
