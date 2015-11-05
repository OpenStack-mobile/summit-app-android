package org.openstack.android.openstacksummit.common.entities;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Claudio Redi on 11/4/2015.
 */
public class PresentationSpeaker extends RealmObject implements IEntity {
    private int id;
    private String firstName;
    private String lastName;
    private String title;
    private String pictureUrl;
    private String bio;
    private String twitter;
    private String irc;
    private String email;
    private int memberId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    // RealmList<Presentation> presentations;

    /*public var presentations: [Presentation] {
        return linkingObjects(Presentation.self, forProperty: "speakers")
    }*/
}
