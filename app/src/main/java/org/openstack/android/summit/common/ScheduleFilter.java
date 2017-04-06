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

    private HashMap<FilterSectionType, List<Object>> selections   = new HashMap<>();
    private List<AbstractFilterSection> filterSections             = new ArrayList<>();
    private HashMap<FilterSectionType, FilterSectionType> typesSet = new HashMap<>();

    public ScheduleFilter(){
        getSelections().put(FilterSectionType.SummitType, new ArrayList<>());
        getSelections().put(FilterSectionType.EventType, new ArrayList<>());
        getSelections().put(FilterSectionType.Level, new ArrayList<>());
        getSelections().put(FilterSectionType.TrackGroup, new ArrayList<>());
        getSelections().put(FilterSectionType.Tag, new ArrayList<>());
        getSelections().put(FilterSectionType.HidePastTalks, new ArrayList<>());
        getSelections().put(FilterSectionType.Venues, new ArrayList<>());
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
        getSelections().get(type).clear();
        typesSet.remove(type);
    }
}
