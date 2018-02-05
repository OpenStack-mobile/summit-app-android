package org.openstack.android.summit.common;

import org.openstack.android.summit.modules.general_schedule_filter.user_interface.AbstractFilterSection;
import org.openstack.android.summit.modules.general_schedule_filter.user_interface.MultiFilterSection;
import org.openstack.android.summit.modules.general_schedule_filter.user_interface.FilterSectionType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Claudio Redi on 2/2/2016.
 */
final public class ScheduleFilter implements IScheduleFilter {

    private HashMap<FilterSectionType, List<Object>> selections    = new HashMap<>();
    private List<AbstractFilterSection> filterSections             = new ArrayList<>();
    private HashMap<FilterSectionType, FilterSectionType> typesSet = new HashMap<>();

    public ScheduleFilter(){
        selections.put(FilterSectionType.SummitType, new ArrayList<>());
        selections.put(FilterSectionType.EventType, new ArrayList<>());
        selections.put(FilterSectionType.Level, new ArrayList<>());
        selections.put(FilterSectionType.TrackGroup, new ArrayList<>());
        selections.put(FilterSectionType.Tag, new ArrayList<>());
        selections.put(FilterSectionType.HidePastTalks, new ArrayList<>());
        selections.put(FilterSectionType.Venues, new ArrayList<>());
        selections.put(FilterSectionType.Tracks, new ArrayList<>());
    }

    @Override
    public boolean areAllSelectedForType(FilterSectionType type) {
        if (getFilterSections().size() == 0) {
            return false;
        }

        boolean found = false;
        int position = 0;
        AbstractFilterSection filterSection = null;
        while (!found && getFilterSections().size() < position) {
            filterSection = getFilterSections().get(position);
            if (filterSection.getType() == type) {
                found = true;
            }
            position++;
        }
        return MultiFilterSection.class.isInstance(filterSection)? ((MultiFilterSection)filterSection).getItems().size() == getSelections().get(type).size() : false;
    }

    @Override
    public HashMap<FilterSectionType, List<Object>> getSelections() {
        return selections;
    }

    @Override
    public void setSelections(HashMap<FilterSectionType, List<Object>> selections) {
        this.selections = selections;
    }

    @Override
    public List<AbstractFilterSection> getFilterSections() {
        return filterSections;
    }

    @Override
    public AbstractFilterSection getFilterSectionByName(String filterSectionName){
        for(AbstractFilterSection section: filterSections){
            if(section.getName().equals(filterSectionName))
                return section;
        }
        return null;
    }

    @Override
    public void removeFilterSectionByName(String filterSectionName) {
        AbstractFilterSection section = this.getFilterSectionByName(filterSectionName);
        if(section == null) return;
        filterSections.remove(section);
    }

    @Override
    public void setFilterSections(List<AbstractFilterSection> filterSections) {
        this.filterSections = filterSections;
    }

    @Override
    public boolean hasActiveFilters() {
        boolean hasActiveFilters = false;
        for (List<Object> values : selections.values()) {
            if (values.size() > 0) {
                hasActiveFilters = true;
                break;
            }
        }
        return hasActiveFilters;
    }

    @Override
    public void clearActiveFilters() {
        for (List<Object> values : selections.values()) {
            values.clear();
        }
    }

    @Override
    public boolean isTypeSet(FilterSectionType type) {
        return typesSet.containsKey(type);
    }

    @Override
    public void setTypeValues(FilterSectionType type, Object... values){
        for(Object value:values){
            getSelections().get(type).add(value);
        }
        typesSet.put(type, type);
    }

    @Override
    public void clearTypeValues(FilterSectionType type){
        if(!getSelections().containsKey(type)) return;
        getSelections().get(type).clear();
        typesSet.remove(type);
    }
}
