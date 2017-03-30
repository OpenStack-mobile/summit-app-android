package org.openstack.android.summit.common.user_interface;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import org.openstack.android.summit.R;
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
            showActivityIndicator = false;
            isActivityIndicatorVisible = false;

            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        });
    }

    @Override
    public void showErrorMessage(String message) {
        if (getActivity() == null) {
            return;
        }

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getFragmentActivity());

        builder.setTitle(R.string.generic_error_title)
                .setMessage(R.string.generic_error_message)
                .setPositiveButton(R.string.generic_error_message_ok,  (dialog, id) -> dialog.dismiss() )
                .create()
                .show();
    }

    @Override
    public void showInfoMessage(String message) {
        if (getActivity() == null) {
            return;
        }

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getFragmentActivity());

        builder.setTitle("Info")
                .setMessage(message)
                .setPositiveButton(R.string.generic_error_message_ok,  (dialog, id) -> dialog.dismiss() )
                .create()
                .show();
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

    protected void hideKeyboard(Activity activity) {
        View v = activity.getWindow().getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
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
}