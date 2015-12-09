package org.openstack.android.summit.common.DTOs;

/**
 * Created by Claudio Redi on 11/18/2015.
 */
public class PersonListItemDTO extends NamedDTO {
    private String title;
    private String pictureUrl;
    private String isAttendee;
    private String isSpeaker;

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

    public String getIsAttendee() {
        return isAttendee;
    }

    public void setIsAttendee(String isAttendee) {
        this.isAttendee = isAttendee;
    }

    public String getIsSpeaker() {
        return isSpeaker;
    }

    public void setIsSpeaker(String isSpeaker) {
        this.isSpeaker = isSpeaker;
    }
}
