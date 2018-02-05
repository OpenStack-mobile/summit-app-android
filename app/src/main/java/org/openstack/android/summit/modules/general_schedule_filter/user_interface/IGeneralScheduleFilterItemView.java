package org.openstack.android.summit.modules.general_schedule_filter.user_interface;

/**
 * Created by Claudio Redi on 2/3/2016.
 */
public interface IGeneralScheduleFilterItemView {

    void setText(String text);

    void setIsSelected(boolean isSelected);

    void setCircleColor(int color);

    void setShowCircle(boolean showCircle);
}
