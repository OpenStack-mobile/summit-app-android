package org.openstack.android.summit.modules.member_order_confirm.user_interface;

import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.modules.member_order_confirm.IMemberOrderConfirmWireframe;
import org.openstack.android.summit.modules.member_order_confirm.business_logic.IMemberOrderConfirmInteractor;

/**
 * Created by Claudio Redi on 3/27/2016.
 */
public class MemberOrderConfirmPresenter extends BasePresenter<IMemberOrderConfirmView, IMemberOrderConfirmInteractor, IMemberOrderConfirmWireframe> implements IMemberOrderConfirmPresenter {
    public MemberOrderConfirmPresenter(IMemberOrderConfirmInteractor interactor, IMemberOrderConfirmWireframe wireframe) {
        super(interactor, wireframe);
    }
}
