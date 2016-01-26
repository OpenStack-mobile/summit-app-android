package org.openstack.android.summit.modules.search.user_interface;

import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.DTOs.PersonListItemDTO;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.user_interface.IBaseView;

import java.util.List;

/**
 * Created by Claudio Redi on 1/21/2016.
 */
public interface ISearchView extends IBaseView {
    void showEvents(List<ScheduleItemDTO> events);

    void showTracks(List<NamedDTO> tracks);

    void showSpeakers(List<PersonListItemDTO> speakers);

    void setSearchTerm(String searchTerm);

    void showNoResultsForEvents();

    void showNoResultsForTracks();

    void showNoResultsForSpeakers();
}
