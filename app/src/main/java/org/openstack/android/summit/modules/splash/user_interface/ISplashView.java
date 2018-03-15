package org.openstack.android.summit.modules.splash.user_interface;

import android.widget.Button;

import org.openstack.android.summit.common.user_interface.IBaseView;

/**
 * Created by smarcet on 2/6/17.
 */

public interface ISplashView extends IBaseView {

    static final int DATA_LOAD_REQUEST              = 0XFF99;
    static final int SUMMITS_LIST_DATA_LOAD_REQUEST = 0XFF98;

    void setLoginButtonVisibility(boolean visible);

    void setGuestButtonVisibility(boolean visible);

    void setSummitName(String summitName);

    void setSummitDates(String summitDates);

    void setSummitInfoContainerVisibility(boolean visible);

    void setSummitDaysLeftContainerVisibility(boolean visible);

    void setSummitDay1(String day1);

    void setSummitDay2(String day2);

    void setSummitDay3(String day3);

    void setSummitCurrentDayContainerVisibility(boolean visible);

    void setSummitCurrentDay(String day);
}
