package org.openstack.android.summit.common.data_access;

import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.PushNotification;
import java.util.List;

/**
 * Created by sebastian on 8/20/2016.
 */
public interface IPushNotificationDataStore extends IGenericDataStore {

    long getNotOpenedCountBy(Member member);

    List<PushNotification> getByFilterLocal(String searchTerm, Member member, int page, int objectsPerPage);
}
