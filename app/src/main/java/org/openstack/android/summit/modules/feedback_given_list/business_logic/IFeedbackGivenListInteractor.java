package org.openstack.android.summit.modules.feedback_given_list.business_logic;

import org.openstack.android.summit.common.DTOs.FeedbackDTO;

import java.util.List;

/**
 * Created by Claudio Redi on 1/27/2016.
 */
public interface IFeedbackGivenListInteractor {
    List<FeedbackDTO> getFeedbackGivenByCurrentUser();
}
