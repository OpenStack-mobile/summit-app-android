package org.openstack.android.summit.modules.event_detail.user_interface;

import org.openstack.android.summit.common.DTOs.FeedbackDTO;
import org.openstack.android.summit.common.DTOs.PersonListItemDTO;
import org.openstack.android.summit.common.DTOs.VideoDTO;
import org.openstack.android.summit.common.user_interface.IBaseView;
import java.util.List;

/**
 * Created by Claudio Redi on 1/22/2016.
 */
public interface IEventDetailView extends IBaseView {

    void setName(String name);

    void setTrack(String track);

    void setDescription(String description);

    void setDate(String date);

    void setTime(String time);

    void setLocation(String location);

    void setLevel(String level);

    void setSponsors(String sponsors);

    void setSpeakers(List<PersonListItemDTO> speakers);

    void setTags(String tags);

    void setAverageRate(double rate);

    void setMyFeedbackRate(int rate);

    void setMyFeedbackReview(String review);

    void setMyFeedbackDate(String date);

    void hasMyFeedback(boolean hasMyFeedback);

    void showFeedbackActivityIndicator();

    void hideFeedbackActivityIndicator();

    void setOtherPeopleFeedback(List<FeedbackDTO> feedback);

    void toggleLoadMore(boolean show);

    void showFeedbackErrorMessage(String message);

    void showFeedbackErrorMessage();

    void showFeedbackContainer();

    void loadVideo(VideoDTO video);

    void showGoingButton(boolean show);

    void showFavoriteButton(boolean show);

    void showRateButton(boolean show);

    void setFavoriteButtonState(boolean pressed);

    void setGoingButtonState(boolean pressed);

    void showAddFavoriteMenuAction(boolean show);

    void showRemoveFavoriteMenuAction(boolean show);

    void showGoingMenuAction(boolean show);

    void showNotGoingMenuAction(boolean show);

    void showRSVPMenuAction(boolean show);

    void showUnRSVOMenuAction(boolean show);

    void showRateMenuAction(boolean show);

    void setReviewCount(int reviewCount);

    void showAttachment(boolean show, boolean isPresentation);

    void showToRecord(boolean show);

    void setTrackColor(String color);

    void resetGoingButtonState();

    void resetFavoriteButtonState();

    void setGoingButtonText(String text);
}
