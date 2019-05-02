package org.openstack.android.summit.common.user_interface;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.modules.main.user_interface.MainActivity;
import org.openstack.android.summit.dagger.components.ApplicationComponent;

import java.lang.reflect.Field;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressPie;


/**
 * Created by Claudio Redi on 11/3/2015.
 */
public abstract class BaseFragment<P extends IBasePresenter> extends Fragment implements IBaseView {

    private ScheduledFuture<?> activityIndicatorTask;
    private ACProgressPie progressDialog;
    private boolean showActivityIndicator;
    private boolean isActivityIndicatorVisible;
    protected View view;

    @Inject
    protected P presenter;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.setView(this);
        presenter.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        presenter.onCreateView(savedInstanceState);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
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
        showActivityIndicator(500);
    }

    @Override
    public void showActivityIndicator(int delay) {
        if (isActivityIndicatorVisible || getActivity() == null) {
            return;
        }

        isActivityIndicatorVisible = true;

        showActivityIndicator = true;
        Runnable task = () -> getActivity().runOnUiThread(() -> {
            try {
                if (!showActivityIndicator) {
                    return;
                }
                progressDialog = new ACProgressPie.Builder(getActivity())
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
        ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();
        activityIndicatorTask = worker.schedule(task, delay, TimeUnit.MILLISECONDS);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideActivityIndicator();
    }

    @Override
    public void hideActivityIndicator() {
        if (getActivity() == null) {
            return;
        }
        getActivity().runOnUiThread(() -> {
            try {
                showActivityIndicator = false;
                isActivityIndicatorVisible = false;

                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            }
            catch (Exception ex){
                Log.e(Constants.LOG_TAG, ex.getMessage());
                Crashlytics.logException(ex);
            }
        });
    }

    @Override
    public FragmentManager getSupportFragmentManager() {
        return getActivity().getSupportFragmentManager();
    }

    @Override
    public void runOnUiThread(Runnable runnable) {
        getActivity().runOnUiThread(runnable);
    }

    @Override
    public void setTitle(String title) {
        getActivity().setTitle(title.toUpperCase());
    }

    @Override
    public void hideKeyboard() {
        ((BaseActivity)getActivity()).hideKeyboard();
    }

    @Override
    public Context getApplicationContext() {
        Context ctx = getContext();
        return ctx != null ? ctx.getApplicationContext() : null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            // todo: remove when we update to com.android.support:appcompat-v7:24.2.0
            // https://code.google.com/p/android/issues/detail?id=42601
            // http://johnfeng.github.io/blog/2015/05/31/fragment-activity-has-been-destoryed-problem/
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public View getView() {
        if(view != null) return view;
        return super.getView();
    }

    @Override
    public void finish(){
        getActivity().finish();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode){
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    public ContentResolver getContentResolver(){
        return getActivity().getContentResolver();
    }

    @Override
    public FragmentActivity getFragmentActivity(){
        return super.getActivity();
    }

    @Override
    public Intent getIntent() {
        return super.getActivity().getIntent();
    }

    @Override
    public ApplicationComponent getApplicationComponent() {
        return ((BaseActivity)getActivity()).getApplicationComponent();
    }

    @Override
    public ComponentName startService(Intent service){
        return ((BaseActivity)getActivity()).startService(service);
    }
}