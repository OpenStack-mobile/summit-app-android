package org.openstack.android.summit.common.entities;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Claudio Redi on 11/4/2015.
 */
public class Member extends RealmObject implements IPerson {

    @PrimaryKey
    private int id;
    private Speaker speakerRole;
    private SummitAttendee attendeeRole;
    private String firstName;
    private String lastName;
    private String fullName;
    private String title;
    private String pictureUrl;
    private String bio;
    private RealmList<SummitEvent> scheduledEvents  = new RealmList<>();

    public RealmList<SummitEvent> getScheduledEvents() {

        if(scheduledEvents == null) scheduledEvents  = new RealmList<>();
        return scheduledEvents;
    }

    public ArrayList<Integer> getScheduleEventIds(){
        ArrayList<Integer> res = new ArrayList<>();
        for(SummitEvent event: getScheduledEvents()){
            res.add(event.getId());
        }
        return res;
    }

    public void setScheduledEvents(RealmList<SummitEvent> scheduledEvents) {
        this.scheduledEvents = scheduledEvents;
    }


    public void setGroupEvents(RealmList<SummitGroupEvent> groupEvents) {
        this.groupEvents = groupEvents;
    }

    public RealmList<SummitEvent> getFavoriteEvents() {
        return favoriteEvents;
    }

    public void setFavoriteEvents(RealmList<SummitEvent> favoriteEvents) {
        favoriteEvents = favoriteEvents;
    }

    private String twitter;
    private String irc;
    private String email;
    private RealmList<Feedback> feedback = new RealmList<>();
    private RealmList<SummitGroupEvent> groupEvents = new RealmList<>();
    private RealmList<SummitEvent> favoriteEvents = new RealmList<>();


    public Speaker getSpeakerRole() {
        return speakerRole;
    }

    public void setSpeakerRole(Speaker speakerRole) {
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
        if(feedback == null) feedback = new RealmList<>();
        return feedback;
    }

    public void clearFeedback(){
        for(Feedback f: feedback){
            f.setOwner(null);
        }
        feedback.clear();
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

    public void clearGroupEvents(){
        this.groupEvents.clear();
    }

    public RealmList<SummitGroupEvent> getGroupEvents(){
        return this.groupEvents;
    }

    public void addGroupEvent(SummitGroupEvent groupEvent){
        this.groupEvents.add(groupEvent);
        groupEvent.setOwner(this);
    }
}
