package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.data_access.data_polling.IDataUpdatePoller;
import org.openstack.android.summit.modules.member_order_confirm.IMemberOrderConfirmWireframe;
import org.openstack.android.summit.modules.member_order_confirm.MemberOrderConfirmWireframe;
import org.openstack.android.summit.modules.member_order_confirm.business_logic.IMemberOrderConfirmInteractor;
import org.openstack.android.summit.modules.member_order_confirm.business_logic.MemberOrderConfirmInteractor;
import org.openstack.android.summit.modules.member_order_confirm.user_interface.IMemberOrderConfirmPresenter;
import org.openstack.android.summit.modules.member_order_confirm.user_interface.MemberOrderConfirmFragment;
import org.openstack.android.summit.modules.member_order_confirm.user_interface.MemberOrderConfirmPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Claudio Redi on 3/27/2016.
 */
@Module
public class MemberOrderConfirmModule {
    @Provides
    MemberOrderConfirmFragment providesMemberOrderConfirmFragment() {
        return new MemberOrderConfirmFragment();
    }

    @Provides
    IMemberOrderConfirmWireframe providesMemberOrderConfirmWireframe() {
        return new MemberOrderConfirmWireframe();
    }

    @Provides
    IMemberOrderConfirmInteractor providesMemberProfileDetailInteractor(IDTOAssembler dtoAssembler, IDataUpdatePoller dataUpdatePoller) {
        return new MemberOrderConfirmInteractor(dtoAssembler, dataUpdatePoller);
    }

    @Provides
    IMemberOrderConfirmPresenter providesMemberProfileDetailPresenter(IMemberOrderConfirmInteractor interactor, IMemberOrderConfirmWireframe wireframe) {
        return new MemberOrderConfirmPresenter(interactor, wireframe);
    }
}

