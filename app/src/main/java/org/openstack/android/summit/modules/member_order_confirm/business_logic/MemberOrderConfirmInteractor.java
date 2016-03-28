package org.openstack.android.summit.modules.member_order_confirm.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.data_polling.IDataUpdatePoller;

/**
 * Created by Claudio Redi on 3/27/2016.
 */
public class MemberOrderConfirmInteractor extends BaseInteractor implements IMemberOrderConfirmInteractor {
    public MemberOrderConfirmInteractor(IDTOAssembler dtoAssembler, IDataUpdatePoller dataUpdatePoller) {
        super(dtoAssembler, dataUpdatePoller);
    }
}
