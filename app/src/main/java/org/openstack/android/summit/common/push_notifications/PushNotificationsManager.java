package org.openstack.android.summit.common.push_notifications;

import com.parse.ParseInstallation;

import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.Summit;

import java.util.ArrayList;

/**
 * Created by Claudio Redi on 2/22/2016.
 */
public class PushNotificationsManager implements IPushNotificationsManager {

    public void subscribeMember(Member member, Summit summit) {
        if (summit == null) {
            return;
        }
        ArrayList<String> channels = new ArrayList<>();
        channels.add(String.format("su_%d", summit.getId()));
        channels.add(String.format("me_%d", member.getId()));
        channels.add("attendees");
        if (member.getSpeakerRole() != null) {
            channels.add("speakers");
        }

        ParseInstallation.getCurrentInstallation().put("channels", channels);
        ParseInstallation.getCurrentInstallation().saveEventually();
    }

    public void subscribeAnonymous(Summit summit) {
        if (summit == null) {
            return;
        }
        ArrayList<String> channels = new ArrayList<>();
        channels.add(String.format("su_%d", summit.getId()));
        ParseInstallation.getCurrentInstallation().put("channels", channels);
        ParseInstallation.getCurrentInstallation().saveEventually();
    }
}
