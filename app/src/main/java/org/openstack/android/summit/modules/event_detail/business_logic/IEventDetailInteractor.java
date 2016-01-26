package org.openstack.android.summit.modules.event_detail.business_logic;

import org.openstack.android.summit.common.DTOs.EventDetailDTO;
import org.openstack.android.summit.common.IBaseWireframe;
import org.openstack.android.summit.common.business_logic.IScheduleableInteractor;

/**
 * Created by Claudio Redi on 1/21/2016.
 */
public interface IEventDetailInteractor extends IScheduleableInteractor {
    EventDetailDTO getEventDetail(int eventId);
}
