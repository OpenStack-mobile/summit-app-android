package org.openstack.android.summit.common.data_access;

import org.openstack.android.summit.common.data_access.deserialization.DataStoreOperationListener;
import org.openstack.android.summit.common.entities.SummitAttendee;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.utils.RealmFactory;

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
        RealmFactory.getSession().beginTransaction();
        try {
            if (summitAttendee.getScheduledEvents().where().equalTo("id", summitEvent.getId()).count() == 0){
                summitAttendee.getScheduledEvents().add(summitEvent);
            }
            RealmFactory.getSession().commitTransaction();
        }
        catch (Exception e) {
            RealmFactory.getSession().cancelTransaction();
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
        RealmFactory.getSession().beginTransaction();
        try{
            if (summitAttendee.getScheduledEvents().where().equalTo("id", summitEvent.getId()).count() > 0) {
                summitAttendee.getScheduledEvents().remove(summitEvent);
            }
            RealmFactory.getSession().commitTransaction();
        }
        catch (Exception e) {
            RealmFactory.getSession().cancelTransaction();
            throw e;
        }
    }

}
