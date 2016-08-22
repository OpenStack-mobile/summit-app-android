package org.openstack.android.summit.modules.push_notifications_inbox.user_interface;

import java.util.Date;

/**
 * Created by sebastian on 8/20/2016.
 */
public interface IPushNotificationItemView {

    void setSubject(String subject);

    void setBody(String body);

    void setOpened(boolean isOpened);

    void setReceivedDate(Date receivedDate);
}
