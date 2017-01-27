package org.openstack.android.summit.common.entities.teams;

import org.openstack.android.summit.common.entities.IEntity;
import org.openstack.android.summit.common.entities.Member;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by smarcet on 1/24/17.
 */

public class TeamMember extends RealmObject implements IEntity {

    @PrimaryKey
    private int id;

    private Member member;

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    private String permission;

    private Team team;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

}
