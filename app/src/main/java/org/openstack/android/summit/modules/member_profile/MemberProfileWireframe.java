package org.openstack.android.summit.modules.member_profile;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.BaseWireframe;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.user_interface.IBaseView;
import org.openstack.android.summit.modules.events.IEventsWireframe;
import org.openstack.android.summit.modules.member_profile.user_interface.MemberProfileFragment;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 1/26/2016.
 */
public class MemberProfileWireframe extends BaseWireframe implements IMemberProfileWireframe {
    IEventsWireframe eventsWireframe;

    @Inject
    public MemberProfileWireframe(INavigationParametersStore navigationParametersStore, IEventsWireframe eventsWireframe) {
        super(navigationParametersStore);
        this.eventsWireframe = eventsWireframe;
    }

    @Override
    public void presentOtherSpeakerProfileView(int speakerId, IBaseView context) {
        navigationParametersStore.put(Constants.NAVIGATION_PARAMETER_IS_MY_PROFILE, false);
        navigationParametersStore.put(Constants.NAVIGATION_PARAMETER_SPEAKER, speakerId);
        presentMemberProfileView(context);
    }

    @Override
    public void presentMyProfileView(IBaseView context) {
        navigationParametersStore.put(Constants.NAVIGATION_PARAMETER_IS_MY_PROFILE, true);
        MemberProfileFragment memberProfileFragment = new MemberProfileFragment();
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                    .replace(R.id.frame_layout_content, memberProfileFragment)
                    .addToBackStack(null)
                .commit();
    }

    private void presentMemberProfileView(IBaseView context) {
        MemberProfileFragment memberProfileFragment = new MemberProfileFragment();
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                    .replace(R.id.frame_layout_content, memberProfileFragment)
                    .addToBackStack(null)
                .commit();
    }

    public void showEventsView(IBaseView context) {
        eventsWireframe.presentEventsView(context);
    }
}