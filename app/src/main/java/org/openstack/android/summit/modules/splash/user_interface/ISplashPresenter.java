package org.openstack.android.summit.modules.splash.user_interface;

import android.content.Intent;
import android.view.View;

import org.openstack.android.summit.common.user_interface.IBasePresenter;

/**
 * Created by smarcet on 2/6/17.
 */

public interface ISplashPresenter extends IBasePresenter<ISplashView> {

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void loginClicked(View v);

    void guestClicked(View v);
}
