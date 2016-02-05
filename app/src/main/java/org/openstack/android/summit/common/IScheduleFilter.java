package org.openstack.android.summit.common;

import org.openstack.android.summit.modules.general_schedule_filter.user_interface.FilterSection;
import org.openstack.android.summit.modules.general_schedule_filter.user_interface.FilterSectionType;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Claudio Redi on 2/2/2016.
 */
public interface IScheduleFilter {
    boolean areAllSelectedForType(FilterSectionType type);

    HashMap<FilterSectionType, List<Object>> getSelections();

    void setSelections(HashMap<FilterSectionType, List<Object>> selections);

    List<FilterSection> getFilterSections();

    void setFilterSections(List<FilterSection> filterSections);

    boolean hasActiveFilters();

    void clearActiveFilters();
}
