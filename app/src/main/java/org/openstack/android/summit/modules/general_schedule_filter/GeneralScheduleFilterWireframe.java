package org.openstack.android.summit.modules.general_schedule_filter;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.BaseWireframe;
import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.user_interface.IBaseView;
import org.openstack.android.summit.modules.general_schedule_filter.user_interface.GeneralScheduleFilterFragment;
import org.openstack.android.summit.modules.speakers_list.user_interface.SpeakerListFragment;

/**
 * Created by Claudio Redi on 2/2/2016.
 */
public class GeneralScheduleFilterWireframe extends BaseWireframe implements IGeneralScheduleFilterWireframe {
    public GeneralScheduleFilterWireframe(INavigationParametersStore navigationParametersStore) {
        super(navigationParametersStore);
    }

    public void presentGeneralScheduleFilterView(IBaseView context) {
        GeneralScheduleFilterFragment generalScheduleFilterFragment = new GeneralScheduleFilterFragment();
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .setCustomAnimations
                        (
                                R.anim.slide_in_left,
                                R.anim.slide_out_left,
                                R.anim.slide_out_right,
                                R.anim.slide_in_right
                        )
                .addToBackStack(null)
                .replace(R.id.frame_layout_content, generalScheduleFilterFragment)
                .commit();
    }
}
