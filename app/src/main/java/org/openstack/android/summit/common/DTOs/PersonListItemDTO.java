package org.openstack.android.summit.common.DTOs;

import org.joda.time.Instant;

/**
 * Created by Claudio Redi on 11/18/2015.
 */
public class PersonListItemDTO extends NamedDTO {
    private String title;
    private String pictureUrl;
    private Boolean isAttendee;
    private Boolean isSpeaker;
    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPictureUrl() {
        return pictureUrl+"?t="+ Instant.now().getMillis();
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
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
}
