package org.openstack.android.summit.modules.settings.business_logic;

import org.openstack.android.summit.common.business_logic.IBaseInteractor;

/**
 * Created by sebastian on 9/19/2016.
 */
public interface ISettingsInteractor extends IBaseInteractor {
    public void setBlockAllNotifications(boolean block);
    public boolean getBlockAllNotifications();
}
