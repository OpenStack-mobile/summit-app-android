package org.openstack.android.summit.modules.main.user_interface;

import android.content.Intent;
import android.net.Uri;
import org.openstack.android.summit.common.user_interface.IBaseView;

/**
 * Created by Claudio Redi on 2/12/2016.
 */
public interface IMainView extends IBaseView {

    public static final int DATA_LOAD_REQUEST              = 0xFF97;
    public static final int SUMMITS_LIST_DATA_LOAD_REQUEST = 0xFF96;

    void setLoginButtonText(String text);

    void setMemberName(String text);

    void setProfilePic(Uri uri);

    void toggleMyProfileMenuItem(boolean show);

    void toggleMenu(boolean show);

    Intent getIntent();

    void setIntent(Intent newIntent);

    void updateNotificationCounter(Long value);

    void startActivityForResult(Intent intent, int requestCode);

    void setMenuItemChecked(int menuItemId);

    void setMenuItemVisible(int menuItemId, boolean visible);

    void closeMenuDrawer();

    void setNavigationViewLogOutState();
}
