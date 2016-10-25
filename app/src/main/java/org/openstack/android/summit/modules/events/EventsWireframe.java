package org.openstack.android.summit.modules.events;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.user_interface.IBaseView;
import org.openstack.android.summit.modules.events.user_interface.EventsFragment;
import org.openstack.android.summit.modules.general_schedule_filter.IGeneralScheduleFilterWireframe;

/**
 * Created by claudio on 10/30/2015.
 */
public class EventsWireframe implements IEventsWireframe {

    private IGeneralScheduleFilterWireframe generalScheduleFilterWireframe;

    public EventsWireframe(IGeneralScheduleFilterWireframe generalScheduleFilterWireframe) {
        this.generalScheduleFilterWireframe = generalScheduleFilterWireframe;
    }

    @Override
    public void presentEventsView(IBaseView context) {
        try {
            EventsFragment eventsFragment   = new EventsFragment();
            FragmentManager fragmentManager = context.getSupportFragmentManager();

            // clean backs stack entries ...
            if (fragmentManager.getBackStackEntryCount() > 0) {
                FragmentManager.BackStackEntry first = fragmentManager.getBackStackEntryAt(0);
                fragmentManager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }

            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout_content, eventsFragment)
                    .commitAllowingStateLoss();
        }
        catch (Exception ex){
            Crashlytics.logException(ex);
        }
    }

    @Override
    public void showFilterView(IBaseView view) {
        generalScheduleFilterWireframe.presentGeneralScheduleFilterView(view);
    }
}
