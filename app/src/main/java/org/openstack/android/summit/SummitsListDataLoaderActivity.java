package org.openstack.android.summit;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.services.SummitDataIngestionService;
import org.openstack.android.summit.common.services.SummitsListIngestionService;
import org.openstack.android.summit.dagger.components.ApplicationComponent;
import org.openstack.android.summit.modules.main.user_interface.IDataLoadingPresenter;
import org.openstack.android.summit.modules.main.user_interface.IDataLoadingView;

import javax.inject.Inject;
import javax.inject.Named;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

/**
 * Created by smarcet on 12/5/16.
 */

public class SummitsListDataLoaderActivity extends Activity implements IDataLoadingView {

    private ACProgressFlower progressDialog;
    private static final int REQUEST_CODE                      = 0xFF56;
    public static final int RESULT_OK_FIRE_SUMMIT_DATA_LOADING = 0XFF57;
    private PendingIntent pending                              = null;


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
        doDataLoading();
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void doDataLoading() {
        Log.d(Constants.LOG_TAG, "SummitsListDataLoaderActivity.doInitialDataLoading");
        showActivityIndicator();
        showErrorContainer(false);

        if(SummitsListIngestionService.isRunning()) return;

        Log.d(Constants.LOG_TAG, "SummitsListDataLoaderActivity.doInitialDataLoading: invoking service SummitsListIngestionService ");
        Intent intent = SummitsListIngestionService.newIntent(this);
        pending       = createPendingResult(REQUEST_CODE, new Intent(), 0);
        intent.putExtra(SummitsListIngestionService.PENDING_RESULT, pending);
        startService(intent);
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

    @Override
    protected void onActivityResult(int req, int res, Intent data) {
        if (req == REQUEST_CODE) {
            switch (res){
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
        super.onActivityResult(req, res, data);
    }

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
        progressDialog = new ACProgressFlower.Builder(getActivity())
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("Please wait...")
                .fadeColor(Color.DKGRAY).build();
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
    public void onDestroy() {
        super.onDestroy();
        hideActivityIndicator();
        showErrorContainer(false);
        Log.d(Constants.LOG_TAG, "SummitsListDataLoaderActivity.onDestroy");
    }

}
