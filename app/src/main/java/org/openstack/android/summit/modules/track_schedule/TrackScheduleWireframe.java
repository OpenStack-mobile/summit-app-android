package org.openstack.android.summit.modules.track_schedule;

import androidx.fragment.app.FragmentManager;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.ScheduleWireframe;
import org.openstack.android.summit.common.user_interface.IBaseView;
import org.openstack.android.summit.modules.event_detail.IEventDetailWireframe;
import org.openstack.android.summit.modules.general_schedule_filter.IGeneralScheduleFilterWireframe;
import org.openstack.android.summit.modules.rsvp.IRSVPWireframe;
import org.openstack.android.summit.modules.track_schedule.user_interface.TrackScheduleFragment;

/**
 * Created by Claudio Redi on 1/12/2016.
 */
public class TrackScheduleWireframe
        extends ScheduleWireframe
        implements ITrackScheduleWireframe
{
    private IGeneralScheduleFilterWireframe generalScheduleFilterWireframe;

    public TrackScheduleWireframe
    (
        IEventDetailWireframe eventDetailWireframe,
        IGeneralScheduleFilterWireframe generalScheduleFilterWireframe,
        IRSVPWireframe rsvpWireframe,
        INavigationParametersStore navigationParametersStore
    )
    {
        super(eventDetailWireframe, rsvpWireframe, navigationParametersStore);
        this.generalScheduleFilterWireframe = generalScheduleFilterWireframe;
    }

    @Override
    public void presentTrackScheduleView(int trackId, IBaseView context) {
        TrackScheduleFragment trackScheduleFragment = new TrackScheduleFragment();
        navigationParametersStore.put(Constants.NAVIGATION_PARAMETER_TRACK, trackId);
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.frame_layout_content, trackScheduleFragment)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    @Override
    public void reloadTrackScheduleView(int trackId, IBaseView context) {
        TrackScheduleFragment trackScheduleFragment = new TrackScheduleFragment();
        navigationParametersStore.put(Constants.NAVIGATION_PARAMETER_TRACK, trackId);
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.frame_layout_content, trackScheduleFragment)
                .commitAllowingStateLoss();
    }

    @Override
    public void showFilterView(IBaseView view) {
        generalScheduleFilterWireframe.presentGeneralScheduleFilterView(view);
    }
}