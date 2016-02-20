package org.openstack.android.summit.common.data_access;

import org.joda.time.DateTime;
import org.openstack.android.summit.common.entities.Feedback;
import org.openstack.android.summit.common.entities.PresentationSpeaker;
import org.openstack.android.summit.common.entities.SummitEvent;

import java.util.Date;
import java.util.List;

/**
 * Created by Claudio Redi on 12/21/2015.
 */
public interface ISummitEventDataStore {
    List<SummitEvent> getByFilterLocal(Date startDate, Date endDate, List<Integer> eventTypes, List<Integer> summitTypes, List<Integer> trackGroups, List<Integer> tracks, List<String> tags, List<String> levels);

    SummitEvent getByIdLocal(int id);

    List<String> getPresentationLevelsLocal();

    List<SummitEvent> getBySearchTerm(String searchTerm);

    List<SummitEvent> getSpeakerEvents(int speakerId, Date startDate, Date endDate);

    void getFeedbackOrigin(int eventId, int page, int objectsPerPage, IDataStoreOperationListener<Feedback> dataStoreOperationListener);
}
