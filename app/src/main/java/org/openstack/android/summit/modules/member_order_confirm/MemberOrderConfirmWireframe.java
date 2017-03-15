package org.openstack.android.summit.modules.member_order_confirm;

import android.support.v4.app.FragmentManager;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.user_interface.IBaseView;
import org.openstack.android.summit.modules.member_order_confirm.user_interface.MemberOrderConfirmFragment;

/**
 * Created by Claudio Redi on 3/27/2016.
 */
public class MemberOrderConfirmWireframe implements IMemberOrderConfirmWireframe {

    @Override
    public void presentMemberOrderConfirmView(IBaseView context) {
        MemberOrderConfirmFragment memberOrderConfirmFragment = new MemberOrderConfirmFragment();
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.frame_layout_content, memberOrderConfirmFragment, "nav_my_profile_confirm_order")
                .addToBackStack("nav_my_profile_confirm_order")
                .commitAllowingStateLoss();
    }

    @Override
    public void back(IBaseView context) {
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        fragmentManager.popBackStackImmediate();
    }
}
