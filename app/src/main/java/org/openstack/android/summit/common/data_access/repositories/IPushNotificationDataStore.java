package org.openstack.android.summit.common.data_access.repositories;

import org.openstack.android.summit.common.entities.notifications.PushNotification;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by sebastian on 8/20/2016.
 */
public interface IPushNotificationDataStore extends IGenericDataStore<PushNotification> {

    long getNotOpenedCountBy(Integer memberId);

    List<PushNotification> getByFilter(String searchTerm, Integer memberId, int page, int objectsPerPage);

    Observable<List<PushNotification>> getByFilterRemote(String searchTerm, Integer memberId, int page, int objectsPerPage);
}
