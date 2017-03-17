package org.openstack.android.summit.modules.feedback_edit;

import org.openstack.android.summit.common.IBaseWireframe;
import org.openstack.android.summit.common.user_interface.IBaseView;

/**
 * Created by Claudio Redi on 2/17/2016.
 */
public interface IFeedbackEditWireframe extends IBaseWireframe {

    void presentFeedbackEditView(int eventId, String eventName, int rate, IBaseView context);

    void backToPreviousView(IBaseView context);
}
