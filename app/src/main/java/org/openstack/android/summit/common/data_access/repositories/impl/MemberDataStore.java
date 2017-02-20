package org.openstack.android.summit.common.data_access.repositories.impl;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.data_access.IDataStoreOperationListener;
import org.openstack.android.summit.common.data_access.IMemberRemoteDataStore;
import org.openstack.android.summit.common.data_access.deserialization.DataStoreOperationListener;
import org.openstack.android.summit.common.data_access.repositories.IMemberDataStore;
import org.openstack.android.summit.common.data_access.repositories.strategies.IDeleteStrategy;
import org.openstack.android.summit.common.data_access.repositories.strategies.ISaveOrUpdateStrategy;
import org.openstack.android.summit.common.entities.Feedback;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.NonConfirmedSummitAttendee;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.utils.RealmFactory;
import org.openstack.android.summit.common.utils.Void;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmSchema;

/**
 * Created by Claudio Redi on 12/16/2015.
 */
public class MemberDataStore extends GenericDataStore<Member> implements IMemberDataStore {
    private IMemberRemoteDataStore memberRemoteDataStore;

    public MemberDataStore
    (
        IMemberRemoteDataStore memberRemoteDataStore,
        ISaveOrUpdateStrategy saveOrUpdateStrategy,
        IDeleteStrategy deleteStrategy
    )
    {
        super(Member.class, saveOrUpdateStrategy, deleteStrategy);
        this.memberRemoteDataStore = memberRemoteDataStore;
    }

    @Override
    public Observable<Integer> getLoggedInMember() {
        return memberRemoteDataStore.getMemberInfo().map(member ->
                member.getId()
        );
    }

    public void getAttendeesForTicketOrder(String orderNumber, final IDataStoreOperationListener<NonConfirmedSummitAttendee> dataStoreOperationListener) {
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
            public void onSucceedWithSingleData(final Feedback data) {
                super.onSucceedWithSingleData(data);

                try {
                    RealmFactory.transaction(new RealmFactory.IRealmCallback<Void>() {
                        @Override
                        public Void callback(Realm session) throws Exception {
                            member.getFeedback().add(data);
                            return Void.getInstance();
                        }
                    });
                    dataStoreOperationListener.onSucceedWithSingleData(data);
                }
                catch (Exception ex) {
                    super.onError(ex.getMessage());
                    Crashlytics.logException(ex);
                    onError(ex.getMessage());
                }
            }

            @Override
            public void onError(String message) {
                super.onError(message);
                dataStoreOperationListener.onError(message);
            }
        };
        memberRemoteDataStore.addFeedback(feedback, remoteDataStoreOperationListener);
    }

    @Override
    public void addEventToMyFavoritesLocal(Member me, SummitEvent summitEvent) {
        try {

            RealmFactory.transaction(session -> {
                if (me.getFavoriteEvents().where().equalTo("id", summitEvent.getId()).count() == 0){
                    Log.d(Constants.LOG_TAG, String.format("adding event %s to my favorites", summitEvent.getId()));
                    me.getFavoriteEvents().add(summitEvent);
                }
                return Void.getInstance();
            });
        }
        catch (Exception e) {
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
            Crashlytics.logException(e);
        }
    }

    @Override
    public void removeEventFromMyFavoritesLocal(Member me, SummitEvent summitEvent) {
        try{
            RealmFactory.transaction(session -> {
                if (me.getFavoriteEvents().where().equalTo("id", summitEvent.getId()).count() > 0) {
                    SummitEvent entityRealm = session.where(SummitEvent.class).equalTo("id", summitEvent.getId()).findFirst();
                    Log.d(Constants.LOG_TAG, String.format("removing event %s to favorites ", summitEvent.getId()));
                    me.getFavoriteEvents().remove(entityRealm);
                }
                return Void.getInstance();
            });
        }
        catch (Exception e) {
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
            Crashlytics.logException(e);
        }
    }

    @Override
    public Observable<Boolean> addEventToMyFavorites(Member me, SummitEvent summitEvent) {

        int memberId = me.getId();
        int eventId  = summitEvent.getId();

        return memberRemoteDataStore
                .addSummitEvent2Favorites(summitEvent.getSummit().getId(), summitEvent.getId())
                .doOnNext( res -> {

                    addEventToMyFavoritesLocal(
                            getById(memberId),
                            RealmFactory.getSession().where(SummitEvent.class).equalTo("id", eventId).findFirst()
                    );
                })
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<Boolean> removeEventFromMyFavorites(Member me, SummitEvent summitEvent) {

        int memberId = me.getId();
        int eventId  = summitEvent.getId();

        return memberRemoteDataStore
                .removeSummitEventFromFavorites(summitEvent.getSummit().getId(), summitEvent.getId())
                .doOnNext( res -> {

                    removeEventFromMyFavoritesLocal(
                            getById(memberId),
                            RealmFactory.getSession().where(SummitEvent.class).equalTo("id", eventId).findFirst()
                    );
                })
                .subscribeOn(Schedulers.io());
    }

    @Override
    public boolean isEventOnMyFavorites(Member me, SummitEvent summitEvent) {
        return RealmFactory.getSession()
                .where(Member.class)
                .equalTo("id", me.getId())
                .equalTo("favoriteEvents.id", summitEvent.getId())
                .count() > 0;
    }

}