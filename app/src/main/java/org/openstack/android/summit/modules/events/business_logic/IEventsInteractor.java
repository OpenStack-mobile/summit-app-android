package org.openstack.android.summit.modules.events.business_logic;

import org.openstack.android.summit.common.DTOs.SummitDTO;
import org.openstack.android.summit.common.business_logic.IBaseInteractor;

/**
 * Created by Claudio Redi on 2/10/2016.
 */
public interface IEventsInteractor extends IBaseInteractor {

    boolean isDataLoaded();

    boolean isNetworkingAvailable();

    SummitDTO getLocalActiveSummit();
}
