package org.openstack.android.summit.modules.track_schedule.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.business_logic.ScheduleInteractor;
import org.openstack.android.summit.common.data_access.ISummitAttendeeDataStore;
import org.openstack.android.summit.common.data_access.ISummitDataStore;
import org.openstack.android.summit.common.data_access.ISummitEventDataStore;
import org.openstack.android.summit.common.security.ISecurityManager;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 1/12/2016.
 */
public class TrackScheduleInteractor extends ScheduleInteractor implements ITrackScheduleInteractor {
    @Inject
    public TrackScheduleInteractor(ISummitEventDataStore summitEventDataStore, ISummitDataStore summitDataStore, ISummitAttendeeDataStore summitAttendeeDataStore, IDTOAssembler dtoAssembler, ISecurityManager securityManager) {
        super(summitEventDataStore, summitDataStore, summitAttendeeDataStore, dtoAssembler, securityManager);
    }
}
