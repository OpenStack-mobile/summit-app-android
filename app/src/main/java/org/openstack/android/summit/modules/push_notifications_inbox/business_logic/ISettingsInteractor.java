package org.openstack.android.summit.modules.push_notifications_inbox.business_logic;

import org.openstack.android.summit.common.business_logic.IBaseInteractor;

public interface ISettingsInteractor extends IBaseInteractor {

    void setBlockAllNotifications(boolean block);

    boolean getBlockAllNotifications();
}