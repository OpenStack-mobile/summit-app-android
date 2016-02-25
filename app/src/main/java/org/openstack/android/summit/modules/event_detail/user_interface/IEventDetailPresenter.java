package org.openstack.android.summit.modules.event_detail.user_interface;

import org.openstack.android.summit.common.user_interface.FeedbackItemView;
import org.openstack.android.summit.common.user_interface.IBasePresenter;
import org.openstack.android.summit.common.user_interface.PersonItemView;

/**
 * Created by Claudio Redi on 1/21/2016.
 */
public interface IEventDetailPresenter extends IBasePresenter<IEventDetailView> {
    void buildSpeakerListItem(PersonItemView personItemView, int position);

    void toggleScheduleStatus();

    void showSpeakerProfile(int position);

    void showFeedbackEdit();

    void buildFeedbackListItem(FeedbackItemView feedbackItemView, int position);

    void loadFeedback();

    void showVenueDetail();
}
