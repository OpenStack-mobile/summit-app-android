package org.openstack.android.summit.modules.speakers_list;

import androidx.fragment.app.FragmentActivity;

import org.openstack.android.summit.common.user_interface.IBaseView;

/**
 * Created by Claudio Redi on 1/13/2016.
 */
public interface ISpeakerListWireframe {

    void presentSpeakersListView(IBaseView context);

    void showSpeakerProfile(int speakerId, IBaseView context);
}
