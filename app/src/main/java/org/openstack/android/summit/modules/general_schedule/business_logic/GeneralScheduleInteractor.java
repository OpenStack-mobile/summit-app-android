package org.openstack.android.summit.modules.general_schedule.business_logic;

import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.data_access.repositories.IMemberDataStore;
import org.openstack.android.summit.common.push_notifications.IPushNotificationsManager;
import org.openstack.android.summit.common.ISession;
import org.openstack.android.summit.common.business_logic.ScheduleInteractor;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitEventDataStore;
import org.openstack.android.summit.common.network.IReachability;
import org.openstack.android.summit.common.security.ISecurityManager;

/**
 * Created by Claudio Redi on 12/21/2015.
 */
public class GeneralScheduleInteractor extends ScheduleInteractor implements IGeneralScheduleInteractor {

    IReachability reachability;

    public GeneralScheduleInteractor
    (
        ISummitEventDataStore summitEventDataStore,
        IMemberDataStore memberDataStore,
        ISummitDataStore summitDataStore,
        IDTOAssembler dtoAssembler,
        ISecurityManager securityManager,
        IPushNotificationsManager pushNotificationsManager,
        ISession session,
        IReachability reachability,
        ISummitSelector summitSelector
    ) {
        super
        (
            summitEventDataStore,
            summitDataStore,
            memberDataStore,
            dtoAssembler,
            securityManager,
            pushNotificationsManager,
            session,
            summitSelector
        );
        this.reachability = reachability;
    }



    @Override
    public boolean isNetworkingAvailable() {
        return reachability.isNetworkingAvailable(OpenStackSummitApplication.context);
    }

}
