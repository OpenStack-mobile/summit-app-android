package org.openstack.android.summit.common;

import org.openstack.android.summit.modules.general_schedule_filter.user_interface.FilterSection;
import org.openstack.android.summit.modules.general_schedule_filter.user_interface.FilterSectionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Claudio Redi on 2/2/2016.
 */
public class ScheduleFilter implements IScheduleFilter {
    private HashMap<FilterSectionType, List<Object>> selections = new HashMap<>();
    private List<FilterSection> filterSections = new ArrayList<>();

    @Override
    public boolean areAllSelectedForType(FilterSectionType type) {
        if (getFilterSections().size() == 0) {
            return false;
        }

        boolean found = false;
        int position = 0;
        FilterSection filterSection = null;
        while (!found && getFilterSections().size() < position) {
            filterSection = getFilterSections().get(position);
            if (filterSection.getType() == type) {
                found = true;
            }
            position++;
        }
        return filterSection.getItems().size() == getSelections().get(type).size();
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
    public List<FilterSection> getFilterSections() {
        return filterSections;
    }

    @Override
    public void setFilterSections(List<FilterSection> filterSections) {
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
}
