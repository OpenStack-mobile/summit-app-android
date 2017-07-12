package org.openstack.android.summit.modules.track_schedule.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.data_access.repositories.IMemberDataStore;
import org.openstack.android.summit.common.data_access.repositories.ITrackDataStore;
import org.openstack.android.summit.common.push_notifications.IPushNotificationsManager;
import org.openstack.android.summit.common.ISession;
import org.openstack.android.summit.common.business_logic.ScheduleInteractor;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitEventDataStore;
import org.openstack.android.summit.common.entities.Track;
import org.openstack.android.summit.common.security.ISecurityManager;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 1/12/2016.
 */
public class TrackScheduleInteractor extends ScheduleInteractor implements ITrackScheduleInteractor {
    private ITrackDataStore trackDataStore;

    @Inject
    public TrackScheduleInteractor
    (
            IMemberDataStore memberDataStore,
            ISummitEventDataStore summitEventDataStore,
            ISummitDataStore summitDataStore,
            ITrackDataStore trackDataStore,
            IDTOAssembler dtoAssembler,
            ISecurityManager securityManager,
            IPushNotificationsManager pushNotificationsManager,
            ISession session,
            ISummitSelector summitSelector
    )
    {
        super(summitEventDataStore, summitDataStore, memberDataStore, dtoAssembler, securityManager, pushNotificationsManager, session, summitSelector);
        this.trackDataStore = trackDataStore;
    }

    @Override
    public NamedDTO getTrack(Integer trackId) {
        Track track = trackDataStore.getById(trackId);
        return (track != null) ? dtoAssembler.createDTO(track, NamedDTO.class) : null;
    }
}
