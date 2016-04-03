package org.openstack.android.summit.modules.about.business_logic;

import org.openstack.android.summit.common.DTOs.SummitDTO;
import org.openstack.android.summit.common.business_logic.IBaseInteractor;

/**
 * Created by Claudio Redi on 4/2/2016.
 */
public interface IAboutInteractor extends IBaseInteractor {
    SummitDTO getActiveSummit();
}
