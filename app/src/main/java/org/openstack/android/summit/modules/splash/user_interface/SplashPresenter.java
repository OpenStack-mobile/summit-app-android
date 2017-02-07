package org.openstack.android.summit.modules.splash.user_interface;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import org.openstack.android.summit.SummitsListDataLoaderActivity;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.SummitDTO;
import org.openstack.android.summit.common.services.DataUpdatesService;
import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.modules.splash.ISplashWireframe;
import org.openstack.android.summit.modules.splash.business_logic.ISplashInteractor;

/**
 * Created by smarcet on 2/6/17.
 */

public class SplashPresenter extends BasePresenter<ISplashView, ISplashInteractor, ISplashWireframe>
        implements ISplashPresenter {

    public SplashPresenter(ISplashInteractor interactor, ISplashWireframe wireframe) {
        super(interactor, wireframe);
    }

    private boolean onDataLoading              = false;
    private boolean loadedSummitList           = false;
    private static final int SHOW_SPLASH_DELAY = 2000;

    public void showSummitInfo() {
        SummitDTO summit = interactor.getActiveSummit();
        if(summit != null){
            view.setSummitName(summit.getName());
            String dates = summit.getLocalStartDate().toString("MMM")
                    + " "   + summit.getLocalStartDate().toString("dd")+ "TH "
                    + " - " + summit.getLocalEndDate().toString("dd")+ "TH "
                    +  ", " + summit.getLocalStartDate().toString("yyyy");
            view.setSummitDates(dates);
            view.setSummitInfoContainerVisibility(true);
            return;
        }
        view.setSummitInfoContainerVisibility(false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(Constants.LOG_TAG, "SplashPresenter.onSaveInstanceState");
        outState.putBoolean(Constants.ON_DATA_LOADING_PROCESS, onDataLoading);
        outState.putBoolean(Constants.LOADED_SUMMITS_LIST, loadedSummitList);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(Constants.LOG_TAG, "SplashPresenter.onRestoreInstanceState");
        onDataLoading    = savedInstanceState.getBoolean(Constants.ON_DATA_LOADING_PROCESS, false);
        loadedSummitList = savedInstanceState.getBoolean(Constants.LOADED_SUMMITS_LIST, false);
    }

    private void disableDataUpdateService() {
        if (DataUpdatesService.isServiceAlarmOn((Context) view)) {
            DataUpdatesService.setServiceAlarm((Context) view, false);
        }
    }

    private void enableDataUpdateService() {
        if (!DataUpdatesService.isServiceAlarmOn((Context) view)) {
            DataUpdatesService.setServiceAlarm((Context) view, true);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        disableDataUpdateService();

       view.setLoginButtonVisibility(!interactor.isMemberLogged());
       view.setGuestButtonVisibility(!interactor.isMemberLogged());
       launchSummitListDataLoadingActivity();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    void launchSummitListDataLoadingActivity(){
        if(loadedSummitList) return;
        onDataLoading = true;
        // disable data updates ...
        disableDataUpdateService();
        wireframe.showSummitListLoadingActivity(this.view);
    }

    void launchInitialDataLoadingActivity() {
        if (!onDataLoading) {
            onDataLoading = true;
            // disable data updates ...
            disableDataUpdateService();
            wireframe.showSummitDataLoadingActivity(this.view);
        }
    }

    void launchMainActivity(boolean doLogin){
        wireframe.showMainActivity(this.view, doLogin);
    }

    void launchMainActivity(){
        wireframe.showMainActivity(this.view, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
            // Check which request we're responding to
            if (requestCode == ISplashView.DATA_LOAD_REQUEST) {
                onDataLoading = false;
                // Make sure the request was successful
                if (resultCode == Activity.RESULT_OK) {
                    Log.i(Constants.LOG_TAG, "SplashPresenter.onActivityResult: Summit Data Loaded!");
                    //re enable data update service
                    enableDataUpdateService();
                    if(interactor.isMemberLogged()) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                launchMainActivity();
                            }
                        }, SHOW_SPLASH_DELAY);

                        return;
                    }
                }
            }
            if(requestCode == ISplashView.SUMMITS_LIST_DATA_LOAD_REQUEST){
                onDataLoading    = false;
                loadedSummitList = true;
                showSummitInfo();
                // Make sure the request was successful
                if (resultCode == Activity.RESULT_OK) {
                    Log.i(Constants.LOG_TAG, "SplashPresenter.onActivityResult: Summit Data Loaded!");
                    if(interactor.isMemberLogged()) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                launchMainActivity();
                            }
                        }, SHOW_SPLASH_DELAY);
                        return;
                    }
                }
                else if(resultCode == SummitsListDataLoaderActivity.RESULT_OK_FIRE_SUMMIT_DATA_LOADING){
                    launchInitialDataLoadingActivity();
                }
            }
    }

    @Override
    public void loginClicked(View v){
        disableDataUpdateService();
        launchMainActivity(true);

    }

    @Override
    public void guestClicked(View v){
        enableDataUpdateService();
        launchMainActivity();
    }

}
