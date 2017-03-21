package org.openstack.android.summit.modules.member_profile.user_interface;

import org.openstack.android.summit.common.user_interface.IBasePresenter;

/**
 * Created by Claudio Redi on 1/26/2016.
 */
public interface IMemberProfilePresenter extends IBasePresenter<IMemberProfileView> {

    boolean getIsMyPofile();

    boolean getIsAttendee();

    boolean getIsSpeaker();

    boolean getIsMember();

    boolean showOrderConfirm();

    void onPageSelected(int newTabIndex);

}
