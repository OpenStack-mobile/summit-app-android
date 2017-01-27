package org.openstack.android.summit.common.data_access.repositories;

import org.openstack.android.summit.common.data_access.IDataStoreOperationListener;
import org.openstack.android.summit.common.entities.Feedback;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.NonConfirmedSummitAttendee;

/**
 * Created by Claudio Redi on 12/16/2015.
 */
public interface IMemberDataStore extends IGenericDataStore<Member> {

    void getLoggedInMember(IDataStoreOperationListener<Member> dataStoreOperationListener);

    void getAttendeesForTicketOrder(String orderNumber, final IDataStoreOperationListener<NonConfirmedSummitAttendee> dataStoreOperationListener);

    void addFeedback(final Member member, Feedback feedback, final IDataStoreOperationListener dataStoreOperationListener);
}
