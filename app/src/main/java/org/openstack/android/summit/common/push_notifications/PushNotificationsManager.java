package org.openstack.android.summit.common.push_notifications;

import com.parse.ParseInstallation;
import java.util.ArrayList;

/**
 * Created by Claudio Redi on 2/22/2016.
 */
public class PushNotificationsManager implements IPushNotificationsManager {


    public void subscribeMember(int memberId, int summitId, int speakerId, int attendeeId, ArrayList<Integer> scheduleEventsIds) {

        ArrayList<String> channels = new ArrayList<>();
        channels.add(String.format("su_%d", summitId));
        channels.add(String.format("me_%d", memberId));

        if(attendeeId > 0) {

            channels.add("attendees");
            if(scheduleEventsIds != null){
                for (Integer eventId : scheduleEventsIds){
                    channels.add(String.format("evt_%d", eventId));
                }
            }
        }

        if (speakerId > 0) {
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
