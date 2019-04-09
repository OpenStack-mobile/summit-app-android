package org.openstack.android.summit.modules.about.user_interface;

import org.openstack.android.summit.common.user_interface.IBasePresenter;
import org.openstack.android.summit.common.user_interface.WifiItemView;

/**
 * Created by Claudio Redi on 4/2/2016.
 */
public interface IAboutPresenter extends IBasePresenter<IAboutView> {

    void buildWifiListItem(WifiItemView wifiItemView, int position);

    void redirect2IssueTrackerPage();

    void redirect2SummitPage();

    void redirect2CodeConductPage();

    void sendInquireEmail();
}
