package org.openstack.android.summit.common.DTOs;

import org.joda.time.Instant;

import java.util.Date;

/**
 * Created by Claudio Redi on 11/18/2015.
 */
public class FeedbackDTO {

    private int id = 0;
    private int rate = 0;
    private String review;
    private Date date;
    private String timeAgo;
    private String owner;
    private String ownerPicUrl;

    private String eventName;
    private int eventId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }

    public String getOwner() {
        return owner;
    }

    public String getOwnerPicUrl(){
        return this.ownerPicUrl+"?t="+ Instant.now().getMillis();
    }

    public void setOwnerPicUrl(String ownerPicUrl){
        this.ownerPicUrl = ownerPicUrl;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
