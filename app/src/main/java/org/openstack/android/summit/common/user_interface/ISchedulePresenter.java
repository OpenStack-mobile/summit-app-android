package org.openstack.android.summit.common.user_interface;

/**
 * Created by Claudio Redi on 12/28/2015.
 */
public interface ISchedulePresenter<V extends IScheduleView> extends IScheduleListPresenter<V> {

    void reloadSchedule();

    void setHidePastTalks(boolean hidePastTalks);

}

