package org.openstack.android.summit.modules.member_profile_detail;

import org.openstack.android.summit.common.IBaseWireframe;
import org.openstack.android.summit.common.user_interface.IBaseView;

/**
 * Created by Claudio Redi on 1/28/2016.
 */
public interface IMemberProfileDetailWireframe extends IBaseWireframe {
    void showMemberOrderConfirmView(IBaseView view);
}
