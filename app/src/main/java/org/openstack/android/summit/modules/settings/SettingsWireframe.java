package org.openstack.android.summit.modules.settings;

import android.support.v4.app.FragmentManager;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.user_interface.IBaseView;
import org.openstack.android.summit.modules.settings.user_interface.SettingsFragment;

/**
 * Created by sebastian on 9/19/2016.
 */
public class SettingsWireframe implements ISettingsWireframe {

    public SettingsWireframe() {

    }

    @Override
    public void presentSettingsView(IBaseView context) {
        SettingsFragment fragment       = new SettingsFragment();
        FragmentManager fragmentManager = context.getSupportFragmentManager();

        fragmentManager
                .beginTransaction()
                .replace(R.id.frame_layout_content, fragment)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }
}
