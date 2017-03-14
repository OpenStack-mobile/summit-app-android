package org.openstack.android.summit.modules.member_order_confirm.business_logic;

import org.openstack.android.summit.common.DTOs.NonConfirmedSummitAttendeeDTO;
import org.openstack.android.summit.common.business_logic.IBaseInteractor;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Claudio Redi on 3/27/2016.
 */
public interface IMemberOrderConfirmInteractor extends IBaseInteractor {

    Observable<List<NonConfirmedSummitAttendeeDTO>> getAttendeesForTicketOrder(String orderNumber) throws Exception;

    Observable<Boolean> selectAttendeeFromOrderList(String orderNumber, int externalAttendeeId) throws Exception;

    void bindCurrentUser();
}
