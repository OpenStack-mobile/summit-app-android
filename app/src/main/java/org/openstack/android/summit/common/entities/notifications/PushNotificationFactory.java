package org.openstack.android.summit.common.entities.notifications;

import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitEventDataStore;
import org.openstack.android.summit.common.data_access.repositories.ITeamDataStore;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.entities.exceptions.NotFoundEntityException;
import org.openstack.android.summit.common.entities.teams.Team;

import java.util.Date;
import java.util.Map;

/**
 * Created by smarcet on 1/24/17.
 */

public class PushNotificationFactory implements IPushNotificationFactory {

    private ISummitDataStore summitDataStore;

    private ISummitEventDataStore eventDataStore;

    private ITeamDataStore teamDataStore;

    public PushNotificationFactory
    (
        ISummitDataStore summitDataStore,
        ISummitEventDataStore eventDataStore,
        ITeamDataStore teamDataStore
    ){
        this.summitDataStore = summitDataStore;
        this.eventDataStore  = eventDataStore;
        this.teamDataStore   = teamDataStore;
    }

    @Override
   public IPushNotification build(Map<String, String> data) throws NotFoundEntityException {

        IPushNotification pushNotification = null;
        // common properties
        String type                        = data.get("type");
        int    id                          = Integer.parseInt(data.get("id"));
        int    created_at                  = Integer.parseInt(data.get("created_at"));
        String body                        = data.get("body");
        String title                       = data.get("title");

        switch (type){
            case IPushNotificationType.Regular:
            {
                String channel   = data.get("channel");
                int    summitId  = Integer.parseInt(data.get("summit_id"));
                Summit summit    = this.summitDataStore.getById(summitId);

                if(summit == null)
                    throw new NotFoundEntityException();

                switch(channel){
                    case IPushNotificationChannel.Event:{
                        pushNotification  = new EventPushNotification();
                        int eventId       = Integer.parseInt(data.get("event_id"));
                        SummitEvent event = this.eventDataStore.getById(eventId);

                        if(event == null) throw new NotFoundEntityException();

                        ((EventPushNotification)pushNotification).setEvent(event);
                    }
                    break;
                    default:{
                        pushNotification = new PushNotification();
                    }
                    break;
                }

                pushNotification.setSummit(summit);
                pushNotification.setChannel(channel);
            }
            break;
            default:{
                pushNotification = new TeamPushNotification();
                int teamId       = Integer.parseInt(data.get("team_id"));
                Team team        = this.teamDataStore.getById(teamId);
                if(team == null)
                    throw new NotFoundEntityException();

                ((TeamPushNotification)pushNotification).setTeam(team);
            }
            break;
        }

        pushNotification.setId(id);
        pushNotification.setBody(body);
        pushNotification.setType(type);
        pushNotification.setTitle(title);
        pushNotification.setCreatedAt(new Date(created_at * 1000L));

        return pushNotification;
    }
}
