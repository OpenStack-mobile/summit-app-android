package org.openstack.android.summit.modules.settings.business_logic;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.ISession;

/**
 * Created by sebastian on 9/19/2016.
 */
public class SettingsInteractor implements ISettingsInteractor {

    private ISession session;

    public SettingsInteractor(ISession session) {
        this.session = session;
    }

    @Override
    public void setBlockAllNotifications(boolean block) {
        session.setInt(Constants.SETTING_BLOCK_NOTIFICATIONS_KEY, block ? 1: 0);
    }

    @Override
    public boolean getBlockAllNotifications() {
        return session.getInt(Constants.SETTING_BLOCK_NOTIFICATIONS_KEY) == 1;
    }
}
