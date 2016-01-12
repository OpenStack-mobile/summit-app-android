package org.openstack.android.summit.common.data_access;

import org.openstack.android.summit.common.entities.SummitEvent;

import java.util.Date;
import java.util.List;

/**
 * Created by Claudio Redi on 12/21/2015.
 */
public interface ISummitEventDataStore {
    List<SummitEvent> getByFilterLocal(Date startDate, Date endDate, List<Integer> eventTypes, List<Integer> summitTypes, List<Integer> tracks, List<String> tags, List<String> levels);

    SummitEvent getByIdLocal(int id);

    List<String> getPresentationLevelsLocal();
}
