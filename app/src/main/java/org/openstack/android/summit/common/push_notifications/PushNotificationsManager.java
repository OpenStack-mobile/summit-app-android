package org.openstack.android.summit.common.push_notifications;

import com.parse.ParseInstallation;

import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.entities.SummitEvent;

import java.util.ArrayList;

/**
 * Created by Claudio Redi on 2/22/2016.
 */
public class PushNotificationsManager implements IPushNotificationsManager {


    public void subscribeMember(Member member, int summitId) {

        ArrayList<String> channels = new ArrayList<>();
        channels.add(String.format("su_%d", summitId));
        channels.add(String.format("me_%d", member.getId()));

        if(member.getAttendeeRole()!= null) {

            channels.add("attendees");

            for (SummitEvent event:member.getAttendeeRole().getScheduledEvents()){
                channels.add(String.format("evt_%d", event.getId()));
            }
        }
        if (member.getSpeakerRole() != null) {
            channels.add("speakers");
        }

        ParseInstallation.getCurrentInstallation().put("channels", channels);
        ParseInstallation.getCurrentInstallation().saveEventually();
    }

    public void subscribeAnonymous(int summitId) {

        ArrayList<String> channels = new ArrayList<>();
        channels.add(String.format("su_%d", summitId));

        ParseInstallation.getCurrentInstallation().put("channels", channels);
        ParseInstallation.getCurrentInstallation().saveEventually();
    }

    @Override
    public void unSubscribe() {
        ParseInstallation.getCurrentInstallation().remove("channels");
        ParseInstallation.getCurrentInstallation().saveEventually();
    }
}
