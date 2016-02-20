package org.openstack.android.summit.common.data_access;

import org.openstack.android.summit.common.entities.Feedback;

/**
 * Created by Claudio Redi on 2/19/2016.
 */
public interface ISummitEventRemoteDataStore {
    void getFeedback(int eventId, int page, int objectsPerPage, IDataStoreOperationListener<Feedback> dataStoreOperationListener);
}
