package org.openstack.android.summit.common.data_access;

import org.openstack.android.summit.common.entities.Feedback;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.entities.SummitEventAverageFeedback;

/**
 * Created by Claudio Redi on 2/19/2016.
 */
public interface ISummitEventRemoteDataStore {
    void getFeedback(int eventId, int page, int objectsPerPage, IDataStoreOperationListener<Feedback> dataStoreOperationListener);

    void getAverageFeedback(int eventId, final IDataStoreOperationListener<SummitEventAverageFeedback> dataStoreOperationListener);
}
