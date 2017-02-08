package org.openstack.android.summit.modules.inbox;

import android.support.v4.app.FragmentManager;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.user_interface.IBaseView;
import org.openstack.android.summit.modules.inbox.user_interface.InboxFragment;

/**
 * Created by smarcet on 2/7/17.
 */

public class InboxWireframe implements IInboxWireframe {

    @Override
    public void presentInboxView(IBaseView context) {
        try {
            InboxFragment fragment   = new InboxFragment();
            FragmentManager fragmentManager = context.getSupportFragmentManager();

            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout_content, fragment, "nav_inbox")
                    .addToBackStack("nav_inbox")
                    .commitAllowingStateLoss();
        }
        catch (Exception ex){
            Crashlytics.logException(ex);
        }
    }
}
