package org.openstack.android.summit.common.user_interface;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import org.openstack.android.summit.dagger.components.ApplicationComponent;

/**
 * Created by Claudio Redi on 1/7/2016.
 */
public interface IBaseView {

    void showActivityIndicator(int delay);

    void showActivityIndicator();

    void hideActivityIndicator();

    FragmentManager getSupportFragmentManager();

    void runOnUiThread(Runnable runnable);

    void setTitle(String title);

    Resources getResources();

    void startActivity(Intent intent);

    Context getApplicationContext();

    void finish();

    void startActivityForResult(Intent intent, int requestCode);

    ContentResolver getContentResolver();

    FragmentActivity getFragmentActivity();

    Intent getIntent();

    void hideKeyboard();

    ApplicationComponent getApplicationComponent();

    ComponentName startService(Intent service);
}