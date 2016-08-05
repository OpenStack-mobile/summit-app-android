package org.openstack.android.summit.modules.main.user_interface;

import android.content.Intent;
import android.net.Uri;

import org.openstack.android.summit.common.user_interface.IBaseView;

/**
 * Created by Claudio Redi on 2/12/2016.
 */
public interface IMainView extends IBaseView {

    void setLoginButtonText(String text);

    void setMemberName(String text);

    void setProfilePic(Uri uri);

    void toggleMyProfileMenuItem(boolean show);

    void toggleMenuLogo(boolean show);

    void showInfoMessage(String message, String title);

    void showInfoMessage(String message);

    void toggleMenu(boolean show);

    Intent getIntent();

    void startActivityForResult(Intent intent, int requestCode);
}
