package org.openstack.android.summit.common.push_notifications;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import org.openstack.android.summit.common.Constants;

/**
 * Created by Claudio Redi on 2/22/2016.
 */
public class PushNotificationsManager implements IPushNotificationsManager {

    public static final String MemberChannelSlug    = "android_member_%d";
    public static final String SummitChannelSlug    = "android_summit_%d";
    public static final String TeamChannelSlug      = "android_team_%d";
    public static final String EventChannelSlug     = "android_event_%d";
    public static final String SpeakersChannelSlug  = "android_speakers";
    public static final String AttendeesChannelSlug = "android_attendees";
    public static final String EveryoneChannelSlug  = "android_everyone";

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
            Log.d(Constants.LOG_TAG, "PushNotificationsManager.subscribeMember");
            subscribeEveryone();
            subscribeMember(memberId);
            subscribeSummit(summitId);

            if (attendeeId > 0) {

               subscribeAttendees();
                if (scheduleEventsIds != null) {
                    for (Integer eventId : scheduleEventsIds) {
                        subscribeToEvent(eventId);
                    }
                }
            }

            if (speakerId > 0) {
               subscribeSpeakers();
            }
            isMemberSubscribed = true;
        }
    }

    @Override
    public boolean isMemberSubscribed(){
        synchronized (this.lock) {
            return this.isMemberSubscribed;
        }
    }

    @Override
    public boolean isAnonymousSubscribed(){
        synchronized (this.lock) {
            return this.isAnonymousSubscribed;
        }
    }

    @Override
    public void subscribeMember(int memberId){
        synchronized (this.lock) {
            if (channels.contains(String.format(MemberChannelSlug,  memberId))) return;
            channels.add(String.format(MemberChannelSlug, memberId));
            FirebaseMessaging.getInstance().subscribeToTopic(String.format(MemberChannelSlug, memberId));
        }
    }

    @Override
    public void subscribeSummit(int summitId){
        synchronized (this.lock) {
            if (channels.contains(String.format(SummitChannelSlug,  summitId))) return;
            channels.add(String.format(SummitChannelSlug, summitId));
            FirebaseMessaging.getInstance().subscribeToTopic(String.format(SummitChannelSlug, summitId));
        }
    }

    @Override
    public void subscribeEveryone(){
        synchronized (this.lock) {
            if (channels.contains(EveryoneChannelSlug)) return;
            channels.add(EveryoneChannelSlug);
            FirebaseMessaging.getInstance().subscribeToTopic(EveryoneChannelSlug);
        }
    }

    @Override
    public void subscribeAttendees(){
        synchronized (this.lock) {
            if (channels.contains(AttendeesChannelSlug)) return;
            channels.add(AttendeesChannelSlug);
            FirebaseMessaging.getInstance().subscribeToTopic(AttendeesChannelSlug);
        }
    }

    @Override
    public void subscribeSpeakers(){
        synchronized (this.lock) {
            if (channels.contains(SpeakersChannelSlug)) return;
            channels.add(SpeakersChannelSlug);
            FirebaseMessaging.getInstance().subscribeToTopic(SpeakersChannelSlug);
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

    @Override
    public void subscribeAnonymous(int summitId) {

        synchronized (this.lock) {
            if(isAnonymousSubscribed) return;
            Log.d(Constants.LOG_TAG, "PushNotificationsManager.subscribeAnonymous");
            subscribeEveryone();
            subscribeSummit(summitId);
            isAnonymousSubscribed = true;
        }
    }

    @Override
    public void unSubscribe() {
        Log.d(Constants.LOG_TAG, "PushNotificationsManager.unSubscribe");
        synchronized (this.lock) {
            for (String channel : channels)
                FirebaseMessaging.getInstance().unsubscribeFromTopic(channel);
            channels.clear();
            /*try {
                new Thread(() -> {
                    try {
                        FirebaseInstanceId.getInstance().deleteInstanceId();
                    } catch (IOException ex) {
                        Log.w(Constants.LOG_TAG, ex.getMessage());
                    }
                }).start();
            }
            catch (Exception ex){
                Log.w(Constants.LOG_TAG, ex.getMessage());
            }*/
            isMemberSubscribed = false;
            isAnonymousSubscribed = false;
        }
    }
}
