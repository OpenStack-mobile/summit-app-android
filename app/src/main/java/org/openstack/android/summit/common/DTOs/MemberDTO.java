package org.openstack.android.summit.common.DTOs;

/**
 * Created by Claudio Redi on 11/18/2015.
 */
public class MemberDTO {
    private int id;
    private PresentationSpeakerDTO speakerRole;
    private SummitAttendeeDTO attendeeRole;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PresentationSpeakerDTO getSpeakerRole() {
        return speakerRole;
    }

    public void setSpeakerRole(PresentationSpeakerDTO speakerRole) {
        this.speakerRole = speakerRole;
    }

    public SummitAttendeeDTO getAttendeeRole() {
        return attendeeRole;
    }

    public void setAttendeeRole(SummitAttendeeDTO attendeeRole) {
        this.attendeeRole = attendeeRole;
    }
}
