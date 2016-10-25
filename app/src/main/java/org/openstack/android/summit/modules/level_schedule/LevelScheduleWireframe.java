package org.openstack.android.summit.modules.level_schedule;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.ScheduleWireframe;
import org.openstack.android.summit.common.user_interface.IBaseView;
import org.openstack.android.summit.modules.event_detail.IEventDetailWireframe;
import org.openstack.android.summit.modules.general_schedule_filter.IGeneralScheduleFilterWireframe;
import org.openstack.android.summit.modules.level_schedule.user_interface.LevelScheduleFragment;

/**
 * Created by Claudio Redi on 1/11/2016.
 */
public class LevelScheduleWireframe extends ScheduleWireframe implements ILevelScheduleWireframe {
    private IGeneralScheduleFilterWireframe generalScheduleFilterWireframe;

    public LevelScheduleWireframe(IEventDetailWireframe eventDetailWireframe, IGeneralScheduleFilterWireframe generalScheduleFilterWireframe, INavigationParametersStore navigationParametersStore) {
        super(eventDetailWireframe, navigationParametersStore);
        this.generalScheduleFilterWireframe = generalScheduleFilterWireframe;
    }

    @Override
    public void presentLevelScheduleView(String level, FragmentActivity context) {
        try {
            LevelScheduleFragment levelScheduleFragment = new LevelScheduleFragment();
            navigationParametersStore.put(Constants.NAVIGATION_PARAMETER_LEVEL, level);
            FragmentManager fragmentManager = context.getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout_content, levelScheduleFragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        }
        catch(Exception ex){
            Crashlytics.logException(ex);
        }
    }

    @Override
    public void showFilterView(IBaseView view) {
        generalScheduleFilterWireframe.presentGeneralScheduleFilterView(view);
    }
}
