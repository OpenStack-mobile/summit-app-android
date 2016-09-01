package org.openstack.android.summit.common.data_access;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.data_access.deserialization.DataStoreOperationListener;
import org.openstack.android.summit.common.entities.Feedback;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.NonConfirmedSummitAttendee;
import org.openstack.android.summit.common.utils.RealmFactory;

import java.util.List;

import io.realm.Realm;

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
            public void onSucceedWithSingleData(final Member detachedMember) {
                try{
                    Member member = RealmFactory.transaction(new RealmFactory.IRealmCallback<Member>() {
                        @Override
                        public Member callback(Realm session) throws Exception {
                            return session.copyToRealmOrUpdate(detachedMember);
                        }
                    });
                    dataStoreOperationListener.onSucceedWithSingleData(member);
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
        memberRemoteDataStore.getMemberInfo(remoteDataStoreOperationListener);
    }

    public Member getByIdLocal(int id) {
        return getByIdLocal(id, Member.class);
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

    @Override
    public void addFeedback(final Member member, Feedback feedback, final IDataStoreOperationListener dataStoreOperationListener) {
        IDataStoreOperationListener<Feedback> remoteDataStoreOperationListener = new DataStoreOperationListener<Feedback>() {
            @Override
            public void onSucceedWithSingleData(Feedback data) {
                super.onSucceedWithSingleData(data);

                try {
                    RealmFactory.getSession().beginTransaction();
                    member.getFeedback().add(data);
                    RealmFactory.getSession().commitTransaction();
                }
                catch (Exception e) {
                    RealmFactory.getSession().cancelTransaction();
                    throw e;
                }

                dataStoreOperationListener.onSucceedWithSingleData(data);
            }

            @Override
            public void onError(String message) {
                super.onError(message);
                dataStoreOperationListener.onError(message);
            }
        };
        memberRemoteDataStore.addFeedback(feedback, remoteDataStoreOperationListener);
    }
}