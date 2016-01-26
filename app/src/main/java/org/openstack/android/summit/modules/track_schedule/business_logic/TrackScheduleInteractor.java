package org.openstack.android.summit.modules.track_schedule.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.business_logic.ScheduleInteractor;
import org.openstack.android.summit.common.data_access.IGenericDataStore;
import org.openstack.android.summit.common.data_access.ISummitAttendeeDataStore;
import org.openstack.android.summit.common.data_access.ISummitDataStore;
import org.openstack.android.summit.common.data_access.ISummitEventDataStore;
import org.openstack.android.summit.common.entities.Track;
import org.openstack.android.summit.common.security.ISecurityManager;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 1/12/2016.
 */
public class TrackScheduleInteractor extends ScheduleInteractor implements ITrackScheduleInteractor {
    private IGenericDataStore genericDataStore;

    @Inject
    public TrackScheduleInteractor(ISummitEventDataStore summitEventDataStore, ISummitDataStore summitDataStore, ISummitAttendeeDataStore summitAttendeeDataStore, IGenericDataStore genericDataStore, IDTOAssembler dtoAssembler, ISecurityManager securityManager) {
        super(summitEventDataStore, summitDataStore, summitAttendeeDataStore, dtoAssembler, securityManager);
        this.genericDataStore = genericDataStore;
    }

    @Override
    public NamedDTO getTrack(Integer trackId) {
        Track track = genericDataStore.getByIdLocal(trackId, Track.class);
        NamedDTO dto = dtoAssembler.createDTO(track, NamedDTO.class);
        return dto;
    }
}
