package org.openstack.android.summit.modules.events.business_logic;

import org.openstack.android.summit.common.DTOs.SummitDTO;
import org.openstack.android.summit.common.business_logic.IInteractorOperationListener;

/**
 * Created by Claudio Redi on 11/18/2015.
 */
public interface IEventsInteractor {
    IInteractorOperationListener<SummitDTO> getDelegate();

    void setDelegate(IInteractorOperationListener<SummitDTO> delegate);

    void getActiveSummit();
}
