package org.openstack.android.summit;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.services.SummitsListIngestionService;
import org.openstack.android.summit.dagger.components.ApplicationComponent;
import org.openstack.android.summit.modules.main.user_interface.IDataLoadingPresenter;
import org.openstack.android.summit.modules.main.user_interface.IDataLoadingView;

import javax.inject.Inject;
import javax.inject.Named;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressPie;

/**
 * Created by smarcet on 12/5/16.
 */

public class SummitsListDataLoaderActivity extends Activity implements IDataLoadingView {

    private ACProgressPie progressDialog;
    public static final int RESULT_OK_FIRE_SUMMIT_DATA_LOADING = 0XFF57;

    @Inject
    @Named("SummitListDataLoading")
    IDataLoadingPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApplicationComponent().inject(this);
        setFinishOnTouchOutside(false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_initial_data_loading_activity);
        presenter.setView(this);
        Button retryButton = (Button) this.findViewById(R.id.initial_data_loading_retry_button);

        retryButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.retryButtonPressed();
                    }
                }
        );

        Button doLoadButton = (Button) this.findViewById(R.id.initial_data_loading_do_load);
        doLoadButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.loadNewDataButtonPressed();
                    }
                }
        );

        Button doCancel = (Button) this.findViewById(R.id.initial_data_loading_do_cancel);
        doCancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    presenter.cancelLoadNewDataButtonPressed();
                    }
                }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(SummitsListIngestionService.ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(serviceReceiver, filter);
        doDataLoading();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(serviceReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideActivityIndicator();
        showErrorContainer(false);
        Log.d(Constants.LOG_TAG, "SummitsListDataLoaderActivity.onDestroy");
    }

    @Override
    public void doDataLoading() {
        Log.d(Constants.LOG_TAG, "SummitsListDataLoaderActivity.doInitialDataLoading");
        showActivityIndicator();
        showErrorContainer(false);

        if(SummitsListIngestionService.isRunning()) return;

        Log.d(Constants.LOG_TAG, "SummitsListDataLoaderActivity.doInitialDataLoading: invoking service SummitsListIngestionService ");
        Intent intent = SummitsListIngestionService.newIntent(this);
        startService(intent);
    }

    private BroadcastReceiver serviceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra("res", RESULT_CANCELED);

            switch (resultCode){
                case SummitsListIngestionService.RESULT_CODE_OK:
                    Log.d(Constants.LOG_TAG, "SummitsListDataLoaderActivity.onActivityResult: SummitDataIngestionService.RESULT_CODE_OK.");
                    hideActivityIndicator();
                    finishOk();
                    break;
                case SummitsListIngestionService.RESULT_CODE_OK_INITIAL_LOADING:
                    Log.d(Constants.LOG_TAG, "SummitsListDataLoaderActivity.onActivityResult: SummitDataIngestionService.RESULT_CODE_OK_MUST_READ_SUMMIT_DATA.");
                    hideActivityIndicator();
                    finishOkWithNewData();
                    break;
                case SummitsListIngestionService.RESULT_CODE_OK_NEW_SUMMIT_AVAILABLE_LOADING:
                    Log.d(Constants.LOG_TAG, "SummitsListDataLoaderActivity.onActivityResult: SummitDataIngestionService.RESULT_CODE_OK_MUST_READ_SUMMIT_DATA.");
                    presenter.newDataAvailable();
                    break;
                case SummitsListIngestionService.RESULT_CODE_ERROR:
                    Log.d(Constants.LOG_TAG, "SummitsListDataLoaderActivity.onActivityResult: SummitDataIngestionService.RESULT_CODE_ERROR ");
                    presenter.onFailedInitialLoad();
                    break;
            }
        }
    };

    private void showQuestionContainer(boolean show) {
        LinearLayout container = (LinearLayout) this.findViewById(R.id.initial_data_loading_question);
        container.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public ApplicationComponent getApplicationComponent() {
        return ((OpenStackSummitApplication) getApplication()).getApplicationComponent();
    }

    private Context getActivity() {
        return this;
    }

    @Override
    public void showActivityIndicator(int delay) {
        showActivityIndicator();
    }

    @Override
    public void showActivityIndicator() {
        hideActivityIndicator();
        progressDialog = new ACProgressPie.Builder(this)
                .ringColor(Color.WHITE)
                .pieColor(Color.WHITE)
                .updateType(ACProgressConstant.PIE_AUTO_UPDATE)
                .build();
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void hideActivityIndicator() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    public void showErrorMessage(String message) {
        throw new UnsupportedOperationException();
    }

    @Override
    public FragmentManager getSupportFragmentManager() {
        return null;
    }

    @Override
    public void setTitle(String title) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void showInfoMessage(String message) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void showErrorContainer(boolean show) {
        LinearLayout container = (LinearLayout) this.findViewById(R.id.initial_data_loading_no_conectivity);
        container.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void askToLoadNewData() {
        this.showQuestionContainer(true);
    }

    @Override
    public void finishOk() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void finishOkWithNewData() {
        Intent intent2 = new Intent();
        setResult(RESULT_OK_FIRE_SUMMIT_DATA_LOADING, intent2);
        finish();
    }

}
