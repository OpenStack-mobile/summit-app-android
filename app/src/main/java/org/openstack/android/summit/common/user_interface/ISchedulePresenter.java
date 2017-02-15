package org.openstack.android.summit.common.user_interface;

/**
 * Created by Claudio Redi on 12/28/2015.
 */
public interface ISchedulePresenter<V extends IScheduleView> extends IBasePresenter<V> {

    void buildItem(IScheduleItemView scheduleItemView, int position);

    void reloadSchedule();

    void toggleScheduleStatus(IScheduleItemView scheduleItemView, int position);

    void showEventDetail(int position);

    void removeItem(int position);

    void setHidePastTalks(boolean hidePastTalks);

}

