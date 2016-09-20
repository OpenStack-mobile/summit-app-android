package org.openstack.android.summit.modules.settings.user_interface;

import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.modules.settings.ISettingsWireframe;
import org.openstack.android.summit.modules.settings.business_logic.ISettingsInteractor;

/**
 * Created by sebastian on 9/19/2016.
 */
public class SettingsPresenter extends BasePresenter<ISettingsView, ISettingsInteractor, ISettingsWireframe>
        implements ISettingsPresenter {

    public SettingsPresenter(ISettingsInteractor interactor, ISettingsWireframe wireframe) {
        super(interactor, wireframe);
    }

    @Override
    public void setBlockAllNotifications(boolean block) {
        interactor.setBlockAllNotifications(block);
    }

    @Override
    public boolean getBlockAllNotifications() {
        return interactor.getBlockAllNotifications();
    }
}
