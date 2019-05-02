package org.openstack.android.summit.modules.speakers_list;

import androidx.fragment.app.FragmentManager;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.BaseWireframe;
import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.user_interface.IBaseView;
import org.openstack.android.summit.modules.member_profile.IMemberProfileWireframe;
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
    public void presentSpeakersListView(IBaseView context) {

        SpeakerListFragment speakerListFragment = new SpeakerListFragment();
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
                    .replace(R.id.frame_layout_content, speakerListFragment, "nav_speakers")
                    .addToBackStack("nav_speakers")
                .commitAllowingStateLoss();
    }

    @Override
    public void showSpeakerProfile(int speakerId, IBaseView context) {
        memberProfileWireframe.presentOtherSpeakerProfileView(speakerId, context);
    }
}
