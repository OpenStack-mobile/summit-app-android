package org.openstack.android.summit.common.user_interface;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.utils.KeyboardHelper;
import org.openstack.android.summit.dagger.components.ApplicationComponent;

import butterknife.Unbinder;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressPie;

/**
 * Created by smarcet on 2/8/18.
 */

abstract public class BaseActivity extends AppCompatActivity implements IBaseView {

    protected ACProgressPie progressDialog;
    protected Unbinder unbinder;

    @Override
    public void hideKeyboard() {
        KeyboardHelper.hideKeyboard(getFragmentActivity());
    }

    @Override
    public void setTitle(String title) {
        super.setTitle(title);
    }


    @Override
    public void showActivityIndicator(int delay) {
        showActivityIndicator();
    }

    @Override
    public void showActivityIndicator() {

        runOnUiThread(() -> {
            try {
                if (progressDialog != null) {
                    hideActivityIndicator();
                }
                Log.d(Constants.LOG_TAG, "MainActivity.showActivityIndicator");
                progressDialog = new ACProgressPie.Builder(this)
                        .ringColor(Color.WHITE)
                        .pieColor(Color.WHITE)
                        .updateType(ACProgressConstant.PIE_AUTO_UPDATE)
                        .build();
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
            catch (Exception ex){
                Log.e(Constants.LOG_TAG, ex.getMessage());
                Crashlytics.logException(ex);
            }
        });
    }

    @Override
    public void hideActivityIndicator() {

        runOnUiThread(() -> {
            try {
                if (progressDialog != null && progressDialog.isShowing()) {
                    Log.d(Constants.LOG_TAG, "MainActivity.hideActivityIndicator");
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            }
            catch (Exception ex){
                Crashlytics.logException(ex);
            }
        });

    }

    @Override
    public FragmentActivity getFragmentActivity() {
        return this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(unbinder != null){
            unbinder.unbind();
            unbinder = null;
        }
    }

    @Override
    public ApplicationComponent getApplicationComponent() {
        return ((OpenStackSummitApplication) getApplication()).getApplicationComponent();
    }

}
