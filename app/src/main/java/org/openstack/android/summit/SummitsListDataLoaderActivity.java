package org.openstack.android.summit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.services.SummitsListIngestionService;
import org.openstack.android.summit.common.user_interface.BaseDataLoadingActivity;
import org.openstack.android.summit.modules.main.user_interface.IDataLoadingPresenter;
import org.openstack.android.summit.modules.main.user_interface.IDataLoadingView;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by smarcet on 12/5/16.
 */

public class SummitsListDataLoaderActivity extends BaseDataLoadingActivity implements IDataLoadingView {

    public static final int RESULT_OK_FIRE_SUMMIT_DATA_LOADING = 0XFF57;

    @Inject
    @Named("SummitListDataLoading")
    IDataLoadingPresenter presenter;

    @BindView(R.id.initial_data_loading_do_load)
    Button doLoadButton;

    @BindView(R.id.initial_data_loading_do_cancel)
    Button doCancel;

    @BindView(R.id.initial_data_loading_question)
    LinearLayout initialDataLoadingQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApplicationComponent().inject(this);
        setFinishOnTouchOutside(false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_initial_data_loading_activity);
        unbinder = ButterKnife.bind(this);
        presenter.setView(this);

        retryButton.setOnClickListener(
                v -> presenter.retryButtonPressed()
        );

        doLoadButton.setOnClickListener(
                v -> presenter.loadNewDataButtonPressed()
        );

        doCancel.setOnClickListener(
                v -> presenter.cancelLoadNewDataButtonPressed()
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
        if(initialDataLoadingQuestion == null ) return;
        initialDataLoadingQuestion.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showErrorContainer(boolean show) {

        if(initialDataLoginNoConnectivity == null) return;
        initialDataLoginNoConnectivity.setVisibility(show ? View.VISIBLE : View.GONE);
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
