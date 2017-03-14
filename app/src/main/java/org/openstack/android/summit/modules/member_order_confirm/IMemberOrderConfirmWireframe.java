package org.openstack.android.summit.modules.member_order_confirm;

import org.openstack.android.summit.common.user_interface.IBaseView;

/**
 * Created by Claudio Redi on 3/27/2016.
 */
public interface IMemberOrderConfirmWireframe {

    void presentMemberOrderConfirmView(IBaseView context);

    void back(IBaseView context);
}
