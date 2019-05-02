package org.openstack.android.summit.modules.search;

import androidx.fragment.app.FragmentManager;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.ScheduleWireframe;
import org.openstack.android.summit.common.user_interface.IBaseView;
import org.openstack.android.summit.modules.event_detail.IEventDetailWireframe;
import org.openstack.android.summit.modules.member_profile.IMemberProfileWireframe;
import org.openstack.android.summit.modules.rsvp.IRSVPWireframe;
import org.openstack.android.summit.modules.search.user_interface.SearchFragment;
import org.openstack.android.summit.modules.track_schedule.ITrackScheduleWireframe;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 1/14/2016.
 */
public class SearchWireframe extends ScheduleWireframe implements ISearchWireframe {

    private ITrackScheduleWireframe trackScheduleWireframe;
    private IMemberProfileWireframe memberProfileWireframe;

    @Inject
    public SearchWireframe
    (
        IMemberProfileWireframe memberProfileWireframe,
        ITrackScheduleWireframe trackScheduleWireframe,
        IEventDetailWireframe eventDetailWireframe,
        IRSVPWireframe rsvpWireframe,
        INavigationParametersStore navigationParametersStore
    )
    {
        super(eventDetailWireframe, rsvpWireframe, navigationParametersStore);
        this.trackScheduleWireframe = trackScheduleWireframe;
        this.memberProfileWireframe = memberProfileWireframe;
    }

    @Override
    public void presentSearchView(String searchTerm, IBaseView context) {
        navigationParametersStore.put(Constants.NAVIGATION_PARAMETER_SEARCH_TERM, searchTerm);
        SearchFragment searchFragment = new SearchFragment();
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
                .replace(R.id.frame_layout_content, searchFragment)
                .commitAllowingStateLoss();
    }

    @Override
    public void showTrackSchedule(int trackId, IBaseView context) {
        trackScheduleWireframe.presentTrackScheduleView(trackId, context);
    }

    @Override
    public void showSpeakerProfile(int speakerId, IBaseView context) {
        memberProfileWireframe.presentOtherSpeakerProfileView(speakerId, context);
    }
}
