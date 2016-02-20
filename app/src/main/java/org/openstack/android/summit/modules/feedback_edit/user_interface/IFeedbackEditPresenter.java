package org.openstack.android.summit.modules.feedback_edit.user_interface;

import org.openstack.android.summit.common.user_interface.IBasePresenter;

/**
 * Created by Claudio Redi on 2/17/2016.
 */
public interface IFeedbackEditPresenter extends IBasePresenter<IFeedbackEditView> {
    void saveFeedback();
}
