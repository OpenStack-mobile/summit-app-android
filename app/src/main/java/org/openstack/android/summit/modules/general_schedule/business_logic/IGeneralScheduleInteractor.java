package org.openstack.android.summit.modules.general_schedule.business_logic;

import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.DTOs.SummitDTO;
import org.openstack.android.summit.common.business_logic.IInteractorAsyncOperationListener;
import org.openstack.android.summit.common.business_logic.IScheduleInteractor;

import java.util.Date;
import java.util.List;

/**
 * Created by Claudio Redi on 12/21/2015.
 */
public interface IGeneralScheduleInteractor extends IScheduleInteractor {
    boolean isDataLoaded();

    boolean isNetworkingAvailable();

}
