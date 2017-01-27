package org.openstack.android.summit.common.entities.notifications;

import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.teams.Team;

/**
 * Created by smarcet on 1/24/17.
 */

public interface ITeamPushNotification extends IPushNotification {

    Member getFrom();

    void setFrom(Member from);

    Team getTeam();

    void setTeam(Team team);
}
