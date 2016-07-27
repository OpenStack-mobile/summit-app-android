package org.openstack.android.summit.common.data_access;

import org.openstack.android.summit.common.data_access.deserialization.DataStoreOperationListener;
import org.openstack.android.summit.common.entities.Feedback;
import org.openstack.android.summit.common.entities.SummitAttendee;
import org.openstack.android.summit.common.entities.SummitEvent;

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
    public void addEventToMemberScheduleLocal(SummitAttendee summitAttendee, SummitEvent summitEvent) {
        realm.beginTransaction();
        try {
            if (summitAttendee.getScheduledEvents().where().equalTo("id", summitEvent.getId()).count() == 0){
                summitAttendee.getScheduledEvents().add(summitEvent);
            }
            realm.commitTransaction();
        }
        catch (Exception e) {
            realm.cancelTransaction();
            throw e;
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

    @Override
    public void removeEventFromMemberScheduleLocal(SummitAttendee summitAttendee, SummitEvent summitEvent) {
        realm.beginTransaction();
        try{
            if (summitAttendee.getScheduledEvents().where().equalTo("id", summitEvent.getId()).count() > 0) {
                summitAttendee.getScheduledEvents().remove(summitEvent);
            }
            realm.commitTransaction();
        }
        catch (Exception e) {
            realm.cancelTransaction();
            throw e;
        }
    }

    @Override
    public void addFeedback(final SummitAttendee attendee, Feedback feedback, final IDataStoreOperationListener dataStoreOperationListener) {
        IDataStoreOperationListener<Feedback> remoteDataStoreOperationListener = new DataStoreOperationListener<Feedback>() {
            @Override
            public void onSucceedWithSingleData(Feedback data) {
                super.onSucceedWithSingleData(data);

                realm.beginTransaction();
                try {
                    attendee.getFeedback().add(data);
                    realm.commitTransaction();
                }
                catch (Exception e) {
                    realm.cancelTransaction();
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
        summitAttendeeRemoteDataStore.addFeedback(attendee, feedback, remoteDataStoreOperationListener);
    }
}
