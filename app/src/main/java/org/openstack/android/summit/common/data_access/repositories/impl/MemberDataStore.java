package org.openstack.android.summit.common.data_access.repositories.impl;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.data_access.IMemberRemoteDataStore;
import org.openstack.android.summit.common.data_access.repositories.IMemberDataStore;
import org.openstack.android.summit.common.data_access.repositories.strategies.IDeleteStrategy;
import org.openstack.android.summit.common.data_access.repositories.strategies.ISaveOrUpdateStrategy;
import org.openstack.android.summit.common.entities.Feedback;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.NonConfirmedSummitAttendee;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.utils.RealmFactory;
import org.openstack.android.summit.common.utils.Void;

import java.util.Date;
import java.util.List;

import io.reactivex.Observable;

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

    @Override
    public Observable<List<NonConfirmedSummitAttendee>> getAttendeesForTicketOrder(String orderNumber) {
        return memberRemoteDataStore.getAttendeesForTicketOrder(orderNumber);
    }

    @Override
    public Observable<Integer> addFeedback(final Member member, Feedback feedback) {

        int eventId   = feedback.getEvent().getId();
        int rate      = feedback.getRate();
        Date  date    = feedback.getDate();
        String review = feedback.getReview();
        int memberId  = member.getId();

        return memberRemoteDataStore
                .addFeedback(eventId, rate, review)
                .doOnNext( id -> {
                    RealmFactory.transaction(session -> {
                        // save it locally and recreate bc realm does not support xcross threading
                        Member me            = this.getById(memberId);
                        SummitEvent event    = session.where(SummitEvent.class).equalTo("id", eventId).findFirst();
                        Feedback newFeedback = session.createObject(Feedback.class, id);
                        newFeedback.setOwner(me);
                        newFeedback.setDate(date);
                        newFeedback.setRate(rate);
                        newFeedback.setReview(review);
                        newFeedback.setEvent(event);
                        me.getFeedback().add(newFeedback);
                        return newFeedback;
                    });
                });
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
                });
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
                });
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