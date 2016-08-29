package org.openstack.android.summit.common.data_access;

import org.joda.time.DateTime;
import org.openstack.android.summit.common.entities.Feedback;
import org.openstack.android.summit.common.entities.SummitEvent;
import java.util.List;

/**
 * Created by Claudio Redi on 12/21/2015.
 */
public interface ISummitEventDataStore {

    List<SummitEvent> getByFilterLocal(DateTime startDate, DateTime endDate, List<Integer> eventTypes, List<Integer> summitTypes, List<Integer> trackGroups, List<Integer> tracks, List<String> tags, List<String> levels, List<Integer> venues);

    SummitEvent getByIdLocal(int id);

    List<String> getPresentationLevelsLocal();

    List<SummitEvent> getBySearchTerm(String searchTerm);

    List<SummitEvent> getSpeakerEvents(int speakerId, DateTime startDate, DateTime endDate);

    void getFeedbackOrigin(int eventId, int page, int objectsPerPage, IDataStoreOperationListener<Feedback> dataStoreOperationListener);

    void getAverageFeedbackOrigin(int eventId, IDataStoreOperationListener<SummitEvent> dataStoreOperationListener);
}
