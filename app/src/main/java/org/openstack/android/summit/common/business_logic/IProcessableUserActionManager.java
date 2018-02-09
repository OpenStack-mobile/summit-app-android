package org.openstack.android.summit.common.business_logic;

/**
 * Created by smarcet on 2/8/18.
 */

public interface IProcessableUserActionManager {

    void processMyScheduleProcessableUserActions();

    void processMyFavoritesProcessableUserActions();

    void processMyFeedbackProcessableUserActions();

    void processMyRSVPProcessableUserActions();
}
