package org.openstack.android.summit.common.DTOs;

/**
 * Created by Claudio Redi on 11/18/2015.
 */
public class MemberDTO extends PersonDTO {

    private PersonDTO speakerRole;
    private PersonDTO attendeeRole;

    public PersonDTO getSpeakerRole() {
        return speakerRole;
    }

    public void setSpeakerRole(PersonDTO speakerRole) {
        this.speakerRole = speakerRole;
    }

    public PersonDTO getAttendeeRole() {
        return attendeeRole;
    }

    public void setAttendeeRole(PersonDTO attendeeRole) {
        this.attendeeRole = attendeeRole;
    }

    public String getPictureUrl() {
        if(speakerRole!=null) return speakerRole.getPictureUrl();
        return super.getPictureUrl();
    }
}
