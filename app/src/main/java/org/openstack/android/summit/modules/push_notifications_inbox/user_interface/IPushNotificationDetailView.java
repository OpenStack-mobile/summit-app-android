package org.openstack.android.summit.modules.push_notifications_inbox.user_interface;

import org.openstack.android.summit.common.user_interface.IBaseView;

import java.util.Date;

/**
 * Created by sebastian on 8/22/2016.
 */
public interface IPushNotificationDetailView extends IBaseView {

    void setSubject(String name);

    void setBody(String name);

    void setReceived(Date received);

    void setType(String type);

    void showGo2EventMenuItem(boolean show);

    void close();

    void hideView();
}
