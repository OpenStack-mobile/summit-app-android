package org.openstack.android.summit.common;

import org.openstack.android.summit.common.user_interface.IBaseView;

/**
 * Created by Claudio Redi on 12/29/2015.
 */
public interface IScheduleWireframe extends IBaseWireframe {

    void showEventDetail(int eventId, IBaseView context);

    void presentEventRsvpView(String rsvpLink, IBaseView context);
}
