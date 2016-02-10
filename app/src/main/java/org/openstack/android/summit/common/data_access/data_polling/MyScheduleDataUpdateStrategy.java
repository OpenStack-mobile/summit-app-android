package org.openstack.android.summit.common.data_access.data_polling;

import org.openstack.android.summit.common.data_access.IGenericDataStore;
import org.openstack.android.summit.common.data_access.ISummitAttendeeDataStore;
import org.openstack.android.summit.common.entities.DataUpdate;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.security.ISecurityManager;

/**
 * Created by Claudio Redi on 2/8/2016.
 */
public class MyScheduleDataUpdateStrategy extends DataUpdateStrategy {
    ISecurityManager securityManager;
    ISummitAttendeeDataStore summitAttendeeDataStore;

    public MyScheduleDataUpdateStrategy(IGenericDataStore genericDataStore, ISummitAttendeeDataStore summitAttendeeDataStore, ISecurityManager securityManager) {
        super(genericDataStore);
        this.summitAttendeeDataStore = summitAttendeeDataStore;
        this.securityManager = securityManager;
    }

    @Override
    public void process(DataUpdate dataUpdate) {
        Member currentMember = securityManager.getCurrentMember();

        switch (dataUpdate.getOperation()) {
            case DataOperation.Insert:
            case DataOperation.Update:
                summitAttendeeDataStore.addEventToMemberSheduleLocal(currentMember.getAttendeeRole(), (SummitEvent)dataUpdate.getEntity());
            case DataOperation.Delete:
                summitAttendeeDataStore.removeEventFromMemberSheduleLocal(currentMember.getAttendeeRole(), (SummitEvent)dataUpdate.getEntity());
        }
    }
}
