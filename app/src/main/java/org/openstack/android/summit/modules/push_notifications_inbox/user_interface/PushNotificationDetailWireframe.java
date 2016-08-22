package org.openstack.android.summit.modules.push_notifications_inbox.user_interface;

import org.openstack.android.summit.common.BaseWireframe;
import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.user_interface.IBaseView;
import org.openstack.android.summit.modules.main.IMainWireframe;

/**
 * Created by sebastian on 8/22/2016.
 */
public class PushNotificationDetailWireframe
        extends BaseWireframe
        implements IPushNotificationDetailWireframe {

    private IMainWireframe mainWireframe;

    public PushNotificationDetailWireframe(IMainWireframe mainWireframe, INavigationParametersStore navigationParametersStore) {
        super(navigationParametersStore);
        this.mainWireframe = mainWireframe;
    }

    @Override
    public void showEventDetail(int eventId,  IBaseView context) {
        mainWireframe.showEventDetail(eventId, context);
    }
}
