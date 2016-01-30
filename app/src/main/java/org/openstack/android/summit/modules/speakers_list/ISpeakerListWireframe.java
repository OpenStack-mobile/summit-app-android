package org.openstack.android.summit.modules.speakers_list;

import android.support.v4.app.FragmentActivity;

import org.openstack.android.summit.common.user_interface.IBaseView;
import org.openstack.android.summit.modules.speakers_list.user_interface.ISpeakerListView;

/**
 * Created by Claudio Redi on 1/13/2016.
 */
public interface ISpeakerListWireframe {
    void presentSpeakersListView(FragmentActivity context);

    void showSpeakerProfile(int speakerId, IBaseView context);
}
