package org.openstack.android.summit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.Window;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.services.SummitDataIngestionService;
import org.openstack.android.summit.common.user_interface.BaseDataLoadingActivity;

import butterknife.ButterKnife;

/**
 * SummitDataLoadingActivity
 * Do the summit data initial loading
 */
public class SummitDataLoadingActivity extends BaseDataLoadingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApplicationComponent().inject(this);

        setFinishOnTouchOutside(false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_initial_data_loading_activity);

        unbinder = ButterKnife.bind(this);

        retryButton.setOnClickListener(
                v -> {
                    Log.d(Constants.LOG_TAG, "SummitDataLoadingActivity.retryButton.setOnClickListener");
                    doInitialDataLoading();
                }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(SummitDataIngestionService.ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(serviceReceiver, filter);
        doInitialDataLoading();
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
        Log.d(Constants.LOG_TAG, "SummitDataLoadingActivity.onDestroy");
    }

    private void doInitialDataLoading() {
        Log.d(Constants.LOG_TAG, "SummitDataLoadingActivity.doInitialDataLoading");
        showActivityIndicator();
        showErrorContainer(false);

        if(SummitDataIngestionService.isRunning()) return;

        Log.d(Constants.LOG_TAG, "SummitDataLoadingActivity.doInitialDataLoading: invoking service SummitDataIngestionService ");

        SummitDataIngestionService.enqueueWork(this, SummitDataIngestionService.newIntent(this));
    }

    public void finishOk() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    private BroadcastReceiver serviceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra("res", RESULT_CANCELED);

            switch (resultCode){
                case SummitDataIngestionService.RESULT_CODE_OK:
                    Log.d(Constants.LOG_TAG, "SummitDataLoadingActivity.onActivityResult: SummitDataIngestionService.RESULT_CODE_OK ");
                    hideActivityIndicator();
                    finishOk();
                    break;
                case SummitDataIngestionService.RESULT_CODE_ERROR:
                    Log.d(Constants.LOG_TAG, "SummitDataLoadingActivity.onActivityResult: SummitDataIngestionService.RESULT_CODE_ERROR ");
                    onFailedInitialLoad();
                    break;
            }
        }
    };

    protected void onFailedInitialLoad() {
        hideActivityIndicator();
        showErrorContainer(true);
    }

    private void showErrorContainer(boolean show) {
        if(initialDataLoginNoConnectivity == null) return;
        initialDataLoginNoConnectivity.setVisibility(show ? View.VISIBLE : View.GONE);
    }

}
