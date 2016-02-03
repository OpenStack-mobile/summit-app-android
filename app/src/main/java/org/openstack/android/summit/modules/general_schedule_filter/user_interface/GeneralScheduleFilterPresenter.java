package org.openstack.android.summit.modules.general_schedule_filter.user_interface;

import android.graphics.Color;
import android.os.Bundle;

import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.DTOs.TrackGroupDTO;
import org.openstack.android.summit.common.IScheduleFilter;
import org.openstack.android.summit.common.entities.TrackGroup;
import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.modules.general_schedule_filter.IGeneralScheduleFilterWireframe;
import org.openstack.android.summit.modules.general_schedule_filter.business_logic.IGeneralScheduleFilterInteractor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Claudio Redi on 2/1/2016.
 */
public class GeneralScheduleFilterPresenter extends BasePresenter<IGeneralScheduleFilterView, IGeneralScheduleFilterInteractor, IGeneralScheduleFilterWireframe> implements IGeneralScheduleFilterPresenter {
    private IScheduleFilter scheduleFilter;
    List<NamedDTO> summitTypes;
    List<NamedDTO> eventTypes;
    List<String> levels;
    List<TrackGroupDTO> trackGroups;

    public GeneralScheduleFilterPresenter(IGeneralScheduleFilterInteractor interactor, IGeneralScheduleFilterWireframe wireframe, IScheduleFilter scheduleFilter) {
        super(interactor, wireframe);
        this.scheduleFilter = scheduleFilter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        summitTypes = interactor.getSummitTypes();
        eventTypes = interactor.getEventTypes();
        levels = interactor.getLevels();
        trackGroups = interactor.getTrackGroups();

        if (scheduleFilter.getFilterSections().size() == 0) {

            scheduleFilter.getSelections().put(FilterSectionType.SummitType, new ArrayList<>());
            FilterSection filterSection = new FilterSection();
            filterSection.setType(FilterSectionType.SummitType);
            filterSection.setName("Summit Type");
            FilterSectionItem filterSectionItem;
            for (NamedDTO summitType : summitTypes) {
                filterSectionItem = createSectionItem(summitType.getId(), summitType.getName(), filterSection.getType());
                filterSection.getItems().add(filterSectionItem);
            }
            scheduleFilter.getFilterSections().add(filterSection);

            scheduleFilter.getSelections().put(FilterSectionType.EventType, new ArrayList<>());
            filterSection = new FilterSection();
            filterSection.setType(FilterSectionType.EventType);
            filterSection.setName("Event Type");
            for (NamedDTO eventType : eventTypes) {
                filterSectionItem = createSectionItem(eventType.getId(), eventType.getName(), filterSection.getType());
                filterSection.getItems().add(filterSectionItem);
            }
            scheduleFilter.getFilterSections().add(filterSection);

            scheduleFilter.getSelections().put(FilterSectionType.Level, new ArrayList<>());
            filterSection = new FilterSection();
            filterSection.setType(FilterSectionType.Level);
            filterSection.setName("Levels");
            for (String level : levels) {
                filterSectionItem = createSectionItem(0, level, filterSection.getType());
                filterSection.getItems().add(filterSectionItem);
            }
            scheduleFilter.getFilterSections().add(filterSection);

            scheduleFilter.getSelections().put(FilterSectionType.TrackGroup, new ArrayList<>());
            filterSection = new FilterSection();
            filterSection.setType(FilterSectionType.TrackGroup);
            filterSection.setName("Track Groups");
            for (NamedDTO trackGroup : trackGroups) {
                filterSectionItem = createSectionItem(trackGroup.getId(), trackGroup.getName(), filterSection.getType());
                filterSection.getItems().add(filterSectionItem);
            }
            scheduleFilter.getFilterSections().add(filterSection);

            scheduleFilter.getSelections().put(FilterSectionType.Tag, new ArrayList<>());
        }
        view.showSummitTypes(summitTypes);
        view.showEventTypes(eventTypes);
        view.showTrackGroups(trackGroups);
        view.showLevels(levels);
    }

    private FilterSectionItem createSectionItem(int id, String name, FilterSectionType type) {
        FilterSectionItem filterSectionItem = new FilterSectionItem();
        filterSectionItem.setId(id);
        filterSectionItem.setName(name);
        return filterSectionItem;
    }

