package org.openstack.android.summit.modules.level_schedule.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.push_notifications.IPushNotificationsManager;
import org.openstack.android.summit.common.ISession;
import org.openstack.android.summit.common.business_logic.ScheduleInteractor;
import org.openstack.android.summit.common.data_access.ISummitAttendeeDataStore;
import org.openstack.android.summit.common.data_access.ISummitDataStore;
import org.openstack.android.summit.common.data_access.ISummitEventDataStore;
import org.openstack.android.summit.common.data_access.data_polling.IDataUpdatePoller;
import org.openstack.android.summit.common.security.ISecurityManager;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 1/11/2016.
 */
public class LevelScheduleInteractor extends ScheduleInteractor implements ILevelScheduleInteractor {
    @Inject
    public LevelScheduleInteractor(ISummitEventDataStore summitEventDataStore, ISummitDataStore summitDataStore, ISummitAttendeeDataStore summitAttendeeDataStore, IDTOAssembler dtoAssembler, ISecurityManager securityManager, IPushNotificationsManager pushNotificationsManager, ISession session, IDataUpdatePoller dataUpdatePoller) {
        super(summitEventDataStore, summitDataStore, summitAttendeeDataStore, dtoAssembler, securityManager, pushNotificationsManager, session, dataUpdatePoller);
    }
}
