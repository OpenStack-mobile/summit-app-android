package org.openstack.android.summit.modules.splash.user_interface;

import android.widget.Button;

import org.openstack.android.summit.common.user_interface.IBaseView;

/**
 * Created by smarcet on 2/6/17.
 */

public interface ISplashView extends IBaseView {

    static final int DATA_LOAD_REQUEST              = 3;
    static final int SUMMITS_LIST_DATA_LOAD_REQUEST = 4;

    void setLoginButtonVisibility(boolean visible);

    void setGuestButtonVisibility(boolean visible);

    void setSummitName(String summitName);

    void setSummitDates(String summitDates);

    void setSummitInfoContainerVisibility(boolean visible);
}
