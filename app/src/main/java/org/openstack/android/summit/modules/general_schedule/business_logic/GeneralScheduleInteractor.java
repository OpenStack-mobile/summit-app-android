package org.openstack.android.summit.modules.general_schedule.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.IPushNotificationsManager;
import org.openstack.android.summit.common.ISession;
import org.openstack.android.summit.common.business_logic.ScheduleInteractor;
import org.openstack.android.summit.common.data_access.ISummitAttendeeDataStore;
import org.openstack.android.summit.common.data_access.ISummitDataStore;
import org.openstack.android.summit.common.data_access.ISummitEventDataStore;
import org.openstack.android.summit.common.data_access.data_polling.IDataUpdatePoller;
import org.openstack.android.summit.common.security.ISecurityManager;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 12/21/2015.
 */
public class GeneralScheduleInteractor extends ScheduleInteractor implements IGeneralScheduleInteractor {

    @Inject
    public GeneralScheduleInteractor(ISummitEventDataStore summitEventDataStore, ISummitDataStore summitDataStore, ISummitAttendeeDataStore summitAttendeeDataStore, IDTOAssembler dtoAssembler, ISecurityManager securityManager, IDataUpdatePoller dataUpdatePoller, IPushNotificationsManager pushNotificationsManager, ISession session) {
        super(summitEventDataStore, summitDataStore, summitAttendeeDataStore, dtoAssembler, securityManager, dataUpdatePoller, pushNotificationsManager, session);
    }
}
