package org.openstack.android.summit.common.entities;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Claudio Redi on 11/4/2015.
 */
public class Feedback extends RealmObject implements IEntity {
    @PrimaryKey
    private int id;

    private int rate;
    private String review;
    private Date date;
    private SummitEvent event;
    private Member owner;

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public SummitEvent getEvent() {
        return event;
    }

    public void setEvent(SummitEvent event) {
        this.event = event;
    }

    public Member getOwner() {
        return owner;
    }

    public void setOwner(Member owner) {
        this.owner = owner;
    }
}
