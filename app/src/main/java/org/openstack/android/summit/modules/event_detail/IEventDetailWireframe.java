package org.openstack.android.summit.modules.event_detail;

import org.openstack.android.summit.common.IBaseWireframe;
import org.openstack.android.summit.common.user_interface.IBaseView;

/**
 * Created by Claudio Redi on 11/17/2015.
 */
public interface IEventDetailWireframe extends IBaseWireframe {
    void presentEventDetailView(int eventId, IBaseView context);
}
