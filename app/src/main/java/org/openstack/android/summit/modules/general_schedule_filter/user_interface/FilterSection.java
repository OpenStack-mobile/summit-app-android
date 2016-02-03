package org.openstack.android.summit.modules.general_schedule_filter.user_interface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Claudio Redi on 2/2/2016.
 */
public class FilterSection {
    private FilterSectionType type;
    private String name;
    private List<FilterSectionItem> items = new ArrayList<>();

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

    public List<FilterSectionItem> getItems() {
        return items;
    }

    public void setItems(List<FilterSectionItem> items) {
        this.items = items;
    }
}