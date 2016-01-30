package org.openstack.android.summit.modules.event_detail;

import android.support.v4.app.FragmentManager;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.BaseWireframe;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.user_interface.BaseFragment;
import org.openstack.android.summit.common.user_interface.IBaseView;
import org.openstack.android.summit.modules.event_detail.user_interface.EventDetailFragment;
import org.openstack.android.summit.modules.event_detail.user_interface.IEventDetailFragment;
import org.openstack.android.summit.modules.member_profile.IMemberProfileWireframe;

import javax.inject.Inject;

/**
 * Created by claudio on 11/2/2015.
 */
public class EventDetailWireframe extends BaseWireframe implements IEventDetailWireframe {
    private IMemberProfileWireframe memberProfileWireframe;

    @Inject
    public EventDetailWireframe(IMemberProfileWireframe memberProfileWireframe,INavigationParametersStore navigationParametersStore) {
        super(navigationParametersStore);
        this.memberProfileWireframe = memberProfileWireframe;
    }

    @Override
    public void presentEventDetailView(int eventId, IBaseView context) {
        navigationParametersStore.put(Constants.NAVIGATION_PARAMETER_EVENT_ID, eventId);
        EventDetailFragment eventDetailFragment = new EventDetailFragment();
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.frame_layout_content, eventDetailFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void showSpeakerProfile(int speakerId, IBaseView context) {
        memberProfileWireframe.presentOtherSpeakerProfileView(speakerId, context);
    }
}
