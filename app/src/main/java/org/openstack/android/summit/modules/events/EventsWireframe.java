package org.openstack.android.summit.modules.events;

import androidx.fragment.app.FragmentManager;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.BaseWireframe;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.user_interface.IBaseView;
import org.openstack.android.summit.modules.general_schedule.user_interface.GeneralScheduleFragment;
import org.openstack.android.summit.modules.general_schedule_filter.IGeneralScheduleFilterWireframe;

/**
 * Created by claudio on 10/30/2015.
 */
public class EventsWireframe
        extends BaseWireframe
        implements IEventsWireframe {

    private IGeneralScheduleFilterWireframe generalScheduleFilterWireframe;

    public EventsWireframe
    (
        IGeneralScheduleFilterWireframe generalScheduleFilterWireframe,
        INavigationParametersStore navigationParametersStore
    )
    {
        super(navigationParametersStore);
        this.generalScheduleFilterWireframe = generalScheduleFilterWireframe;
    }

    @Override
    public void presentEventsView(IBaseView context) {
        try {
            GeneralScheduleFragment eventsFragment   = new GeneralScheduleFragment();
            FragmentManager fragmentManager = context.getSupportFragmentManager();

            // clean backs stack entries ...
            if (fragmentManager.getBackStackEntryCount() > 0) {
                FragmentManager.BackStackEntry first = fragmentManager.getBackStackEntryAt(0);
                fragmentManager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
            navigationParametersStore.put(Constants.NAVIGATION_PARAMETER_DAY, 0);
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
    public void presentEventsView(IBaseView context, int day) {
        try {
            GeneralScheduleFragment eventsFragment   = new GeneralScheduleFragment();
            FragmentManager fragmentManager = context.getSupportFragmentManager();

            // clean backs stack entries ...
            if (fragmentManager.getBackStackEntryCount() > 0) {
                FragmentManager.BackStackEntry first = fragmentManager.getBackStackEntryAt(0);
                fragmentManager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }

            navigationParametersStore.put(Constants.NAVIGATION_PARAMETER_DAY, day);

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
