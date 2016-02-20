package org.openstack.android.summit.modules.feedback_edit.user_interface;

import org.openstack.android.summit.common.user_interface.IBaseView;

/**
 * Created by Claudio Redi on 2/17/2016.
 */
public interface IFeedbackEditView extends IBaseView {
    int getRate();
    String getReview();
}
