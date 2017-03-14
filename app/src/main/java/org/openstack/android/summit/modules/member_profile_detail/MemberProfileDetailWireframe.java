package org.openstack.android.summit.modules.member_profile_detail;

import org.openstack.android.summit.common.BaseWireframe;
import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.user_interface.IBaseView;
import org.openstack.android.summit.modules.member_order_confirm.IMemberOrderConfirmWireframe;

/**
 * Created by Claudio Redi on 1/28/2016.
 */
public class MemberProfileDetailWireframe extends BaseWireframe implements IMemberProfileDetailWireframe {

    IMemberOrderConfirmWireframe memberOrderConfirmWireframe;

    public MemberProfileDetailWireframe
    (
        IMemberOrderConfirmWireframe memberOrderConfirmWireframe,
        INavigationParametersStore navigationParametersStore
    ) {
        super(navigationParametersStore);
        this.memberOrderConfirmWireframe = memberOrderConfirmWireframe;
    }

    @Override
    public void showMemberOrderConfirmView(IBaseView view) {
        memberOrderConfirmWireframe.presentMemberOrderConfirmView(view);
    }
}
