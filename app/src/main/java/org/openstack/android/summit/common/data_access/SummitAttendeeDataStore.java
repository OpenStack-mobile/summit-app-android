package org.openstack.android.summit.common.data_access;

import org.openstack.android.summit.common.data_access.deserialization.DataStoreOperationListener;
import org.openstack.android.summit.common.entities.Feedback;
import org.openstack.android.summit.common.entities.Summit;
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
    public void addEventToMemberShedule(SummitAttendee summitAttendee, final SummitEvent summitEvent, final IDataStoreOperationListener<SummitAttendee> dataStoreOperationListener) {

        IDataStoreOperationListener<SummitAttendee> remomoteDataStoreOperationListener = new DataStoreOperationListener<SummitAttendee>() {
            @Override
            public void onSuceedWithSingleData(SummitAttendee data) {
                addEventToMemberSheduleLocal(data, summitEvent);
                dataStoreOperationListener.onSucceedWithoutData();
            }

            @Override
            public void onError(String message) {
                dataStoreOperationListener.onError(message);
            }
        };
        summitAttendeeRemoteDataStore.addEventToShedule(summitAttendee, summitEvent, remomoteDataStoreOperationListener);
    }

    @Override
    public void addEventToMemberSheduleLocal(SummitAttendee summitAttendee, SummitEvent summitEvent) {
        realm.beginTransaction();
        summitAttendee.getScheduledEvents().add(summitEvent);
        realm.commitTransaction();
    }

    @Override
    public void removeEventFromMemberShedule(SummitAttendee summitAttendee, final SummitEvent summitEvent, final IDataStoreOperationListener<SummitAttendee> dataStoreOperationListener) {

        IDataStoreOperationListener<SummitAttendee> remomoteDataStoreOperationListener = new DataStoreOperationListener<SummitAttendee>() {
            @Override
            public void onSuceedWithSingleData(SummitAttendee data) {
                removeEventFromMemberSheduleLocal(data, summitEvent);
                dataStoreOperationListener.onSucceedWithoutData();
            }

            @Override
            public void onError(String message) {
                dataStoreOperationListener.onError(message);
            }
        };
        summitAttendeeRemoteDataStore.removeEventFromShedule(summitAttendee, summitEvent, remomoteDataStoreOperationListener);
    }

    @Override
    public void removeEventFromMemberSheduleLocal(SummitAttendee summitAttendee, SummitEvent summitEvent) {
        realm.beginTransaction();
        summitAttendee.getScheduledEvents().remove(summitEvent);
        realm.commitTransaction();
    }

    @Override
    public void addFeedback(final SummitAttendee attendee, Feedback feedback, final IDataStoreOperationListener dataStoreOperationListener) {
        IDataStoreOperationListener<Feedback> remoteDataStoreOperationListener = new DataStoreOperationListener<Feedback>() {
            @Override
            public void onSuceedWithSingleData(Feedback data) {
                super.onSuceedWithSingleData(data);
                realm.beginTransaction();
                attendee.getFeedback().add(data);
                realm.commitTransaction();
                dataStoreOperationListener.onSuceedWithSingleData(data);
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
