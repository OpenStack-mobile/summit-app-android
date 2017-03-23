package org.openstack.android.summit.modules.events;

import org.openstack.android.summit.common.user_interface.IBaseView;

/**
 * Created by Claudio Redi on 11/17/2015.
 */
public interface IEventsWireframe {

    void presentEventsView(IBaseView context);

    void presentEventsView(IBaseView context, int day);

    void showFilterView(IBaseView view);
}
