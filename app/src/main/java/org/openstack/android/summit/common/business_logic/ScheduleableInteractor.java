package org.openstack.android.summit.common.business_logic;

import org.openstack.android.summit.common.ISession;
import org.openstack.android.summit.common.data_access.ISummitDataStore;
import org.openstack.android.summit.common.data_access.ISummitEventDataStore;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.security.ISecurityManager;

/**
 * Created by Claudio Redi on 1/4/2016.
 */
public class ScheduleableInteractor {
    private ISecurityManager securityManager;
    private ISummitEventDataStore summitEventDataStore;

    public ScheduleableInteractor(ISecurityManager securityManager, ISummitEventDataStore summitEventDataStore) {
        this.securityManager = securityManager;
        this.summitEventDataStore = summitEventDataStore;
    }

    public void addEventToLoggedInMemberSchedule(int eventId, IInteractorAsyncOperationListener<Void> interactorAsyncOperationListener) {
        Member loggedInMember = securityManager.getCurrentMember();
    }
}

/*public class ScheduleableInteractor: NSObject, IScheduleableInteractor {
        var summitAttendeeDataStore: ISummitAttendeeDataStore!
        var securityManager: SecurityManager!
        var eventDataStore: IEventDataStore!
        var reachability: IReachability!

public func addEventToLoggedInMemberSchedule(eventId: Int, completionBlock: (NSError?) -> Void) {
        if !reachability.isConnectedToNetwork() {
        let error = NSError(domain: "There is no network connectivity. Operation cancelled", code: 12002, userInfo: nil)
        completionBlock(error)
        return
        }

        let loggedInMember = securityManager.getCurrentMember()
        let event = eventDataStore.getByIdLocal(eventId)

        summitAttendeeDataStore.addEventToMemberShedule(loggedInMember!.attendeeRole!, event: event!) {(attendee, error) in
        completionBlock(error)
        }
        }

public func removeEventFromLoggedInMemberSchedule(eventId: Int, completionBlock: (NSError?) -> Void) {
        if !reachability.isConnectedToNetwork() {
        let error = NSError(domain: "There is no network connectivity. Operation cancelled", code: 12001, userInfo: nil)
        completionBlock(error)
        return
        }

        let loggedInMember = securityManager.getCurrentMember()
        let event = eventDataStore.getByIdLocal(eventId)

        summitAttendeeDataStore.removeEventFromMemberShedule(loggedInMember!.attendeeRole!, event: event!) {(attendee, error) in
        completionBlock(error)
        }
        }

public func isEventScheduledByLoggedMember(eventId: Int) -> Bool {
        guard let loggedInMember = securityManager.getCurrentMember() else {
        return false
        }

        return loggedInMember.attendeeRole!.scheduledEvents.filter("id = \(eventId)").count > 0
        }

public func isMemberLoggedIn() -> Bool {
        return securityManager.isLoggedIn()
        }
        }

*/