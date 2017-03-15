package org.openstack.android.summit.modules.member_profile_detail.user_interface;

import org.openstack.android.summit.common.user_interface.IBasePresenter;

/**
 * Created by Claudio Redi on 1/26/2016.
 */
public interface IMemberProfileDetailPresenter extends IBasePresenter<IMemberProfileDetailView> {

    void onAddEventBriteOrderClicked();

    void willAttendClicked();

    void willNotAttendClicked();
}
