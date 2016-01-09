package org.openstack.android.summit.common.user_interface;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.widget.Toast;

import org.openstack.android.summit.MainActivity;
import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.dagger.components.ApplicationComponent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Claudio Redi on 11/3/2015.
 */
public abstract class BaseFragment extends Fragment implements IBaseFragment {
    private ScheduledFuture<?> activityIndicatorTask;
    private ACProgressFlower progressDialog;
    private Boolean showActivityIndicator;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Shows a {@link android.widget.Toast} message.
     *
     * @param message An string representing a message to be shown.
     */
    protected void showToastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Gets a component for dependency injection by its type.
     */
    @SuppressWarnings("unchecked")
    protected ApplicationComponent getComponent() {
        return ((MainActivity)getActivity()).getApplicationComponent();
    }

    @Override
    public void showActivityIndicator() {
        showActivityIndicator = true;
        Runnable task = new Runnable() {
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!showActivityIndicator) {
                            return;
                        }
                        progressDialog = new ACProgressFlower.Builder(getActivity())
                                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                                .themeColor(Color.WHITE)
                                .text("Please wait...")
                                .fadeColor(Color.DKGRAY).build();
                        progressDialog.show();
                    }
                });
            }
        };
        ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();
        activityIndicatorTask = worker.schedule(task, 500, TimeUnit.MILLISECONDS);
    }

    @Override
    public void hideActivityIndicator() {
        showActivityIndicator = false;

        if (progressDialog != null) {
            progressDialog.hide();
        }
    }

    @Override
    public void showErrorMessage(String message) {
        new SweetAlertDialog(this.getActivity(), SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText(message)
                .show();
    }
}