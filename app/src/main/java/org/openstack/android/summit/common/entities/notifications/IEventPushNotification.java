package org.openstack.android.summit.common.entities.notifications;

import org.openstack.android.summit.common.entities.SummitEvent;

/**
 * Created by smarcet on 1/24/17.
 */

public interface IEventPushNotification extends IPushNotification {

    SummitEvent getEvent();

    void setEvent(SummitEvent event);

}
