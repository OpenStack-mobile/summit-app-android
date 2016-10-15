package org.openstack.android.summit.modules.about;

import android.support.v4.app.FragmentManager;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.user_interface.IBaseView;
import org.openstack.android.summit.modules.about.user_interface.AboutFragment;

/**
 * Created by Claudio Redi on 4/1/2016.
 */
public class AboutWireframe implements IAboutWireframe {
    @Override
    public void presentAboutView(IBaseView context) {
        AboutFragment aboutFragment = new AboutFragment();
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.frame_layout_content, aboutFragment, "nav_about")
                .addToBackStack("nav_about")
                .commit();
    }
}
