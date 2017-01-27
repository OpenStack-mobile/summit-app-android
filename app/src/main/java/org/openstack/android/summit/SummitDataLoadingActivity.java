package org.openstack.android.summit;

import android.app.Activity;
import android.app.PendingIntent;
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
import org.openstack.android.summit.common.services.SummitDataIngestionService;
import org.openstack.android.summit.dagger.components.ApplicationComponent;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

/**
 * SummitDataLoadingActivity
 * Do the summit data initial loading
 */
public class SummitDataLoadingActivity extends Activity {

    private ACProgressFlower progressDialog;
    private static final int REQUEST_CODE = 0xFF45;
    private PendingIntent pending         = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApplicationComponent().inject(this);


        setFinishOnTouchOutside(false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_initial_data_loading_activity);
        doInitialDataLoading();
        Button retryButton = (Button) this.findViewById(R.id.initial_data_loading_retry_button);
        retryButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(Constants.LOG_TAG, "SummitDataLoadingActivity.retryButton.setOnClickListener");
                        doInitialDataLoading();
                    }
                }
        );
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void doInitialDataLoading() {
        Log.d(Constants.LOG_TAG, "SummitDataLoadingActivity.doInitialDataLoading");
        showActivityIndicator();
        showErrorContainer(false);

        if(SummitDataIngestionService.isRunning()) return;

        Log.d(Constants.LOG_TAG, "SummitDataLoadingActivity.doInitialDataLoading: invoking service SummitDataIngestionService ");
        Intent intent = SummitDataIngestionService.newIntent(this);
        pending       = createPendingResult(REQUEST_CODE, new Intent(), 0);
        intent.putExtra(SummitDataIngestionService.PENDING_RESULT, pending);
        startService(intent);
    }

    @Override
    protected void onActivityResult(int req, int res, Intent data) {
        if (req == REQUEST_CODE) {
            switch (res){
                case SummitDataIngestionService.RESULT_CODE_OK:
                    hideActivityIndicator();
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    Log.d(Constants.LOG_TAG, "SummitDataLoadingActivity.onActivityResult: SummitDataIngestionService.RESULT_CODE_OK.");
                    finish();
                    break;
                case SummitDataIngestionService.RESULT_CODE_ERROR:
                    Log.d(Constants.LOG_TAG, "SummitDataLoadingActivity.onActivityResult: SummitDataIngestionService.RESULT_CODE_ERROR ");
                    onFailedInitialLoad();
                    break;
            }
        }
        super.onActivityResult(req, res, data);
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

    protected void onFailedInitialLoad() {
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
        Log.d(Constants.LOG_TAG, "SummitDataLoadingActivity.onDestroy");
    }

}
