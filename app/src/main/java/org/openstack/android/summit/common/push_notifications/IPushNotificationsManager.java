package org.openstack.android.summit.common.push_notifications;

import java.util.ArrayList;

/**
 * Created by Claudio Redi on 2/22/2016.
 */
public interface IPushNotificationsManager {

    void subscribeMember(int memberId, int summitId, int speakerId, int attendeeId, ArrayList<Integer> scheduleEventsIds);

    void subscribeAnonymous(int summitId);

    void unSubscribe();

}
