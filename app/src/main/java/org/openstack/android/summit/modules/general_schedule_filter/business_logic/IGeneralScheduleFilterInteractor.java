package org.openstack.android.summit.modules.general_schedule_filter.business_logic;

import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.DTOs.SummitDTO;
import org.openstack.android.summit.common.DTOs.TrackGroupDTO;
import org.openstack.android.summit.common.business_logic.IBaseInteractor;

import java.util.List;

/**
 * Created by Claudio Redi on 2/2/2016.
 */
public interface IGeneralScheduleFilterInteractor extends IBaseInteractor {

    List<NamedDTO> getSummitTypes();

    List<NamedDTO> getEventTypes();

    List<String> getLevels();

    List<TrackGroupDTO> getTrackGroups();

    List<String> getTags();

    SummitDTO getActiveSummit();

    List<NamedDTO> getVenues();

}
