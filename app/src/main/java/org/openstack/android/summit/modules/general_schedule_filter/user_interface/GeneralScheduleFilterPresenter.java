package org.openstack.android.summit.modules.general_schedule_filter.user_interface;

import android.graphics.Color;
import android.os.Bundle;

import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.DTOs.SummitDTO;
import org.openstack.android.summit.common.DTOs.TrackDTO;
import org.openstack.android.summit.common.DTOs.TrackGroupDTO;
import org.openstack.android.summit.common.IScheduleFilter;
import org.openstack.android.summit.modules.general_schedule_filter.IGeneralScheduleFilterWireframe;
import org.openstack.android.summit.modules.general_schedule_filter.business_logic.IGeneralScheduleFilterInteractor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Claudio Redi on 2/1/2016.
 */
public class GeneralScheduleFilterPresenter
        extends AbstractScheduleFilterPresenter<IGeneralScheduleFilterView>
        implements IGeneralScheduleFilterPresenter {

    private List<NamedDTO>      summitTypes;
    private List<NamedDTO>      venues;
    private List<NamedDTO>      eventTypes;
    private List<String>        levels;
    private List<TrackGroupDTO> trackGroups;

    public GeneralScheduleFilterPresenter
    (
        IGeneralScheduleFilterInteractor interactor,
        IGeneralScheduleFilterWireframe wireframe,
        IScheduleFilter scheduleFilter
    )
    {
        super(interactor, wireframe, scheduleFilter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        summitTypes = interactor.getSummitTypes();
        eventTypes  = interactor.getEventTypes();
        levels      = interactor.getLevels();
        trackGroups = interactor.getTrackGroups();
        venues      = interactor.getVenues();

        scheduleFilter.getFilterSections().clear();

        MultiFilterSection filterSection = new MultiFilterSection();
        filterSection.setType(FilterSectionType.SummitType);
        filterSection.setName(FilterSectionType.SummitType.toString());
        FilterSectionItem filterSectionItem;
        for (NamedDTO summitType : summitTypes) {
            filterSectionItem = createSectionItem(summitType.getId(), summitType.getName(), filterSection.getType());
            filterSection.getItems().add(filterSectionItem);
        }
        scheduleFilter.getFilterSections().add(filterSection);

        filterSection = new MultiFilterSection();
        filterSection.setType(FilterSectionType.EventType);
        filterSection.setName(FilterSectionType.EventType.toString());
        for (NamedDTO eventType : eventTypes) {
            filterSectionItem = createSectionItem(eventType.getId(), eventType.getName(), filterSection.getType());
            filterSection.getItems().add(filterSectionItem);
        }

        scheduleFilter.getFilterSections().add(filterSection);

        filterSection = new MultiFilterSection();
        filterSection.setType(FilterSectionType.Level);
        filterSection.setName(FilterSectionType.Level.toString());
        for (String level : levels) {
            filterSectionItem = createSectionItem(0, level, filterSection.getType());
            filterSection.getItems().add(filterSectionItem);
        }
        scheduleFilter.getFilterSections().add(filterSection);

        filterSection = new MultiFilterSection();
        filterSection.setType(FilterSectionType.TrackGroup);
        filterSection.setName(FilterSectionType.TrackGroup.toString());
        for (NamedDTO trackGroup : trackGroups) {
            filterSectionItem = createSectionItem(trackGroup.getId(), trackGroup.getName(), filterSection.getType());
            filterSection.getItems().add(filterSectionItem);
        }

        scheduleFilter.getFilterSections().add(filterSection);

        SingleFilterSelection singleFilterSection = new SingleFilterSelection(true);
        singleFilterSection.setType(FilterSectionType.HidePastTalks);
        singleFilterSection.setName(FilterSectionType.HidePastTalks.toString());
        scheduleFilter.getFilterSections().add(singleFilterSection);

        filterSection = new MultiFilterSection();
        filterSection.setType(FilterSectionType.Venues);
        filterSection.setName(FilterSectionType.Venues.toString());
        for (NamedDTO venue : venues) {
            filterSectionItem = createSectionItem(venue.getId(), venue.getName(), filterSection.getType());
            filterSection.getItems().add(filterSectionItem);
        }

        scheduleFilter.getFilterSections().add(filterSection);

        SingleFilterSelection singleFilterSection2 = new SingleFilterSelection(false);
        singleFilterSection2.setType(FilterSectionType.ShowVideoTalks);
        singleFilterSection2.setName(FilterSectionType.ShowVideoTalks.toString());
        scheduleFilter.getFilterSections().add(singleFilterSection2);

        view.showSummitTypes(summitTypes);
        view.showTrackGroups(trackGroups);
        view.showLevels(levels);
        view.showVenues(venues);
        view.showShowPastTalks(false);
        view.showVenuesFilter(false);
    }

    @Override
    public void buildSummitTypeFilterItem(GeneralScheduleFilterItemView item, int position) {
        AbstractFilterSection filterSection = scheduleFilter.getFilterSectionByName(FilterSectionType.SummitType.toString());
        buildFilterItem(item, Color.WHITE, Color.RED, false, (MultiFilterSection) filterSection, position);
    }

    @Override
    public void toggleSelectionSummitType(IGeneralScheduleFilterItemView item, int position) {
        AbstractFilterSection filterSection = scheduleFilter.getFilterSectionByName(FilterSectionType.SummitType.toString());
        toggleSelection(item, Color.WHITE, Color.LTGRAY, (MultiFilterSection) filterSection, position);
    }

    @Override
    public void toggleHidePastTalks(boolean hidePastTalks) {
        SingleFilterSelection filterSection = (SingleFilterSelection) scheduleFilter.getFilterSectionByName(FilterSectionType.HidePastTalks.toString());;
        filterSection.setValue(hidePastTalks);
        scheduleFilter.getSelections().get(FilterSectionType.HidePastTalks).clear();
        if (hidePastTalks)
            scheduleFilter.getSelections().get(FilterSectionType.HidePastTalks).add(hidePastTalks);
    }

    @Override
    public void toggleShowVideoTalks(boolean showVideoTalks) {
        SingleFilterSelection filterSection = (SingleFilterSelection) scheduleFilter.getFilterSectionByName(FilterSectionType.ShowVideoTalks.toString());;
        filterSection.setValue(showVideoTalks);
        scheduleFilter.getSelections().get(FilterSectionType.ShowVideoTalks).clear();
        if (showVideoTalks)
            scheduleFilter.getSelections().get(FilterSectionType.ShowVideoTalks).add(showVideoTalks);
    }

    @Override
    public void buildEventTypeFilterItem(GeneralScheduleFilterItemView item, int position) {
        AbstractFilterSection filterSection = scheduleFilter.getFilterSectionByName(FilterSectionType.EventType.toString());
        buildFilterItem(item, Color.WHITE, Color.RED, false, (MultiFilterSection) filterSection, position);
    }

    @Override
    public void buildLevelFilterItem(GeneralScheduleFilterItemView item, int position) {
        MultiFilterSection filterSection = (MultiFilterSection) scheduleFilter.getFilterSectionByName(FilterSectionType.Level.toString());
        FilterSectionItem filterItem = filterSection.getItems().get(position);

        item.setText(filterItem.getName());
        item.setIsSelected(isItemSelected(filterSection.getType(), filterItem.getName()));
        item.setShowCircle(false);
    }

    @Override
    public void buildTrackGroupFilterItem(GeneralScheduleFilterItemView item, int position) {
        AbstractFilterSection filterSection   = scheduleFilter.getFilterSectionByName(FilterSectionType.TrackGroup.toString());
        TrackGroupDTO trackGroupDTO           = trackGroups.get(position);
        List<Integer> filtersOnTracks         = (List<Integer>)(List<?>) scheduleFilter.getSelections().get(FilterSectionType.Tracks);
        List<TrackDTO> tracksBelongingToGroup = interactor.getTracksBelongingToGroup(trackGroupDTO.getId(), filtersOnTracks);

        buildFilterItem(
                item,
                Color.parseColor(trackGroupDTO.getColor()),
                Color.parseColor(trackGroupDTO.getColor()),
                interactor.groupIncludesAnyOfGivenTracks(trackGroupDTO.getId(), filtersOnTracks),
                (MultiFilterSection) filterSection,
                position);

        if(item instanceof IGeneralScheduleFilterItemNavigationView){
            List<String> subItems = new ArrayList<>();
            for (TrackDTO track:tracksBelongingToGroup) {
                subItems.add(track.getName());
            }
            ((IGeneralScheduleFilterItemNavigationView)item).setSelectedSubItemsText(subItems);
        }
    }

    @Override
    public void buildVenueFilterItem(GeneralScheduleFilterItemView item, int position) {
        AbstractFilterSection filterSection = scheduleFilter.getFilterSectionByName(FilterSectionType.Venues.toString());
        NamedDTO venueDTO                   = venues.get(position);
        List<Integer> filtersOnRooms        = (List<Integer>)(List<?>) scheduleFilter.getSelections().get(FilterSectionType.Rooms  );
        buildFilterItem
        (
                item,
                Color.WHITE,
                Color.RED,
                interactor.VenueIncludesAnyOfGivenRooms(venueDTO.getId(), filtersOnRooms),
                (MultiFilterSection) filterSection, position);
        List<NamedDTO> roomsBelongingToVenue = interactor.getRoomsBelongingToVenue(venueDTO.getId(), filtersOnRooms);
        if(item instanceof IGeneralScheduleFilterItemNavigationView){
            List<String> subItems = new ArrayList<>();
            for (NamedDTO room:roomsBelongingToVenue) {
                subItems.add(room.getName());
            }
            ((IGeneralScheduleFilterItemNavigationView)item).setSelectedSubItemsText(subItems);
        }
    }

    @Override
    public void toggleSelectionEventType(IGeneralScheduleFilterItemView item, int position) {
        AbstractFilterSection filterSection = scheduleFilter.getFilterSectionByName(FilterSectionType.EventType.toString());
        toggleSelection(item, Color.WHITE, Color.LTGRAY, (MultiFilterSection) filterSection, position);
    }

    @Override
    public void toggleSelectionLevel(IGeneralScheduleFilterItemView item, int position) {
        AbstractFilterSection filterSection = scheduleFilter.getFilterSectionByName(FilterSectionType.Level.toString());
        FilterSectionItem filterItem = ((MultiFilterSection) filterSection).getItems().get(position);

        if (isItemSelected(filterSection.getType(), filterItem.getName())) {
            int filterItemPosition = 0;
            boolean found = false;
            String level;
            while (!found) {
                level = (String) scheduleFilter.getSelections().get(filterSection.getType()).get(filterItemPosition);
                if (level.equals(filterItem.getName())) {
                    found = true;
                } else {
                    filterItemPosition++;
                }
            }

            scheduleFilter.getSelections().get(filterSection.getType()).remove(filterItemPosition);
            item.setIsSelected(false);
        } else {
            scheduleFilter.getSelections().get(filterSection.getType()).add(filterItem.getName());
            item.setIsSelected(true);
        }
    }

    @Override
    public void toggleSelectionTrackGroup(IGeneralScheduleFilterItemView item, int position) {
        // navigate
        this.wireframe.presentGeneralScheduleFilterTrackGroupView(this.view, trackGroups.get(position));
    }

    @Override
    public void toggleSelectionVenue(IGeneralScheduleFilterItemView item, int position) {
        this.wireframe.presentGeneralScheduleFilterVenuesGroupView(this.view, venues.get(position));
    }

    @Override
    public void onResume() {
        super.onResume();
        List<Boolean> filtersOnShowVideoTalks = (List<Boolean>) (List<?>) scheduleFilter.getSelections().get(FilterSectionType.ShowVideoTalks);
        boolean showVideoTalks = filtersOnShowVideoTalks != null && !filtersOnShowVideoTalks.isEmpty() ? filtersOnShowVideoTalks.get(0) : false;
        view.toggleShowVideoTalks(showVideoTalks);
        SummitDTO activeSummit = interactor.getActiveSummit();
        if (activeSummit != null ){

            if(activeSummit.isCurrentDateTimeInsideSummitRange()) {
                view.showShowPastTalks(true);
                List<Boolean> filtersOnPassTalks = (List<Boolean>) (List<?>) scheduleFilter.getSelections().get(FilterSectionType.HidePastTalks);
                boolean hidePastTalks = filtersOnPassTalks != null && !filtersOnPassTalks.isEmpty() ? filtersOnPassTalks.get(0) : false;
                view.toggleShowPastTalks(hidePastTalks);
            }
            if(activeSummit.shouldShowVenues()){
                view.showVenuesFilter(true);
            }
        }

    }

}