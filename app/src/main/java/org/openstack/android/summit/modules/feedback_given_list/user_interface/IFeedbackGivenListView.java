package org.openstack.android.summit.modules.feedback_given_list.user_interface;

import org.openstack.android.summit.common.DTOs.FeedbackDTO;
import org.openstack.android.summit.common.user_interface.IBaseView;

import java.util.List;

/**
 * Created by Claudio Redi on 1/27/2016.
 */
public interface IFeedbackGivenListView extends IBaseView {
    void setFeedbackList(List<FeedbackDTO> feedbackList);
}
