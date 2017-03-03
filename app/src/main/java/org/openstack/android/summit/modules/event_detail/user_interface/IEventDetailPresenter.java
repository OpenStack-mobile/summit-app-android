package org.openstack.android.summit.modules.event_detail.user_interface;

import android.content.Intent;

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

    void showFeedbackEdit(int rate);

    void buildFeedbackListItem(FeedbackItemView feedbackItemView, int position);

    void loadFeedback();

    void showVenueDetail();

    Intent createShareIntent();

    void updateUI();

    void showEventsByLevel();

    void openAttachment();

    boolean shouldShowContextMenu();

    void updateContextMenuOptions();

    void toggleFavoriteStatus();

    void toggleRSVPStatus();
}
