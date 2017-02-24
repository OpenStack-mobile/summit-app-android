package org.openstack.android.summit.modules.search;

import android.support.v4.app.FragmentManager;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.BaseWireframe;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.NamedDTO;
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
                .addToBackStack(null)
                .replace(R.id.frame_layout_content, searchFragment)
                .commit();
    }

    @Override
    public void showTrackSchedule(NamedDTO track, IBaseView context) {
        trackScheduleWireframe.presentTrackScheduleView(track, context);
    }

    @Override
    public void showSpeakerProfile(int speakerId, IBaseView context) {
        memberProfileWireframe.presentOtherSpeakerProfileView(speakerId, context);
    }
}
