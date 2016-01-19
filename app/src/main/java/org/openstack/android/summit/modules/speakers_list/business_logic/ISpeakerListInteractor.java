package org.openstack.android.summit.modules.speakers_list.business_logic;

import org.openstack.android.summit.common.DTOs.PersonListItemDTO;

import java.util.List;

/**
 * Created by Claudio Redi on 1/13/2016.
 */
public interface ISpeakerListInteractor {
    List<PersonListItemDTO> getSpeakers(int page, int objectsPerPage);
}
