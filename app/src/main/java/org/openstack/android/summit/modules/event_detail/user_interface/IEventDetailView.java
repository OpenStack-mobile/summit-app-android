package org.openstack.android.summit.modules.event_detail.user_interface;

import org.openstack.android.summit.common.DTOs.FeedbackDTO;
import org.openstack.android.summit.common.DTOs.PersonListItemDTO;
import org.openstack.android.summit.common.DTOs.VideoDTO;
import org.openstack.android.summit.common.user_interface.IBaseView;
import org.openstack.android.summit.common.user_interface.IScheduleableView;
import java.util.List;

/**
 * Created by Claudio Redi on 1/22/2016.
 */
public interface IEventDetailView extends IBaseView, IScheduleableView {

    void setName(String name);

    void setTrack(String track);

    void setDescription(String description);

    void setDate(String date);

    void setLocation(String location);

    void setCredentials(String credentials);

    void setLevel(String level);

    void setSponsors(String sponsors);

    void setSpeakers(List<PersonListItemDTO> speakers);

    void setScheduled(Boolean scheduled);

    void setTags(String tags);

    Boolean getScheduled();

    void setIsScheduledStatusVisible(Boolean isScheduledStatusVisible);

    void setAllowNewFeedback(boolean allowNewFeedback);

    void setAllowRsvp(boolean allowRsvp);

    void setAverageRate(int rate);

    void setMyFeedbackRate(int rate);

    void setMyFeedbackReview(String review);

    void setMyFeedbackDate(String date);

    void setMyFeedbackOwner(String owner);

    void hasMyFeedback(boolean hasMyFeedback);

    void showFeedbackActivityIndicator();

    void hideFeedbackActivityIndicator();

    void setOtherPeopleFeedback(List<FeedbackDTO> feedback);

    void toggleLoadMore(boolean show);

    void showFeedbackErrorMessage(String message);

    void showFeedbackContainer();

    void loadVideo(VideoDTO video);
}
