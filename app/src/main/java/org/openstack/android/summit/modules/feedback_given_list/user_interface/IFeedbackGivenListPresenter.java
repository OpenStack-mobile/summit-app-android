package org.openstack.android.summit.modules.feedback_given_list.user_interface;

import org.openstack.android.summit.common.user_interface.IBasePresenter;
import org.openstack.android.summit.common.user_interface.IFeedbackItemView;
import org.openstack.android.summit.common.user_interface.PersonItemView;

/**
 * Created by Claudio Redi on 1/27/2016.
 */
public interface IFeedbackGivenListPresenter extends IBasePresenter<IFeedbackGivenListView> {
    void buildItem(IFeedbackItemView feedbackItemView, int position);
}