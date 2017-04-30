package org.openstack.android.summit.modules.splash;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;

import org.openstack.android.summit.SummitDataLoadingActivity;
import org.openstack.android.summit.SummitsListDataLoaderActivity;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.user_interface.IBaseView;
import org.openstack.android.summit.modules.main.user_interface.MainActivity;
import org.openstack.android.summit.modules.splash.user_interface.ISplashView;

/**
 * Created by smarcet on 2/6/17.
 */

public class SplashWireframe implements ISplashWireframe {

    @Override
    public void showMainActivity(IBaseView context, boolean doLogin) {
        Intent intent = new Intent((Context) context, MainActivity.class);
        intent.putExtra(Constants.START_EXTERNAL_LOGIN, doLogin);
        context.startActivity(intent);
        context.finish();
    }

    @Override
    public void showSummitListLoadingActivity(IBaseView context) {
        Log.d(Constants.LOG_TAG, "SplashWireframe: showSummitListLoadingActivity");
        Intent intent = new Intent((Context) context, SummitsListDataLoaderActivity.class);
        context.startActivityForResult(intent, ISplashView.SUMMITS_LIST_DATA_LOAD_REQUEST);
    }

    @Override
    public void showSummitDataLoadingActivity(IBaseView context) {
        Log.d(Constants.LOG_TAG, "SplashWireframe: showSummitDataLoadingActivity");
        Intent intent = new Intent((Context) context, SummitDataLoadingActivity.class);
        context.startActivityForResult(intent, ISplashView.DATA_LOAD_REQUEST);
    }

    @Override
    public void showNotification(IBaseView context, Uri pushNotificationDeepLink) {
        Intent openedIntent = new Intent(Constants.PUSH_NOTIFICATION_OPENED);
        LocalBroadcastManager.getInstance((Context) context).sendBroadcast(openedIntent);
        Intent activityIntent = new Intent(Intent.ACTION_VIEW, pushNotificationDeepLink);
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(activityIntent);
        context.finish();
    }
}
