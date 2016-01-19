package org.openstack.android.summit.common.data_access;

import org.openstack.android.summit.common.data_access.deserialization.DataStoreOperationListener;
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

/*    public func removeEventFromMemberShedule(attendee: SummitAttendee, event: SummitEvent, completionBlock : (SummitAttendee?, NSError?) -> Void) {
        summitAttendeeRemoteDataStore.removeEventFromShedule(attendee, event: event) { error in
            var innerError = error

            defer { completionBlock(attendee, innerError) }

            if error != nil {
                return
            }

            do {
                try self.removeEventFromMemberSheduleLocal(attendee, event: event)
            }
            catch {
                innerError = NSError(domain: "There was an error removing event from member schedule", code: 1001, userInfo: nil)
            }
        }
    }*/

    /*    public override init() {
        super.init()
    }

    public init(summitAttendeeRemoteDataStore: ISummitAttendeeRemoteDataStore) {
        self.summitAttendeeRemoteDataStore = summitAttendeeRemoteDataStore
    }

    var summitAttendeeRemoteDataStore: ISummitAttendeeRemoteDataStore!

    public func addFeedback(attendee: SummitAttendee, feedback: Feedback, completionBlock : (Feedback?, NSError?)->Void) {
        summitAttendeeRemoteDataStore.addFeedback(attendee, feedback: feedback) {(feedback, error) in
            if (error != nil) {
                completionBlock(nil, error)
                return
            }

            try! self.realm.write{
                attendee.feedback.append(feedback!)
            }
            completionBlock(feedback, error)
        }
    }

    public func addEventToMemberShedule(attendee: SummitAttendee, event: SummitEvent, completionBlock : (SummitAttendee?, NSError?) -> Void) {
        summitAttendeeRemoteDataStore.addEventToShedule(attendee, event: event) { error in

            if error != nil {
                return
            }

            self.addEventToMemberSheduleLocal(attendee, event: event)

            completionBlock(attendee, error)
        }
    }

    public func removeEventFromMemberShedule(attendee: SummitAttendee, event: SummitEvent, completionBlock : (SummitAttendee?, NSError?) -> Void) {
        summitAttendeeRemoteDataStore.removeEventFromShedule(attendee, event: event) { error in
            var innerError = error

            defer { completionBlock(attendee, innerError) }

            if error != nil {
                return
            }

            do {
                try self.removeEventFromMemberSheduleLocal(attendee, event: event)
            }
            catch {
                innerError = NSError(domain: "There was an error removing event from member schedule", code: 1001, userInfo: nil)
            }
        }
    }

    public func addEventToMemberSheduleLocal(attendee: SummitAttendee, event: SummitEvent) {

        try! self.realm.write {
            attendee.scheduledEvents.append(event)
        }
    }

    public func removeEventFromMemberSheduleLocal(attendee: SummitAttendee, event: SummitEvent) throws {

        try! self.realm.write {
            let index = attendee.scheduledEvents.indexOf("id = %@", event.id)
            if (index != nil) {
                attendee.scheduledEvents.removeAtIndex(index!)
            }
        }
    }*/
}
