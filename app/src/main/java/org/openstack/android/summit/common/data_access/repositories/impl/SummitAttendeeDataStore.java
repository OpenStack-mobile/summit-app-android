package org.openstack.android.summit.common.data_access.repositories.impl;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.data_access.ISummitAttendeeRemoteDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitAttendeeDataStore;
import org.openstack.android.summit.common.data_access.repositories.strategies.IDeleteStrategy;
import org.openstack.android.summit.common.data_access.repositories.strategies.ISaveOrUpdateStrategy;
import org.openstack.android.summit.common.entities.SummitAttendee;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.utils.RealmFactory;
import org.openstack.android.summit.common.utils.Void;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Claudio Redi on 1/5/2016.
 */
public class SummitAttendeeDataStore extends GenericDataStore<SummitAttendee> implements ISummitAttendeeDataStore {

    private ISummitAttendeeRemoteDataStore summitAttendeeRemoteDataStore;

    public SummitAttendeeDataStore(ISummitAttendeeRemoteDataStore summitAttendeeRemoteDataStore, ISaveOrUpdateStrategy saveOrUpdateStrategy, IDeleteStrategy deleteStrategy) {
        super(SummitAttendee.class, saveOrUpdateStrategy, deleteStrategy);
        this.summitAttendeeRemoteDataStore = summitAttendeeRemoteDataStore;
    }

    @Override
    public Observable<Boolean> addEventToMemberSchedule(SummitAttendee me, final SummitEvent summitEvent)
    {

        int attendeeId = me.getId();
        int eventId  = summitEvent.getId();

        return summitAttendeeRemoteDataStore
                .addEventToSchedule(me, summitEvent)
                .doOnNext( res -> {

                    addEventToMemberScheduleLocal(
                            getById(attendeeId),
                            RealmFactory.getSession().where(SummitEvent.class).equalTo("id", eventId).findFirst()
                    );
                })
                .doOnError( res -> {
                    removeEventFromMemberScheduleLocal(
                            getById(attendeeId),
                            RealmFactory.getSession().where(SummitEvent.class).equalTo("id", eventId).findFirst()
                    );
                })
                .subscribeOn(Schedulers.io());
    }

    @Override
    public void addEventToMemberScheduleLocal(final SummitAttendee me, final SummitEvent summitEvent) {

        try {

            RealmFactory.transaction(session -> {
                if (me.getScheduledEvents().where().equalTo("id", summitEvent.getId()).count() == 0){
                    Log.d(Constants.LOG_TAG, String.format("adding event %s to myschedule", summitEvent.getId()));
                    me.getScheduledEvents().add(summitEvent);
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
    public void removeEventFromMemberScheduleLocal(final SummitAttendee me, final SummitEvent summitEvent) {
        try{
            RealmFactory.transaction(session -> {
                if (me.getScheduledEvents().where().equalTo("id", summitEvent.getId()).count() > 0) {
                    SummitEvent entityRealm = session.where(SummitEvent.class).equalTo("id", summitEvent.getId()).findFirst();
                    Log.d(Constants.LOG_TAG, String.format("removing event %s to myschedule", summitEvent.getId()));
                    me.getScheduledEvents().remove(entityRealm);
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
    public Observable<Boolean> removeEventFromMemberSchedule(SummitAttendee me, SummitEvent summitEvent) {

        int attendeeId = me.getId();
        int eventId  = summitEvent.getId();

        return summitAttendeeRemoteDataStore
                .removeEventFromSchedule(me, summitEvent)
                .doOnNext( res -> {
                    removeEventFromMemberScheduleLocal(
                            getById(attendeeId),
                            RealmFactory.getSession().where(SummitEvent.class).equalTo("id", eventId).findFirst()
                    );
                })
                .doOnError( res -> {
                    addEventToMemberScheduleLocal(
                            getById(attendeeId),
                            RealmFactory.getSession().where(SummitEvent.class).equalTo("id", eventId).findFirst()
                    );
                })
                .subscribeOn(Schedulers.io());
    }

}
