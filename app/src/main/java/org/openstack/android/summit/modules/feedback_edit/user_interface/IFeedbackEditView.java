package org.openstack.android.summit.modules.feedback_edit.user_interface;

import org.openstack.android.summit.common.user_interface.IBaseView;

/**
 * Created by Claudio Redi on 2/17/2016.
 */
public interface IFeedbackEditView extends IBaseView {
    int getRate();

    void setRate(int rate);

    String getReview();

    void setEventName(String eventName);
}
