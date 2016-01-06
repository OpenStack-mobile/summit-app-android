package org.openstack.android.summit.common.data_access;

import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.Summit;

/**
 * Created by Claudio Redi on 12/16/2015.
 */
public class MemberDataStore extends GenericDataStore implements IMemberDataStore {
    private IMemberRemoteDataStore memberRemoteDataStore;

    public MemberDataStore(IMemberRemoteDataStore memberRemoteDataStore) {
        this.memberRemoteDataStore = memberRemoteDataStore;
    }

    @Override
    public void getLoggedInMemberOrigin(IDataStoreOperationListener<Member> dataStoreOperationListener) {

        final IDataStoreOperationListener<Member> finalDataStoreOperationListener = dataStoreOperationListener;
        IDataStoreOperationListener<Member> remoteDataStoreOperationListener = new IDataStoreOperationListener<Member>() {
            @Override
            public void onSuceedWithData(Member data) {
                try{
                    realm.beginTransaction();
                    Member realmEntity = realm.copyToRealmOrUpdate(data);
                    realm.commitTransaction();
                    finalDataStoreOperationListener.onSuceedWithData(realmEntity);
                }
                catch (Exception ex) {
                    finalDataStoreOperationListener.onError(ex.getMessage());
                }
            }

            @Override
            public void onSucceed() {

            }

            @Override
            public void onError(String message) {
                finalDataStoreOperationListener.onError(message);
            }
        };
        memberRemoteDataStore.getLoggedInMember(remoteDataStoreOperationListener);
    }

    public Member getByIdLocal(int id) {
        return getByIdLocal(id, Member.class);
    }
}