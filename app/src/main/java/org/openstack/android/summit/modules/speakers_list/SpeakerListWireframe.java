package org.openstack.android.summit.modules.speakers_list;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.BaseWireframe;
import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.user_interface.IBaseView;
import org.openstack.android.summit.modules.member_profile.IMemberProfileWireframe;
import org.openstack.android.summit.modules.speakers_list.user_interface.ISpeakerListView;
import org.openstack.android.summit.modules.speakers_list.user_interface.SpeakerListFragment;

/**
 * Created by Claudio Redi on 1/13/2016.
 */
public class SpeakerListWireframe extends BaseWireframe implements ISpeakerListWireframe {
    private IMemberProfileWireframe memberProfileWireframe;

    public SpeakerListWireframe(IMemberProfileWireframe memberProfileWireframe, INavigationParametersStore navigationParametersStore) {
        super(navigationParametersStore);
        this.memberProfileWireframe = memberProfileWireframe;
    }

    @Override
    public void presentSpeakersListView(FragmentActivity context) {
        SpeakerListFragment speakerListFragment = new SpeakerListFragment();
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.frame_layout_content, speakerListFragment)
                .commit();
    }

    @Override
    public void showSpeakerProfile(int speakerId, IBaseView context) {
        memberProfileWireframe.presentOtherSpeakerProfileView(speakerId, context);
    }
}
