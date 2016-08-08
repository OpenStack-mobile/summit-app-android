package org.openstack.android.summit.common.data_access;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.data_access.deserialization.DataStoreOperationListener;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.NonConfirmedSummitAttendee;
import org.openstack.android.summit.common.utils.RealmFactory;

import java.util.List;

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
            public void onSucceedWithSingleData(Member data) {
                try{
                    RealmFactory.getSession().beginTransaction();
                    Member realmEntity = RealmFactory.getSession().copyToRealmOrUpdate(data);
                    RealmFactory.getSession().commitTransaction();
                    dataStoreOperationListener.onSucceedWithSingleData(realmEntity);
                }
                catch (Exception e) {
                    RealmFactory.getSession().cancelTransaction();
                    Crashlytics.logException(e);
                    Log.e(Constants.LOG_TAG, e.getMessage(), e);
                    String friendlyError = Constants.GENERIC_ERROR_MSG;
                    dataStoreOperationListener.onError(friendlyError);
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

    public void getLoggedInMemberBasicInfoOrigin(final IDataStoreOperationListener<Member> dataStoreOperationListener) {
        IDataStoreOperationListener<Member> remoteDataStoreOperationListener = new DataStoreOperationListener<Member>() {
            @Override
            public void onSucceedWithSingleData(Member data) {
                try{
                    dataStoreOperationListener.onSucceedWithSingleData(data);
                }
                catch (Exception e) {
                    Crashlytics.logException(e);
                    Log.e(Constants.LOG_TAG, e.getMessage(), e);
                    String friendlyError = Constants.GENERIC_ERROR_MSG;
                    dataStoreOperationListener.onError(friendlyError);
                }
            }

            @Override
            public void onError(String message) {
                dataStoreOperationListener.onError(message);
            }
        };
        memberRemoteDataStore.getLoggedInMemberBasicInfo(remoteDataStoreOperationListener);
    }

    public void getAttendeesForTicketOrderOrigin(String orderNumber, final IDataStoreOperationListener<NonConfirmedSummitAttendee> dataStoreOperationListener) {
        IDataStoreOperationListener<NonConfirmedSummitAttendee> remoteDataStoreOperationListener = new DataStoreOperationListener<NonConfirmedSummitAttendee>() {
            @Override
            public void onSucceedWithDataCollection(List<NonConfirmedSummitAttendee> data) {
                try{
                    super.onSucceedWithDataCollection(data);
                }
                catch (Exception e) {
                    Crashlytics.logException(e);
                    Log.e(Constants.LOG_TAG, e.getMessage(), e);
                    String friendlyError = Constants.GENERIC_ERROR_MSG;
                    dataStoreOperationListener.onError(friendlyError);
                }
            }

            @Override
            public void onError(String message) {
                dataStoreOperationListener.onError(message);
            }
        };
        memberRemoteDataStore.getAttendeesForTicketOrder(orderNumber, remoteDataStoreOperationListener);
    }
}