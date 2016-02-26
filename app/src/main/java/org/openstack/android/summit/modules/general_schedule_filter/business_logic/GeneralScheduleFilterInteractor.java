package org.openstack.android.summit.modules.general_schedule_filter.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.DTOs.TrackGroupDTO;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.GenericDataStore;
import org.openstack.android.summit.common.data_access.IGenericDataStore;
import org.openstack.android.summit.common.data_access.ISummitEventDataStore;
import org.openstack.android.summit.common.data_access.data_polling.IDataUpdatePoller;
import org.openstack.android.summit.common.entities.EventType;
import org.openstack.android.summit.common.entities.SummitType;
import org.openstack.android.summit.common.entities.Tag;
import org.openstack.android.summit.common.entities.TrackGroup;

import java.util.ArrayList;
import java.util.List;

import io.realm.Sort;

/**
 * Created by Claudio Redi on 2/1/2016.
 */
public class GeneralScheduleFilterInteractor extends BaseInteractor implements IGeneralScheduleFilterInteractor {
    private IGenericDataStore genericDataStore;
    private ISummitEventDataStore summitEventDataStore;

    public GeneralScheduleFilterInteractor(ISummitEventDataStore summitEventDataStore, IGenericDataStore genericDataStore, IDTOAssembler dtoAssembler, IDataUpdatePoller dataUpdatePoller) {
        super(dtoAssembler, dataUpdatePoller);
        this.genericDataStore = genericDataStore;
        this.summitEventDataStore = summitEventDataStore;
    }

    @Override
    public List<NamedDTO> getSummitTypes() {
        List<SummitType> summitTypes = genericDataStore.getAllLocal(SummitType.class, new String[] { "name"}, new Sort[]{ Sort.ASCENDING });
        List<NamedDTO> dtos = createDTOList(summitTypes, NamedDTO.class);
        return dtos;
    }

    @Override
    public List<NamedDTO> getEventTypes() {
        List<EventType> eventTypes = genericDataStore.getAllLocal(EventType.class, new String[] { "name"}, new Sort[]{ Sort.ASCENDING });
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
        List<TrackGroup> trackGroups = genericDataStore.getAllLocal(TrackGroup.class, new String[] { "name"}, new Sort[]{ Sort.ASCENDING });
        List<TrackGroupDTO> dtos = createDTOList(trackGroups, TrackGroupDTO.class);
        return dtos;
    }

    @Override
    public List<String> getTags() {
        List<Tag> tags = genericDataStore.getAllLocal(Tag.class);
        List<String> dtos = new ArrayList<>();
        for (Tag tag: tags) {
            dtos.add(tag.getTag());
        }
        return dtos;
    }
}