package org.openstack.android.summit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.SummitDTO;
import org.openstack.android.summit.common.business_logic.InteractorAsyncOperationListener;
import org.openstack.android.summit.dagger.components.ApplicationComponent;
import org.openstack.android.summit.modules.general_schedule.business_logic.IGeneralScheduleInteractor;

import javax.inject.Inject;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

/**
 * InitialDataLoadingActivity
 * Do the summit data initial loading
 */
public class InitialDataLoadingActivity extends Activity {

    private ACProgressFlower progressDialog;
    private int inProgress = 0;
    private final static String IN_PROGRESS_KEY = "InitialDataLoadingActivity.InProgress";
    @Inject
    IGeneralScheduleInteractor interactor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApplicationComponent().inject(this);

        if (savedInstanceState != null) {
            inProgress = savedInstanceState.getInt(IN_PROGRESS_KEY);
        }

        setFinishOnTouchOutside(false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_initial_data_loading_activity);
        doInitialDataLoading();
        Button retryButton = (Button) this.findViewById(R.id.initial_data_loading_retry_button);
        retryButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(Constants.LOG_TAG, "InitialDataLoadingActivity.retryButton.setOnClickListener");
                        doInitialDataLoading();
                    }
                }
        );
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(IN_PROGRESS_KEY, inProgress);
    }

    private void doInitialDataLoading() {
        showActivityIndicator();
        showErrorContainer(false);

        if (inProgress > 0) {
            Log.d(Constants.LOG_TAG, "InitialDataLoadingActivity.doInitialDataLoading: there is already another data loading process in place ...");
            return;
        }

        Log.d(Constants.LOG_TAG, "InitialDataLoadingActivity.doInitialDataLoading: doing initial data loading ...");
        inProgress = 1;
        InteractorAsyncOperationListener<SummitDTO> operationListener = new InteractorAsyncOperationListener<SummitDTO>() {
            @Override
            public void onSucceedWithData(SummitDTO summit) {
                inProgress = 0;
                hideActivityIndicator();
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                Log.d(Constants.LOG_TAG, "InitialDataLoadingActivity.doInitialDataLoading: data loading done...");
                finish();
            }

            @Override
            public void onError(String message) {
                inProgress = 0;
                Log.d(Constants.LOG_TAG, "InitialDataLoadingActivity.doInitialDataLoading: data loading error ...");

                onFailedInitialLoad(message);
            }
        };
        interactor.getActiveSummit(operationListener);
    }

    public ApplicationComponent getApplicationComponent() {
        return ((OpenStackSummitApplication) getApplication()).getApplicationComponent();
    }

    private Context getActivity() {
        return this;
    }

    private void showActivityIndicator() {
        progressDialog = new ACProgressFlower.Builder(getActivity())
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("Please wait...")
                .fadeColor(Color.DKGRAY).build();
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void hideActivityIndicator() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    protected void onFailedInitialLoad(String message) {
        hideActivityIndicator();
        showErrorContainer(true);
    }

    private void showErrorContainer(boolean show) {
        LinearLayout container = (LinearLayout) this.findViewById(R.id.initial_data_loading_no_conectivity);
        container.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideActivityIndicator();
        showErrorContainer(false);
        Log.d(Constants.LOG_TAG, "InitialDataLoadingActivity.onDestroy");
    }

}
