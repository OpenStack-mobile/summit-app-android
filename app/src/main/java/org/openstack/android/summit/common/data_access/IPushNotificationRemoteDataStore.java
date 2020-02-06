package org.openstack.android.summit.common.data_access;

import org.openstack.android.summit.common.entities.notifications.PushNotification;
import java.util.List;
import io.reactivex.Observable;

public interface IPushNotificationRemoteDataStore {

    /**
     *
     * @param searchTerm
     * @param page
     * @param objectsPerPage
     * @return
     */
    Observable<List<PushNotification>> get(String searchTerm, int page, int objectsPerPage);
}
