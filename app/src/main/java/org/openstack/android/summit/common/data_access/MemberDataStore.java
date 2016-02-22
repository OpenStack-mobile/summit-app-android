package org.openstack.android.summit.common.data_access;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.data_access.deserialization.DataStoreOperationListener;
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
    public void getLoggedInMemberOrigin(final IDataStoreOperationListener<Member> dataStoreOperationListener) {

        IDataStoreOperationListener<Member> remoteDataStoreOperationListener = new DataStoreOperationListener<Member>() {
            @Override
            public void onSuceedWithSingleData(Member data) {
                try{
                    realm.beginTransaction();
                    Member realmEntity = realm.copyToRealmOrUpdate(data);
                    realm.commitTransaction();
                    dataStoreOperationListener.onSuceedWithSingleData(realmEntity);
                }
                catch (Exception e) {
                    Crashlytics.logException(e);
                    Log.e(Constants.LOG_TAG, e.getMessage(), e);
                    dataStoreOperationListener.onError(e.getMessage());
                }
            }

            @Override
            public void onError(String message) {
                dataStoreOperationListener.onError(message);
            }
        };
        memberRemoteDataStore.getLoggedInMember(remoteDataStoreOperationListener);
    }

    public Member getByIdLocal(int id) {
        return getByIdLocal(id, Member.class);
    }
}