package org.openstack.android.summit.common.user_interface;

/**
 * Created by smarcet on 2/20/17.
 */

public interface IScheduleListPresenter<V extends IBaseView> extends IBasePresenter<V> {

    void buildItem(IScheduleItemView scheduleItemView, int position);

    void toggleScheduleStatus(IScheduleItemView scheduleItemView, int position);

    void toggleFavoriteStatus(IScheduleItemView scheduleItemView, int position);

    void showEventDetail(int position);
}
