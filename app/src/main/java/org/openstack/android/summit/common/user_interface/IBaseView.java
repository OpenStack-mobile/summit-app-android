package org.openstack.android.summit.common.user_interface;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.FragmentManager;

/**
 * Created by Claudio Redi on 1/7/2016.
 */
public interface IBaseView {

    void showActivityIndicator(int delay);

    void showActivityIndicator();

    void hideActivityIndicator();

    void showErrorMessage(String message);

    FragmentManager getSupportFragmentManager();

    void runOnUiThread(Runnable runnable);

    void setTitle(String title);

    Resources getResources();

    void showInfoMessage(String message);

    void startActivity(Intent intent);

}