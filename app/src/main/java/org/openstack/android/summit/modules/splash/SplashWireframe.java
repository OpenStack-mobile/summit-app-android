package org.openstack.android.summit.modules.splash;

import android.content.Context;
import android.content.Intent;

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
        Intent intent = new Intent((Context) context, SummitsListDataLoaderActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        context.startActivityForResult(intent, ISplashView.SUMMITS_LIST_DATA_LOAD_REQUEST);
    }

    @Override
    public void showSummitDataLoadingActivity(IBaseView context) {
        Intent intent = new Intent((Context) context, SummitDataLoadingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        context.startActivityForResult(intent, ISplashView.DATA_LOAD_REQUEST);
    }
}
