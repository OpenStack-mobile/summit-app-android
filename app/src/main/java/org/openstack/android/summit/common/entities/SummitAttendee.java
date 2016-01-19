package org.openstack.android.summit.common.entities;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Claudio Redi on 11/4/2015.
 */
public class SummitAttendee extends RealmObject implements IPerson {
    @PrimaryKey
    private int id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String title;
    private String pictureUrl;
    private String bio;
    private String twitter;
    private String irc;
    private String email;
    private Integer memberId;
    private RealmList<TicketType> ticketTypes = new RealmList<>();
    private RealmList<SummitEvent> scheduledEvents = new RealmList<>();
    private RealmList<SummitEvent> bookmarkedEvents = new RealmList<>();
    private RealmList<Feedback> feedback = new RealmList<>();

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

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public RealmList<SummitEvent> getScheduledEvents() {
        return scheduledEvents;
    }

    public void setScheduledEvents(RealmList<SummitEvent> scheduledEvents) {
        this.scheduledEvents = scheduledEvents;
    }

    public RealmList<SummitEvent> getBookmarkedEvents() {
        return bookmarkedEvents;
    }

    public void setBookmarkedEvents(RealmList<SummitEvent> bookmarkedEvents) {
        this.bookmarkedEvents = bookmarkedEvents;
    }

    public RealmList<Feedback> getFeedback() {
        return feedback;
    }

    public void setFeedback(RealmList<Feedback> feedback) {
        this.feedback = feedback;
    }

    public RealmList<TicketType> getTicketTypes() {
        return ticketTypes;
    }

    public void setTicketTypes(RealmList<TicketType> ticketTypes) {
        this.ticketTypes = ticketTypes;
    }
}
