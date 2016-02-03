package org.openstack.android.summit.modules.events;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;

import org.openstack.android.summit.common.user_interface.IBaseView;
import org.openstack.android.summit.modules.events.user_interface.EventsFragment;

/**
 * Created by Claudio Redi on 11/17/2015.
 */
public interface IEventsWireframe {
    void presentEventsView(IBaseView context);

    void showFilterView(IBaseView view);
}
