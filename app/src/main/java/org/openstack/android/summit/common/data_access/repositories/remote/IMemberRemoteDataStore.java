package org.openstack.android.summit.common.data_access.repositories.remote;

import org.openstack.android.summit.common.data_access.IDataStoreOperationListener;
import org.openstack.android.summit.common.entities.Feedback;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.NonConfirmedSummitAttendee;

/**
 * Created by Claudio Redi on 12/16/2015.
 */
public interface IMemberRemoteDataStore extends IBaseRemoteDataStore {

    void getMemberInfo(final IDataStoreOperationListener<Member> dataStoreOperationListener);

    void getAttendeesForTicketOrder(String orderNumber, IDataStoreOperationListener<NonConfirmedSummitAttendee> remoteDataStoreOperationListener);

    void selectAttendeeFromOrderList(String orderNumber, int externalAttendeeId, final IDataStoreOperationListener<NonConfirmedSummitAttendee> dataStoreOperationListener);

    void addFeedback(Feedback feedback, IDataStoreOperationListener<Feedback> dataStoreOperationListener);
}
