package org.openstack.android.summit.common.push_notifications;

import org.openstack.android.summit.common.entities.Member;

/**
 * Created by Claudio Redi on 2/22/2016.
 */
public interface IPushNotificationsManager {

    void subscribeMember(Member member,  int summitId);

    void subscribeAnonymous(int summitId);

    void unSubscribe();

}
