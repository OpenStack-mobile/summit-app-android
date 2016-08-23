package org.openstack.android.summit.common.push_notifications;

import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.Summit;

/**
 * Created by Claudio Redi on 2/22/2016.
 */
public interface IPushNotificationsManager {

    void subscribeMember(Member member, Summit summit);

    void subscribeAnonymous(Summit summit);

    void unSubscribe();

}
