package org.openstack.android.summit.common.data_access.repositories;

import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.notifications.IPushNotification;
import org.openstack.android.summit.common.entities.notifications.PushNotification;
import java.util.List;

/**
 * Created by sebastian on 8/20/2016.
 */
public interface IPushNotificationDataStore extends IGenericDataStore<PushNotification> {

    long getNotOpenedCountBy(Member member);

    List<IPushNotification> getByFilter(String searchTerm, Member member, int page, int objectsPerPage);
}
