package org.openstack.android.summit.common.business_logic;

import org.openstack.android.summit.common.DTOs.SummitDTO;

/**
 * Created by Claudio Redi on 11/18/2015.
 */
public interface IScheduleInteractor {
    IInteractorOperationListener<SummitDTO> getDelegate();

    void setDelegate(IInteractorOperationListener<SummitDTO> delegate);

    void getActiveSummit();
}
