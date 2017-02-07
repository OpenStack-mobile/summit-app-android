package org.openstack.android.summit.modules.splash;

import org.openstack.android.summit.common.user_interface.IBaseView;

/**
 * Created by smarcet on 2/6/17.
 */

public interface ISplashWireframe {

    void showMainActivity(IBaseView context, boolean doLogin);

    void showSummitListLoadingActivity(IBaseView context);

    void showSummitDataLoadingActivity(IBaseView context);
}
