package org.openstack.android.summit.common.data_access;

import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.Summit;

/**
 * Created by Claudio Redi on 12/16/2015.
 */
public class MemberDataStore implements IDataStoreOperationListener<Member>,IMemberDataStore {
    private IMemberRemoteDataStore memberRemoteDataStore;
    private IGenericDataStore genericDataStore;
    private IDataStoreOperationListener<Member> delegate;

    public MemberDataStore(IMemberRemoteDataStore memberRemoteDataStore, IGenericDataStore genericDataStore) {
        this.memberRemoteDataStore = memberRemoteDataStore;
        this.genericDataStore = genericDataStore;
        this.memberRemoteDataStore.setDelegate(this);
    }

    @Override
    public void getLoggedInMemberOrigin() {
        memberRemoteDataStore.getLoggedInMember();
    }

    public Member getByIdLocal(int id) {
        return genericDataStore.getByIdLocal(id, Member.class);
    }

    @Override
    public void onSuceedWithData(Member data) {
        delegate.onSuceedWithData(data);
    }

    @Override
    public void onSucceed() {

    }

    @Override
    public void onError(String message) {
        delegate.onError(message);
    }

    @Override
    public IDataStoreOperationListener<Member> getDelegate() {
        return delegate;
    }

    @Override
    public void setDelegate(IDataStoreOperationListener<Member> delegate) {
        this.delegate = delegate;
    }
}
