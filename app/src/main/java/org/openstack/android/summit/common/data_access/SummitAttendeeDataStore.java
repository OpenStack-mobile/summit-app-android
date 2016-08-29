package org.openstack.android.summit.common.data_access;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.data_access.deserialization.DataStoreOperationListener;
import org.openstack.android.summit.common.entities.SummitAttendee;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.utils.RealmFactory;
import org.openstack.android.summit.common.utils.Void;

import io.realm.Realm;

/**
 * Created by Claudio Redi on 1/5/2016.
 */
public class SummitAttendeeDataStore extends GenericDataStore implements ISummitAttendeeDataStore {

    private ISummitAttendeeRemoteDataStore summitAttendeeRemoteDataStore;

    public SummitAttendeeDataStore(ISummitAttendeeRemoteDataStore summitAttendeeRemoteDataStore) {
        this.summitAttendeeRemoteDataStore = summitAttendeeRemoteDataStore;
    }

    @Override
    public void addEventToMemberSchedule(final SummitAttendee summitAttendee, final SummitEvent summitEvent, final IDataStoreOperationListener<SummitAttendee> dataStoreOperationListener) {
        addEventToMemberScheduleLocal(summitAttendee, summitEvent);

        IDataStoreOperationListener<SummitAttendee> remoteDataStoreOperationListener = new DataStoreOperationListener<SummitAttendee>() {
            @Override
            public void onSucceedWithSingleData(SummitAttendee data) {
                dataStoreOperationListener.onSucceedWithoutData();
            }

            @Override
            public void onError(String message) {
                removeEventFromMemberScheduleLocal(summitAttendee, summitEvent);
                dataStoreOperationListener.onError(message);
            }
        };
        summitAttendeeRemoteDataStore.addEventToSchedule(summitAttendee, summitEvent, remoteDataStoreOperationListener);
    }

    @Override
    public void addEventToMemberScheduleLocal(final SummitAttendee summitAttendee, final SummitEvent summitEvent) {

        try {

            RealmFactory.transaction(new RealmFactory.IRealmCallback<Void>() {
                @Override
                public Void callback(Realm session) throws Exception{
                    if (summitAttendee.getScheduledEvents().where().equalTo("id", summitEvent.getId()).count() == 0){
                        Log.d(Constants.LOG_TAG, String.format("adding event %s to myschedule", summitEvent.getId()));
                        summitAttendee.getScheduledEvents().add(summitEvent);
                    }
                    return Void.getInstance();
                }
            });
        }
        catch (Exception e) {
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
            Crashlytics.logException(e);
        }
    }

    @Override
    public void removeEventFromMemberScheduleLocal(final SummitAttendee summitAttendee, final SummitEvent summitEvent) {
        try{
            RealmFactory.transaction(new RealmFactory.IRealmCallback<Void>() {
                @Override
                public Void callback(Realm session) throws Exception{
                    if (summitAttendee.getScheduledEvents().where().equalTo("id", summitEvent.getId()).count() > 0) {
                        SummitEvent entityRealm = session.where(SummitEvent.class).equalTo("id", summitEvent.getId()).findFirst();
                        Log.d(Constants.LOG_TAG, String.format("removing event %s to myschedule", summitEvent.getId()));
                        summitAttendee.getScheduledEvents().remove(entityRealm);
                    }
                    return Void.getInstance();
                }
            });
        }
        catch (Exception e) {
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
            Crashlytics.logException(e);
        }
    }

    @Override
    public void removeEventFromMemberSchedule(SummitAttendee summitAttendee, final SummitEvent summitEvent, final IDataStoreOperationListener<SummitAttendee> dataStoreOperationListener) {

        IDataStoreOperationListener<SummitAttendee> remoteDataStoreOperationListener = new DataStoreOperationListener<SummitAttendee>() {
            @Override
            public void onSucceedWithSingleData(SummitAttendee data) {
                removeEventFromMemberScheduleLocal(data, summitEvent);
                dataStoreOperationListener.onSucceedWithoutData();
            }

            @Override
            public void onError(String message) {
                dataStoreOperationListener.onError(message);
            }
        };
        summitAttendeeRemoteDataStore.removeEventFromSchedule(summitAttendee, summitEvent, remoteDataStoreOperationListener);
    }

}
