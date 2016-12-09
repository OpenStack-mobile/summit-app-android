package org.openstack.android.summit.modules.general_schedule_filter.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.DTOs.TrackGroupDTO;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.IGenericDataStore;
import org.openstack.android.summit.common.data_access.ISummitDataStore;
import org.openstack.android.summit.common.data_access.ISummitEventDataStore;
import org.openstack.android.summit.common.data_access.ITrackGroupDataStore;
import org.openstack.android.summit.common.data_access.IVenueDataStore;
import org.openstack.android.summit.common.entities.EventType;
import org.openstack.android.summit.common.entities.SummitType;
import org.openstack.android.summit.common.entities.Tag;
import org.openstack.android.summit.common.entities.TrackGroup;
import org.openstack.android.summit.common.entities.Venue;

import java.util.ArrayList;
import java.util.List;

import io.realm.Sort;

/**
 * Created by Claudio Redi on 2/1/2016.
 */
public class GeneralScheduleFilterInteractor extends BaseInteractor implements IGeneralScheduleFilterInteractor {

    private IGenericDataStore genericDataStore;
    private ISummitEventDataStore summitEventDataStore;
    private IVenueDataStore venueDataStore;
    private ITrackGroupDataStore trackGroupDataStore;

    public GeneralScheduleFilterInteractor
    (
        ISummitDataStore summitDataStore,
        ISummitEventDataStore summitEventDataStore,
        IGenericDataStore genericDataStore,
        IVenueDataStore venueDataStore,
        ITrackGroupDataStore trackGroupDataStore,
        IDTOAssembler dtoAssembler,
        ISummitSelector summitSelector
    )
    {
        super(dtoAssembler, summitSelector, summitDataStore);
        this.venueDataStore       = venueDataStore;
        this.genericDataStore     = genericDataStore;
        this.summitEventDataStore = summitEventDataStore;
        this.trackGroupDataStore  = trackGroupDataStore;
    }

    @Override
    public List<NamedDTO> getSummitTypes() {
        List<SummitType> summitTypes = genericDataStore.getAllLocal(SummitType.class, new String[] { "name"}, new Sort[]{ Sort.ASCENDING });
        List<SummitType> results     =  new ArrayList<>();
        for(SummitType summitType: summitTypes){
            if ( summitEventDataStore.countBySummitType(summitType.getId()) > 0)
                results.add(summitType);
        }
        return createDTOList(results, NamedDTO.class);
    }

    @Override
    public List<NamedDTO> getEventTypes() {
        List<EventType> eventTypes = genericDataStore.getAllLocal(EventType.class, new String[] { "name"}, new Sort[]{ Sort.ASCENDING });
        List<EventType> results    = new ArrayList<>();
        for(EventType eventType: eventTypes){
            if ( summitEventDataStore.countByEventType(eventType.getId()) > 0)
                results.add(eventType);
        }
        return createDTOList(results, NamedDTO.class);
    }

    @Override
    public List<String> getLevels() {
        return summitEventDataStore.getPresentationLevelsLocal();
    }

    @Override
    public List<NamedDTO> getVenues() {
        List<Venue> venues = venueDataStore.getInternalsBySummit(summitSelector.getCurrentSummitId());
        return createDTOList(venues, NamedDTO.class);
    }

    @Override
    public List<TrackGroupDTO> getTrackGroups() {
        List<TrackGroup> trackGroups = trackGroupDataStore.getAllBySummit(summitSelector.getCurrentSummitId());
        List<TrackGroup> results = new ArrayList<>();
        for(TrackGroup trackGroup: trackGroups){
            if ( summitEventDataStore.countByTrackGroup(trackGroup.getId()) > 0)
                results.add(trackGroup);
        }
        return createDTOList(results, TrackGroupDTO.class);
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