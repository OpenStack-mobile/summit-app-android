package org.openstack.android.summit.common.entities;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Claudio Redi on 11/4/2015.
 */
public class Member extends RealmObject implements IPerson {

    @PrimaryKey
    private int id;
    private PresentationSpeaker speakerRole;
    private SummitAttendee attendeeRole;
    private String firstName;
    private String lastName;
    private String fullName;
    private String title;
    private String pictureUrl;
    private String bio;
    private String twitter;
    private String irc;
    private String email;
    private RealmList<Feedback> feedback = new RealmList<>();

    public PresentationSpeaker getSpeakerRole() {
        return speakerRole;
    }

    public void setSpeakerRole(PresentationSpeaker speakerRole) {
        this.speakerRole = speakerRole;
    }

    public SummitAttendee getAttendeeRole() {
        return attendeeRole;
    }

    public void setAttendeeRole(SummitAttendee attendeeRole) {
        this.attendeeRole = attendeeRole;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public RealmList<Feedback> getFeedback() {
        return feedback;
    }

    public void setFeedback(RealmList<Feedback> feedback) {
        this.feedback = feedback;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String getFullName() {
        return fullName;
    }

    @Override
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
}
