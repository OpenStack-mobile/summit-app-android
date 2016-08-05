package org.openstack.android.summit.modules.general_schedule_filter.user_interface;

/**
 * Created by sebastian on 8/4/2016.
 */
public abstract class AbstractFilterSection {

    protected FilterSectionType type;
    protected String name;

    public FilterSectionType getType() {
        return type;
    }

    public void setType(FilterSectionType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
