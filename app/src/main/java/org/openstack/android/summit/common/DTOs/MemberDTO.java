package org.openstack.android.summit.common.DTOs;

/**
 * Created by Claudio Redi on 11/18/2015.
 */
public class MemberDTO {
    private int id;
    private PersonDTO speakerRole;
    private PersonDTO attendeeRole;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
}
