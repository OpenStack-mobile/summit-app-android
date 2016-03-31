package org.openstack.android.summit.common.data_access;

import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.NonConfirmedSummitAttendee;

/**
 * Created by Claudio Redi on 12/16/2015.
 */
public interface IMemberRemoteDataStore {
    void getLoggedInMember(IDataStoreOperationListener<Member> dataStoreOperationListener);

    void getLoggedInMemberBasicInfo(final IDataStoreOperationListener<Member> dataStoreOperationListener);

    void getAttendeesForTicketOrder(String orderNumber, IDataStoreOperationListener<NonConfirmedSummitAttendee> remoteDataStoreOperationListener);

    void selectAttendeeFromOrderList(String orderNumber, int externalAttendeeId, final IDataStoreOperationListener<NonConfirmedSummitAttendee> dataStoreOperationListener);
}
