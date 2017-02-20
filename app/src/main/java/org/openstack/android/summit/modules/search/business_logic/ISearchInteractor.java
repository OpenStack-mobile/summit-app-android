package org.openstack.android.summit.modules.search.business_logic;

import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.DTOs.PersonDTO;
import org.openstack.android.summit.common.DTOs.PersonListItemDTO;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.business_logic.IScheduleableInteractor;

import java.util.List;

/**
 * Created by Claudio Redi on 1/14/2016.
 */
public interface ISearchInteractor extends IScheduleableInteractor {

    List<ScheduleItemDTO> getEventsBySearchTerm(String searchTerm);

    List<NamedDTO> getTracksBySearchTerm(String searchTerm);

    List<PersonListItemDTO> getSpeakersBySearchTerm(String searchTerm);
}
