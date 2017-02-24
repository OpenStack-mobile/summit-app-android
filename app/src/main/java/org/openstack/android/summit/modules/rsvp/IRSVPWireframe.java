package org.openstack.android.summit.modules.rsvp;

import org.openstack.android.summit.common.IBaseWireframe;
import org.openstack.android.summit.common.user_interface.IBaseView;

/**
 * Created by smarcet on 2/23/17.
 */

public interface IRSVPWireframe extends IBaseWireframe {

    void presentEventRsvpView(String rsvpLink, IBaseView context);
}
