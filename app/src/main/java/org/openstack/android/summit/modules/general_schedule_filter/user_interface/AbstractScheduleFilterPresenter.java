package org.openstack.android.summit.modules.general_schedule_filter.user_interface;

import org.openstack.android.summit.common.IScheduleFilter;
import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.common.user_interface.IBaseView;
import org.openstack.android.summit.modules.general_schedule_filter.IGeneralScheduleFilterWireframe;
import org.openstack.android.summit.modules.general_schedule_filter.business_logic.IGeneralScheduleFilterInteractor;

import java.util.List;

/**
 * Created by smarcet on 2/5/18.
 */

public abstract class AbstractScheduleFilterPresenter<V extends IBaseView>
        extends BasePresenter<V, IGeneralScheduleFilterInteractor, IGeneralScheduleFilterWireframe>

{
    public AbstractScheduleFilterPresenter
    (
            IGeneralScheduleFilterInteractor interactor,
            IGeneralScheduleFilterWireframe wireframe,
            IScheduleFilter scheduleFilter
    )
    {
        super(interactor, wireframe);
        this.scheduleFilter = scheduleFilter;
    }

    protected IScheduleFilter scheduleFilter;

    protected FilterSectionItem createSectionItem(int id, String name, FilterSectionType type) {
        FilterSectionItem filterSectionItem = new FilterSectionItem();
        filterSectionItem.setId(id);
        filterSectionItem.setName(name);
        return filterSectionItem;
    }

    protected boolean isItemSelected(FilterSectionType filterSectionType, int id) {
        List<Object> filterSelectionsForType = scheduleFilter.getSelections().get(filterSectionType);
        if (filterSelectionsForType != null) {
            for (Object selectedId : filterSelectionsForType) {
                if (id == (int) selectedId) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean isItemSelected(FilterSectionType filterSectionType, String name) {
        List<Object> filterSelectionsForType = scheduleFilter.getSelections().get(filterSectionType);
        if (filterSelectionsForType != null) {
            for (Object selectedName : filterSelectionsForType) {
                if (name.equals(selectedName)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected void buildFilterItem(IGeneralScheduleFilterItemView item, int selectedColor, int unselectedColor, boolean showCircle, MultiFilterSection filterSection, int position) {
        FilterSectionItem filterItem = filterSection.getItems().get(position);
        boolean isItemSelected = isItemSelected(filterSection.getType(), filterItem.getId());
        item.setText(filterItem.getName());
        item.setIsSelected(isItemSelected);
        item.setCircleColor(isItemSelected ? selectedColor : unselectedColor);
        item.setShowCircle(showCircle);
    }

    public void toggleSelection(IGeneralScheduleFilterItemView item, int selectedColor, int unselectedColor, MultiFilterSection filterSection, int position) {
        FilterSectionItem filterItem = filterSection.getItems().get(position);
        if (isItemSelected(filterSection.getType(), filterItem.getId())) {
            int filterItemPosition = 0;
            boolean found = false;
            int id;
            while (!found) {
                id = (int) scheduleFilter.getSelections().get(filterSection.getType()).get(filterItemPosition);
                if (id == filterItem.getId()) {
                    found = true;
                } else {
                    filterItemPosition++;
                }
            }

            scheduleFilter.getSelections().get(filterSection.getType()).remove(filterItemPosition);
            item.setIsSelected(false);
        } else {
            scheduleFilter.getSelections().get(filterSection.getType()).add(filterItem.getId());
            item.setIsSelected(true);
        }
    }


}
