package org.openstack.android.summit.modules.member_order_confirm.business_logic;

import org.openstack.android.summit.common.DTOs.NonConfirmedSummitAttendeeDTO;
import org.openstack.android.summit.common.business_logic.IBaseInteractor;
import org.openstack.android.summit.common.business_logic.IInteractorAsyncOperationListener;
import org.openstack.android.summit.common.entities.NonConfirmedSummitAttendee;

import java.util.List;

/**
 * Created by Claudio Redi on 3/27/2016.
 */
public interface IMemberOrderConfirmInteractor extends IBaseInteractor {
    void getAttendeesForTicketOrder(String orderNumber, IInteractorAsyncOperationListener<List<NonConfirmedSummitAttendeeDTO>> interactorAsyncOperationListener);

    void selectAttendeeFromOrderList(String orderNumber, int externalAttendeeId, IInteractorAsyncOperationListener<Void> interactorAsyncOperationListener);

    void linkAttendee();
}
