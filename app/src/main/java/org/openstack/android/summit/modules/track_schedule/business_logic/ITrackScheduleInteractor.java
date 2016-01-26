package org.openstack.android.summit.modules.track_schedule.business_logic;

import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.business_logic.IScheduleInteractor;

/**
 * Created by Claudio Redi on 1/12/2016.
 */
public interface ITrackScheduleInteractor extends IScheduleInteractor {
    NamedDTO getTrack(Integer trackId);
}
