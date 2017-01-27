package org.openstack.android.summit.common.entities.notifications;

import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.entities.teams.Team;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by smarcet on 1/24/17.
 */

public class TeamPushNotification extends RealmObject implements ITeamPushNotification {

    @PrimaryKey
    private int id;

    private Member from;

    private Team team;

    private PushNotification notification;

    public Member getFrom() {
        return from;
    }

    public void setFrom(Member from) {
        this.from = from;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public PushNotification getNotification() {
        return notification;
    }

    public void setNotification(PushNotification notification) {
        this.notification = notification;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
        this.notification.setId(id);
    }

    @Override
    public String getTitle() {
        return notification.getTitle();
    }

    @Override
    public void setTitle(String title) {
        notification.setTitle(title);
    }

    @Override
    public String getChannel() {
        return notification.getChannel();
    }

    @Override
    public void setChannel(String channel) {
        notification.setChannel(channel);
    }

    @Override
    public Date getCreatedAt() {
        return notification.getCreatedAt();
    }

    @Override
    public void setCreatedAt(Date created_at) {
        notification.setCreatedAt(created_at);
    }

    @Override
    public Member getOwner() {
        return notification.getOwner();
    }

    @Override
    public void setOwner(Member owner) {
        notification.setOwner(owner);
    }

    @Override
    public Summit getSummit() {
        return notification.getSummit();
    }

    @Override
    public void setSummit(Summit summit) {
        notification.setSummit(summit);
    }

    @Override
    public String getBody() {
        return notification.getBody();
    }

    @Override
    public void setBody(String body) {
        notification.setBody(body);
    }

    @Override
    public void setType(String type) {
        notification.setType(type);
    }

    @Override
    public String getType() {
        return notification.getType();
    }

    @Override
    public boolean isOpened() {
        return notification.isOpened();
    }

    @Override
    public void markAsRead() {
        notification.markAsRead();
    }

}
