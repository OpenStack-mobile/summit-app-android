package org.openstack.android.summit.common.data_access;

import org.openstack.android.summit.common.entities.Feedback;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.NonConfirmedSummitAttendee;

/**
 * Created by Claudio Redi on 12/16/2015.
 */
public interface IMemberDataStore {

    void getLoggedInMemberOrigin(IDataStoreOperationListener<Member> dataStoreOperationListener);

    Member getByIdLocal(int id);

    void getAttendeesForTicketOrderOrigin(String orderNumber, final IDataStoreOperationListener<NonConfirmedSummitAttendee> dataStoreOperationListener);

    void addFeedback(final Member member, Feedback feedback, final IDataStoreOperationListener dataStoreOperationListener);
}
