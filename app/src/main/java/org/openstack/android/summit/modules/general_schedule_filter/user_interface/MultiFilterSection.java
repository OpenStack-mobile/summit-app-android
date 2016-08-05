package org.openstack.android.summit.modules.general_schedule_filter.user_interface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Claudio Redi on 2/2/2016.
 */
public class MultiFilterSection extends AbstractFilterSection{

    private List<FilterSectionItem> items = new ArrayList<>();

    public List<FilterSectionItem> getItems() {
        return items;
    }
    public void setItems(List<FilterSectionItem> items) {
        this.items = items;
    }
}