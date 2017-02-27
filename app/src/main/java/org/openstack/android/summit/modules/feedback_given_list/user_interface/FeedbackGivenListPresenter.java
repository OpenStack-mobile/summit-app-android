package org.openstack.android.summit.modules.feedback_given_list.user_interface;

import android.os.Bundle;
import org.openstack.android.summit.common.DTOs.FeedbackDTO;
import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.common.user_interface.IFeedbackItemView;
import org.openstack.android.summit.modules.feedback_given_list.business_logic.IFeedbackGivenListInteractor;
import java.util.List;

/**
 * Created by Claudio Redi on 1/27/2016.
 */
public class FeedbackGivenListPresenter
        extends BasePresenter<IFeedbackGivenListView, IFeedbackGivenListInteractor, Void>
        implements IFeedbackGivenListPresenter {

    List<FeedbackDTO> feedbackList;

    public FeedbackGivenListPresenter(IFeedbackGivenListInteractor interactor) {
        super(interactor, null);
    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        feedbackList = interactor.getFeedbackGivenByCurrentUser();
        view.setFeedbackList(feedbackList);
    }

    @Override
    public void buildItem(IFeedbackItemView feedbackItemView, int position) {
        FeedbackDTO feedback = feedbackList.get(position);
        feedbackItemView.setDate(feedback.getTimeAgo());
        feedbackItemView.setOwner(feedback.getOwner());
        feedbackItemView.setRate(feedback.getRate());
        feedbackItemView.setReview(feedback.getReview());
    }
}
