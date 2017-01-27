package org.openstack.android.summit.common.push_notifications;

import java.util.ArrayList;
import com.google.firebase.messaging.FirebaseMessaging;

import org.modelmapper.internal.objenesis.ObjenesisException;

/**
 * Created by Claudio Redi on 2/22/2016.
 */
public class PushNotificationsManager implements IPushNotificationsManager {

    public static final String MemberChannelSlug    = "member_%d";
    public static final String SummitChannelSlug    = "summit_%d";
    public static final String TeamChannelSlug      = "team_%d";
    public static final String EventChannelSlug     = "event_%d";
    public static final String SpeakersChannelSlug  = "speakers";
    public static final String AttendeesChannelSlug = "attendees";
    public static final String EveryoneChannelSlug  = "everyone";
    private boolean isMemberSubscribed              = false;
    private boolean isAnonymousSubscribed           = false;
    private ArrayList<String> channels              = new ArrayList<>();
    private Object lock                             = new Object();

    public void subscribeMember
    (
        int memberId,
        int summitId,
        int speakerId,
        int attendeeId,
        ArrayList<Integer> scheduleEventsIds
    ) {
        synchronized (this.lock) {
            if (isMemberSubscribed) return;
            if (!channels.isEmpty()) unSubscribe();
            channels.add(EveryoneChannelSlug);
            channels.add(String.format(SummitChannelSlug, summitId));
            channels.add(String.format(MemberChannelSlug, memberId));

            if (attendeeId > 0) {

                channels.add(AttendeesChannelSlug);
                if (scheduleEventsIds != null) {
                    for (Integer eventId : scheduleEventsIds) {
                        channels.add(String.format(EventChannelSlug, eventId));
                    }
                }
            }

            if (speakerId > 0) {
                channels.add(SpeakersChannelSlug);
            }

            for (String channel : channels)
                FirebaseMessaging.getInstance().subscribeToTopic(channel);
            isMemberSubscribed = true;
        }
    }

    public void subscribeToTeam(int teamId){
        synchronized (this.lock) {
            if (channels.contains(String.format(TeamChannelSlug, teamId))) return;
            channels.add(String.format(TeamChannelSlug, teamId));
            FirebaseMessaging.getInstance().subscribeToTopic(String.format(TeamChannelSlug, teamId));
        }
    }

    @Override
    public void unsubscribeFromTeam(int teamId) {
        synchronized (this.lock) {
            if (!channels.contains(String.format(TeamChannelSlug, teamId))) return;
            channels.remove(String.format(TeamChannelSlug, teamId));
            FirebaseMessaging.getInstance().unsubscribeFromTopic(String.format(TeamChannelSlug, teamId));
        }
    }

    @Override
    public void subscribeToEvent(int eventId) {
        synchronized (this.lock) {
            if (channels.contains(String.format(EventChannelSlug, eventId))) return;
            channels.add(String.format(EventChannelSlug, eventId));
            FirebaseMessaging.getInstance().subscribeToTopic(String.format(EventChannelSlug, eventId));
        }
    }

    @Override
    public void unsubscribeFromEvent(int eventId) {
        synchronized (this.lock) {
            if (!channels.contains(String.format(EventChannelSlug, eventId))) return;
            channels.remove(String.format(EventChannelSlug, eventId));
            FirebaseMessaging.getInstance().unsubscribeFromTopic(String.format(EventChannelSlug, eventId));
        }
    }

    public void subscribeAnonymous(int summitId) {
        synchronized (this.lock) {
            if(isAnonymousSubscribed) return;
            if(!channels.isEmpty()) unSubscribe();
            channels.add(EveryoneChannelSlug);
            channels.add(String.format(SummitChannelSlug, summitId));

            for(String channel:channels)
                FirebaseMessaging.getInstance().subscribeToTopic(channel);

            isAnonymousSubscribed = true;
        }
    }

    @Override
    public void unSubscribe() {
        synchronized (this.lock) {
            for (String channel : channels)
                FirebaseMessaging.getInstance().unsubscribeFromTopic(channel);

            channels.clear();

            isMemberSubscribed = false;
            isAnonymousSubscribed = false;
        }
    }
}
