package org.openstack.android.summit.common.data_access;

import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.Summit;

/**
 * Created by Claudio Redi on 12/16/2015.
 */
public class MemberDataStore implements IDataStoreOperationListener<Member>,IMemberDataStore {
    private IMemberRemoteDataStore memberRemoteDataStore;
    private IDataStoreOperationListener<Member> delegate;

    public MemberDataStore(IMemberRemoteDataStore memberRemoteDataStore) {
        this.memberRemoteDataStore = memberRemoteDataStore;
        this.memberRemoteDataStore.setDelegate(this);
    }

    @Override
    public void getLoggedInMemberOrigin() {
        memberRemoteDataStore.getLoggedInMember();
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

    /*public func getLoggedInMemberOrigin(completionBlock : (Member?, NSError?) -> Void)  {
        memberRemoteStorage.getLoggedInMember { member, error in

            if (error != nil) {
                completionBlock(member, error)
                return
            }

            self.saveOrUpdateLocal(member!)  { member, error in
                completionBlock(member, error)
            }
        }
    }*/
}
