package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.ISession;
import org.openstack.android.summit.modules.settings.ISettingsWireframe;
import org.openstack.android.summit.modules.settings.SettingsWireframe;
import org.openstack.android.summit.modules.settings.business_logic.ISettingsInteractor;
import org.openstack.android.summit.modules.settings.business_logic.SettingsInteractor;
import org.openstack.android.summit.modules.settings.user_interface.ISettingsPresenter;
import org.openstack.android.summit.modules.settings.user_interface.SettingsFragment;
import org.openstack.android.summit.modules.settings.user_interface.SettingsPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sebastian on 9/19/2016.
 */
@Module
public class SettingsModule {
    @Provides
    SettingsFragment providesSettingsFragment() {
        return new SettingsFragment();
    }

    @Provides
    ISettingsWireframe providesSettingsWireframe() {
        return new SettingsWireframe();
    }

    @Provides
    ISettingsInteractor providesSettingsInteractor(ISession session, IDTOAssembler dtoAssembler) {
        return new SettingsInteractor(dtoAssembler);
    }

    @Provides
    ISettingsPresenter providesAboutPresenter(ISettingsInteractor interactor, ISettingsWireframe wireframe) {
        return new SettingsPresenter(interactor, wireframe);
    }
}
