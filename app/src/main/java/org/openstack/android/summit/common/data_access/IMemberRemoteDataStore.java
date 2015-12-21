package org.openstack.android.summit.common.data_access;

import org.openstack.android.summit.common.entities.Member;

/**
 * Created by Claudio Redi on 12/16/2015.
 */
public interface IMemberRemoteDataStore {
    void getLoggedInMember();

    IDataStoreOperationListener<Member> getDelegate();

    void setDelegate(IDataStoreOperationListener<Member> delegate);
}