    private boolean isItemSelected(FilterSectionType filterSectionType, int id) {
        List<Object> filterSelectionsForType = scheduleFilter.getSelections().get(filterSectionType);
        if (filterSelectionsForType != null) {
            for (Object selectedId: filterSelectionsForType) {
                if (id == (int)selectedId) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isItemSelected(FilterSectionType filterSectionType, String name) {
        List<Object> filterSelectionsForType = scheduleFilter.getSelections().get(filterSectionType);
        if (filterSelectionsForType != null) {
            for (Object selectedName: filterSelectionsForType) {
                if (name.equals(selectedName)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void buildFilterItem(IGeneralScheduleFilterItemView item, int selectedColor, int unselectedColor, FilterSection filterSection, int position) {
        FilterSectionItem filterItem = filterSection.getItems().get(position);
        boolean isItemSelected = isItemSelected(filterSection.getType(), filterItem.getId());
        item.setText(filterItem.getName());
        item.setIsSelected(isItemSelected);
        item.setColor(isItemSelected ? selectedColor : unselectedColor);
    }

    @Override
    public void buildSummitTypeFilterItem(GeneralScheduleFilterItemView item, int position) {
        FilterSection filterSection = scheduleFilter.getFilterSections().get(0);
        buildFilterItem(item, Color.WHITE, Color.LTGRAY, filterSection, position);
    }

    @Override
    public void buildEventTypeFilterItem(GeneralScheduleFilterItemView item, int position) {
        FilterSection filterSection = scheduleFilter.getFilterSections().get(1);
        buildFilterItem(item,Color.WHITE, Color.LTGRAY, filterSection, position);
    }

    @Override
    public void buildLevelFilterItem(GeneralScheduleFilterItemView item, int position) {
        FilterSection filterSection = scheduleFilter.getFilterSections().get(2);
        FilterSectionItem filterItem = filterSection.getItems().get(position);

        item.setText(filterItem.getName());
        item.setIsSelected(isItemSelected(filterSection.getType(), filterItem.getName()));
    }

    @Override
    public void buildTrackGroupFilterItem(GeneralScheduleFilterItemView item, int position) {
        FilterSection filterSection = scheduleFilter.getFilterSections().get(3);
        TrackGroupDTO trackGroupDTO = trackGroups.get(position);
        buildFilterItem(
                item,
                Color.parseColor(trackGroupDTO.getColor()),
                Color.parseColor(trackGroupDTO.getColor()),
                filterSection,
                position);
    }

    @Override
    public void toggleSelectionSummitType(IGeneralScheduleFilterItemView item, int position) {
        FilterSection filterSection = scheduleFilter.getFilterSections().get(0);
        toggleSelection(item, Color.WHITE, Color.LTGRAY, filterSection, position);
    }

    @Override
    public void toggleSelectionEventType(IGeneralScheduleFilterItemView item, int position) {
        FilterSection filterSection = scheduleFilter.getFilterSections().get(1);
        toggleSelection(item, Color.WHITE, Color.LTGRAY, filterSection, position);
    }
    
    @Override
    public void toggleSelectionLevel(IGeneralScheduleFilterItemView item, int position) {
        FilterSection filterSection = scheduleFilter.getFilterSections().get(2);
        FilterSectionItem filterItem = filterSection.getItems().get(position);

        if (isItemSelected(filterSection.getType(), filterItem.getName())) {
            int filterItemPosition = 0;
            boolean found = false;
            String level;
            while(!found) {
                level = (String)scheduleFilter.getSelections().get(filterSection.getType()).get(filterItemPosition);
                if (level == filterItem.getName()) {
                    found = true;
                }
                else {
                    filterItemPosition++;
                }
            }

            scheduleFilter.getSelections().get(filterSection.getType()).remove(filterItemPosition);
            item.setIsSelected(false);
            item.setColor(Color.LTGRAY);
        }
        else {
            scheduleFilter.getSelections().get(filterSection.getType()).add(filterItem.getName());
            item.setIsSelected(true);
            item.setColor(Color.WHITE);
        }
    }

    @Override
    public void toggleSelectionTrackGroup(IGeneralScheduleFilterItemView item, int position) {
        FilterSection filterSection = scheduleFilter.getFilterSections().get(3);
        TrackGroupDTO trackGroupDTO = trackGroups.get(position);
        toggleSelection(
                item,
                Color.parseColor(trackGroupDTO.getColor()),
                Color.parseColor(trackGroupDTO.getColor()),
                filterSection,
                position
        );
    }
    
    public void toggleSelection(IGeneralScheduleFilterItemView item, int selectedColor, int unselectedColor, FilterSection filterSection, int position){
        FilterSectionItem filterItem = filterSection.getItems().get(position);

        if (isItemSelected(filterSection.getType(), filterItem.getId())) {
            int filterItemPosition = 0;
            boolean found = false;
            int id;
            while(!found) {
                id = (int)scheduleFilter.getSelections().get(filterSection.getType()).get(filterItemPosition);
                if (id == filterItem.getId()) {
                    found = true;
                }
                else {
                    filterItemPosition++;
                }
            }

            scheduleFilter.getSelections().get(filterSection.getType()).remove(filterItemPosition);
            item.setIsSelected(false);
            item.setColor(unselectedColor);
        }
        else {
            scheduleFilter.getSelections().get(filterSection.getType()).add(filterItem.getId());
            item.setIsSelected(true);
            item.setColor(selectedColor);
        }
    }
}