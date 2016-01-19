package org.openstack.android.summit.modules.speakers_list;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import org.openstack.android.summit.R;
import org.openstack.android.summit.modules.speakers_list.user_interface.SpeakerListFragment;

/**
 * Created by Claudio Redi on 1/13/2016.
 */
public class SpeakerListWireframe implements ISpeakerListWireframe {
    @Override
    public void presentSpeakersListView(FragmentActivity context) {
        SpeakerListFragment speakerListFragment = new SpeakerListFragment();
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.frame_layout_content, speakerListFragment)
                .commit();
    }
}
