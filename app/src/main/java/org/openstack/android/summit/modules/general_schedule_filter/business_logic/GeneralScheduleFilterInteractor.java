package org.openstack.android.summit.modules.general_schedule_filter.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.DTOs.TrackGroupDTO;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.GenericDataStore;
import org.openstack.android.summit.common.data_access.IGenericDataStore;
import org.openstack.android.summit.common.data_access.ISummitEventDataStore;
import org.openstack.android.summit.common.entities.EventType;
import org.openstack.android.summit.common.entities.SummitType;
import org.openstack.android.summit.common.entities.TrackGroup;

import java.util.List;

/**
 * Created by Claudio Redi on 2/1/2016.
 */
public class GeneralScheduleFilterInteractor extends BaseInteractor implements IGeneralScheduleFilterInteractor {
    private IGenericDataStore genericDataStore;
    private ISummitEventDataStore summitEventDataStore;

    public GeneralScheduleFilterInteractor(ISummitEventDataStore summitEventDataStore, IGenericDataStore genericDataStore, IDTOAssembler dtoAssembler) {
        super(dtoAssembler);
        this.genericDataStore = genericDataStore;
        this.summitEventDataStore = summitEventDataStore;
    }

    @Override
    public List<NamedDTO> getSummitTypes() {
        List<SummitType> summitTypes = genericDataStore.getaAllLocal(SummitType.class);
        List<NamedDTO> dtos = createDTOList(summitTypes, NamedDTO.class);
        return dtos;
    }

    @Override
    public List<NamedDTO> getEventTypes() {
        List<EventType> eventTypes = genericDataStore.getaAllLocal(EventType.class);
        List<NamedDTO> dtos = createDTOList(eventTypes, NamedDTO.class);
        return dtos;
    }

    @Override
    public List<String> getLevels() {
        List<String> levels = summitEventDataStore.getPresentationLevelsLocal();
        return levels;
    }

    @Override
    public List<TrackGroupDTO> getTrackGroups() {
        List<TrackGroup> trackGroups = genericDataStore.getaAllLocal(TrackGroup.class);
        List<TrackGroupDTO> dtos = createDTOList(trackGroups, TrackGroupDTO.class);
        return dtos;
    }
}
