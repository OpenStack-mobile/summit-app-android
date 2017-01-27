package org.openstack.android.summit.common.entities.teams;

import org.openstack.android.summit.common.entities.IEntity;
import org.openstack.android.summit.common.entities.Member;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by smarcet on 1/24/17.
 */

public class TeamInvitation extends RealmObject implements IEntity {
    @PrimaryKey
    private int id;

    public TeamInvitation(){
        this.created_at = new Date();
    }

    private Team team;

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Member getInvitee() {
        return invitee;
    }

    public void setInvitee(Member invitee) {
        this.invitee = invitee;
    }

    public Member getInviter() {
        return inviter;
    }

    public void setInviter(Member inviter) {
        this.inviter = inviter;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Date getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(Date created_at) {
        this.created_at = created_at;
    }

    public Date getAcceptedAt() {
        return accepted_at;
    }

    public void setAcceptedAt(Date accepted_at) {
        this.accepted_at = accepted_at;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    private Member invitee;

    private Member inviter;

    private String permission;

    private Date created_at;

    private Date accepted_at;

    private boolean accepted;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public void accept(){
        this.accepted    = true;
        this.accepted_at = new Date();
    }
}
