package org.openstack.android.summit.modules.settings.business_logic;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.ISession;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.ISummitDataStore;

/**
 * Created by sebastian on 9/19/2016.
 */
public class SettingsInteractor extends BaseInteractor implements ISettingsInteractor {

    private ISession session;

    public SettingsInteractor
    (
        IDTOAssembler dtoAssembler,
        ISummitSelector summitSelector,
        ISummitDataStore summitDataStore,
        ISession session
    )
    {
        super(dtoAssembler, summitSelector, summitDataStore);
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
