package org.openstack.android.summit.modules.general_schedule_filter.user_interface;

import android.graphics.Color;
import android.os.Bundle;

import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.DTOs.SummitDTO;
import org.openstack.android.summit.common.DTOs.TrackGroupDTO;
import org.openstack.android.summit.common.IScheduleFilter;
import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.modules.general_schedule_filter.IGeneralScheduleFilterWireframe;
import org.openstack.android.summit.modules.general_schedule_filter.business_logic.IGeneralScheduleFilterInteractor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Claudio Redi on 2/1/2016.
 */
public class GeneralScheduleFilterPresenter
        extends BasePresenter<IGeneralScheduleFilterView, IGeneralScheduleFilterInteractor, IGeneralScheduleFilterWireframe>
        implements IGeneralScheduleFilterPresenter {

    private IScheduleFilter     scheduleFilter;
    private List<NamedDTO>      summitTypes;
    private List<NamedDTO>      eventTypes;
    private List<String>        levels;
    private List<TrackGroupDTO> trackGroups;
    private List<String>        tags;

    private final String KEY_SELECTED_TAGS = "KEY_SELECTED_TAGS";

    public GeneralScheduleFilterPresenter(IGeneralScheduleFilterInteractor interactor, IGeneralScheduleFilterWireframe wireframe, IScheduleFilter scheduleFilter) {
        super(interactor, wireframe);
        this.scheduleFilter = scheduleFilter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){

    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        summitTypes = interactor.getSummitTypes();
        eventTypes  = interactor.getEventTypes();
        levels      = interactor.getLevels();
        trackGroups = interactor.getTrackGroups();
        tags        = interactor.getTags();

        scheduleFilter.getFilterSections().clear();

        MultiFilterSection filterSection = new MultiFilterSection();
        filterSection.setType(FilterSectionType.SummitType);
        filterSection.setName("Summit Type");
        FilterSectionItem filterSectionItem;
        for (NamedDTO summitType : summitTypes) {
            filterSectionItem = createSectionItem(summitType.getId(), summitType.getName(), filterSection.getType());
            filterSection.getItems().add(filterSectionItem);
        }
        scheduleFilter.getFilterSections().add(filterSection);

        filterSection = new MultiFilterSection();
        filterSection.setType(FilterSectionType.EventType);
        filterSection.setName("Event Type");
        for (NamedDTO eventType : eventTypes) {
            filterSectionItem = createSectionItem(eventType.getId(), eventType.getName(), filterSection.getType());
            filterSection.getItems().add(filterSectionItem);
        }

        scheduleFilter.getFilterSections().add(filterSection);

        filterSection = new MultiFilterSection();
        filterSection.setType(FilterSectionType.Level);
        filterSection.setName("Levels");
        for (String level : levels) {
            filterSectionItem = createSectionItem(0, level, filterSection.getType());
            filterSection.getItems().add(filterSectionItem);
        }
        scheduleFilter.getFilterSections().add(filterSection);

        filterSection = new MultiFilterSection();
        filterSection.setType(FilterSectionType.TrackGroup);
        filterSection.setName("Track Groups");
        for (NamedDTO trackGroup : trackGroups) {
            filterSectionItem = createSectionItem(trackGroup.getId(), trackGroup.getName(), filterSection.getType());
            filterSection.getItems().add(filterSectionItem);
        }

        scheduleFilter.getFilterSections().add(filterSection);

        SingleFilterSelection singleFilterSection = new SingleFilterSelection(true);
        singleFilterSection.setType(FilterSectionType.TrackGroup);
        singleFilterSection.setName("Hide Past Talks");
        scheduleFilter.getFilterSections().add(singleFilterSection);

        view.showEventTypes(eventTypes);
        view.showTrackGroups(trackGroups);
        view.showLevels(levels);
        view.bindTags(tags);
        view.showShowPastTalks(false);

        if(scheduleFilter.isTypeSet(FilterSectionType.HidePastTalks)){
            view.showShowPastTalks(true);
            List<Boolean> filtersOnPassTalks = (List<Boolean>)(List<?>) scheduleFilter.getSelections().get(FilterSectionType.HidePastTalks);
            boolean hidePastTalks            = filtersOnPassTalks != null && !filtersOnPassTalks.isEmpty()? filtersOnPassTalks.get(0) : false;
            view.toggleShowPastTalks(hidePastTalks);
        }

        for (Object tag: scheduleFilter.getSelections().get(FilterSectionType.Tag)){
            view.addTag((String)tag);
        }
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

    private void buildFilterItem(IGeneralScheduleFilterItemView item, int selectedColor, int unselectedColor, boolean showCircle, MultiFilterSection filterSection, int position) {
        FilterSectionItem filterItem = filterSection.getItems().get(position);
        boolean isItemSelected = isItemSelected(filterSection.getType(), filterItem.getId());
        item.setText(filterItem.getName());
        item.setIsSelected(isItemSelected);
        item.setCircleColor(isItemSelected ? selectedColor : unselectedColor);
        item.setShowCircle(showCircle);
    }

    @Override
    public void buildEventTypeFilterItem(GeneralScheduleFilterItemView item, int position) {
        AbstractFilterSection filterSection = scheduleFilter.getFilterSections().get(1);
        buildFilterItem(item,Color.WHITE, Color.RED, false, (MultiFilterSection)filterSection, position);
    }

    @Override
    public void buildLevelFilterItem(GeneralScheduleFilterItemView item, int position) {
        MultiFilterSection filterSection = (MultiFilterSection)scheduleFilter.getFilterSections().get(2);
        FilterSectionItem filterItem     = filterSection.getItems().get(position);

        item.setText(filterItem.getName());
        item.setIsSelected(isItemSelected(filterSection.getType(), filterItem.getName()));
        item.setShowCircle(false);
    }

    @Override
    public void buildTrackGroupFilterItem(GeneralScheduleFilterItemView item, int position) {
        AbstractFilterSection filterSection = scheduleFilter.getFilterSections().get(3);
        TrackGroupDTO trackGroupDTO         = trackGroups.get(position);
        buildFilterItem(
                item,
                Color.parseColor(trackGroupDTO.getColor()),
                Color.parseColor(trackGroupDTO.getColor()),
                true,
                (MultiFilterSection)filterSection,
                position);
    }


    @Override
    public void toggleSelectionEventType(IGeneralScheduleFilterItemView item, int position) {
        AbstractFilterSection filterSection = scheduleFilter.getFilterSections().get(1);
        toggleSelection(item, Color.WHITE, Color.LTGRAY, (MultiFilterSection)filterSection, position);
    }
    
    @Override
    public void toggleSelectionLevel(IGeneralScheduleFilterItemView item, int position) {
        AbstractFilterSection filterSection = scheduleFilter.getFilterSections().get(2);
        FilterSectionItem filterItem       = ((MultiFilterSection)filterSection).getItems().get(position);

        if (isItemSelected(filterSection.getType(), filterItem.getName())) {
            int filterItemPosition = 0;
            boolean found = false;
            String level;
            while(!found) {
                level = (String)scheduleFilter.getSelections().get(filterSection.getType()).get(filterItemPosition);
                if (level.equals(filterItem.getName())) {
                    found = true;
                }
                else {
                    filterItemPosition++;
                }
            }

            scheduleFilter.getSelections().get(filterSection.getType()).remove(filterItemPosition);
            item.setIsSelected(false);
        }
        else {
            scheduleFilter.getSelections().get(filterSection.getType()).add(filterItem.getName());
            item.setIsSelected(true);
        }
    }

    @Override
    public void toggleSelectionTrackGroup(IGeneralScheduleFilterItemView item, int position) {
        AbstractFilterSection filterSection = scheduleFilter.getFilterSections().get(3);
        TrackGroupDTO trackGroupDTO         = trackGroups.get(position);
        toggleSelection(
                item,
                Color.parseColor(trackGroupDTO.getColor()),
                Color.parseColor(trackGroupDTO.getColor()),
                (MultiFilterSection)filterSection,
                position
        );
    }

    @Override
    public void addTag(String tag) {
        if (tag == "" || scheduleFilter.getSelections().get(FilterSectionType.Tag).contains(tag)) {
            return;
        }

        scheduleFilter.getSelections().get(FilterSectionType.Tag).add(tag);
        view.addTag(tag);
    }

    @Override
    public void removeTag(String tagToDelete) {
        int filterItemPosition = 0;
        boolean found = false;
        String tag = "";
        while(!found) {
            tag = (String)scheduleFilter.getSelections().get(FilterSectionType.Tag).get(filterItemPosition);
            if (tag == tagToDelete) {
                found = true;
            }
            else {
                filterItemPosition++;
            }
        }

        scheduleFilter.getSelections().get(FilterSectionType.Tag).remove(filterItemPosition);
    }

    @Override
    public void toggleHidePastTalks(boolean hidePastTalks) {
        SingleFilterSelection filterSection = (SingleFilterSelection)scheduleFilter.getFilterSections().get(4);
        filterSection.setValue(hidePastTalks);
        scheduleFilter.getSelections().get(FilterSectionType.HidePastTalks).clear();
        if(hidePastTalks)
            scheduleFilter.getSelections().get(FilterSectionType.HidePastTalks).add(hidePastTalks);
    }

    public void toggleSelection(IGeneralScheduleFilterItemView item, int selectedColor, int unselectedColor, MultiFilterSection filterSection, int position){
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
        }
        else {
            scheduleFilter.getSelections().get(filterSection.getType()).add(filterItem.getId());
            item.setIsSelected(true);
        }
    }
}