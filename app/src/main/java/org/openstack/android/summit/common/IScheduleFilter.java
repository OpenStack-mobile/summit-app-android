package org.openstack.android.summit.common;

import org.openstack.android.summit.modules.general_schedule_filter.user_interface.AbstractFilterSection;
import org.openstack.android.summit.modules.general_schedule_filter.user_interface.MultiFilterSection;
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

    List<AbstractFilterSection> getFilterSections();

    void setFilterSections(List<AbstractFilterSection> filterSections);

    boolean hasActiveFilters();

    void clearActiveFilters();

    boolean isTypeSet(FilterSectionType type);

    void setTypeValues(FilterSectionType type, Object ... values);

    void clearTypeValues(FilterSectionType type);
}
