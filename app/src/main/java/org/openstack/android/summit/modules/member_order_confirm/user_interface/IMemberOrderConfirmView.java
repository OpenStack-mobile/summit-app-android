package org.openstack.android.summit.modules.member_order_confirm.user_interface;

import org.openstack.android.summit.common.DTOs.NonConfirmedSummitAttendeeDTO;
import org.openstack.android.summit.common.entities.NonConfirmedSummitAttendee;
import org.openstack.android.summit.common.user_interface.IBaseView;

import java.util.List;

/**
 * Created by Claudio Redi on 3/27/2016.
 */
public interface IMemberOrderConfirmView extends IBaseView {
    void setAttendees(List<NonConfirmedSummitAttendeeDTO> nonConfirmedSummitAttendeeList);
}
