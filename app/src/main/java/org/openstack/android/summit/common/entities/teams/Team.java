package org.openstack.android.summit.common.entities.teams;

import org.openstack.android.summit.common.entities.IEntity;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.notifications.PushNotification;
import org.openstack.android.summit.common.entities.notifications.TeamPushNotification;
import java.util.Date;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by smarcet on 1/24/17.
 */

public class Team extends RealmObject implements IEntity {
    @PrimaryKey
    private int id;

    private String name;

    private String description;

    private Member owner;

    private Date created_at;

    private RealmList<TeamMember> members            = new RealmList<>();

    private RealmList<TeamPushNotification> messages = new RealmList<>();

    private RealmList<TeamInvitation> invitations    = new RealmList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Member getOwner() {
        return owner;
    }

    public void setOwner(Member owner) {
        this.owner = owner;
    }

    public Date getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(Date created_at) {
        this.created_at = created_at;
    }

    public void addMessage(TeamPushNotification message){
        this.messages.add(message);
        message.setTeam(this);
    }

    public void addMember(TeamMember member){
        this.members.add(member);
        member.setTeam(this);
    }

    public TeamMember buildMember(TeamInvitation invitation){
        TeamMember teamMember = new TeamMember();
        teamMember.setMember(invitation.getInvitee());
        teamMember.setPermission(invitation.getPermission());
        teamMember.setTeam(this);
        return teamMember;
    }

    public TeamInvitation buildInvitation(Member invitee, Member inviter, ITeamPermission permission){
        TeamInvitation invitation = new TeamInvitation();
        invitation.setTeam(this);
        invitation.setPermission(permission.toString());
        invitation.setInvitee(invitee);
        invitation.setInviter(inviter);
        return invitation;
    }

    public TeamPushNotification buildMessage(String body, Member from){
        TeamPushNotification message  = new TeamPushNotification();
        PushNotification notification = new PushNotification();
        notification.setBody(body);
        message.setNotification(notification);
        message.setFrom(from);
        message.setTeam(this);
        return message;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }
}
