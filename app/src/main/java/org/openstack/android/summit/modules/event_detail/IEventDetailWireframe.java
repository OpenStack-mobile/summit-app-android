package org.openstack.android.summit.modules.event_detail;

import org.openstack.android.summit.common.IBaseWireframe;
import org.openstack.android.summit.common.user_interface.IBaseView;
import org.openstack.android.summit.modules.event_detail.user_interface.IEventDetailView;

/**
 * Created by Claudio Redi on 11/17/2015.
 */
public interface IEventDetailWireframe extends IBaseWireframe {
    void presentEventDetailView(int eventId, IBaseView context);

    void showSpeakerProfile(int speakerId, IBaseView view);

    void showFeedbackEditView(int eventId, IBaseView view);
}
