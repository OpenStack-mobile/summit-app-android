package org.openstack.android.summit.modules.splash.user_interface;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import org.openstack.android.summit.SummitsListDataLoaderActivity;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.SummitDTO;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.services.DataUpdatesService;
import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.modules.splash.ISplashWireframe;
import org.openstack.android.summit.modules.splash.business_logic.ISplashInteractor;

/**
 * Created by smarcet on 2/6/17.
 */

public class SplashPresenter extends BasePresenter<ISplashView, ISplashInteractor, ISplashWireframe>
        implements ISplashPresenter {

    ISummitSelector summitSelector;

    public SplashPresenter(ISplashInteractor interactor, ISplashWireframe wireframe, ISummitSelector summitSelector) {
        super(interactor, wireframe);
        this.summitSelector = summitSelector;
    }

    private Boolean onDataLoading              = false;
    private Boolean loadedSummitList           = false;
    private static final int SHOW_SPLASH_DELAY = 2000;

    public void showSummitInfo() {
        SummitDTO summit = interactor.getActiveSummit();
        if (summit != null) {
            view.setSummitName(summit.getName());
            String dates = summit.getLocalStartDate().toString("MMM")
                    + " " + summit.getLocalStartDate().toString("d") + "TH "
                    + " - " + summit.getLocalEndDate().toString("d") + "TH "
                    + ", " + summit.getLocalStartDate().toString("yyyy");
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
        PackageInfo pInfo = null;

        try {
            pInfo = view.getApplicationContext().getPackageManager().getPackageInfo(view.getApplicationContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            pInfo = null;
        }
        // check current build against stored build
        if (pInfo != null) {
            int currentBuildNumber = pInfo.versionCode;
            int installedBuildNumber = interactor.getInstalledBuildNumber();
            if (installedBuildNumber < currentBuildNumber) {
                Log.i(Constants.LOG_TAG, String.format("SplashPresenter.onCreate: old version %d - new version %d", installedBuildNumber, currentBuildNumber));
                interactor.setInstalledBuildNumber(currentBuildNumber);
                // if we are updating version and data is already loaded cleaning it ...
                if (interactor.isDataLoaded()) {
                    Log.i(Constants.LOG_TAG, "SplashPresenter.onCreate: upgrading data storage");
                    this.disableDataUpdateService();
                    interactor.upgradeStorage();
                    summitSelector.clearCurrentSummit();
                }
            }
        }
        if(savedInstanceState != null){
            onDataLoading    = savedInstanceState.getBoolean(Constants.ON_DATA_LOADING_PROCESS, false);
            loadedSummitList = savedInstanceState.getBoolean(Constants.LOADED_SUMMITS_LIST, false);
            Log.d(Constants.LOG_TAG, String.format("SplashPresenter.onCreate: Reloading former state - loadedSummitList %b - onDataLoading %b", loadedSummitList, onDataLoading));
        }
        view.setLoginButtonVisibility(!interactor.isMemberLoggedIn());
        view.setGuestButtonVisibility(!interactor.isMemberLoggedIn());

        launchSummitListDataLoadingActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        showSummitInfo();
    }

    void launchSummitListDataLoadingActivity() {
        if (loadedSummitList || onDataLoading) return;
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

    void launchMainActivity(boolean doLogin) {
        wireframe.showMainActivity(this.view, doLogin);
    }

    void launchMainActivity() {
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
                if (interactor.isMemberLoggedIn()) {
                    new Handler().postDelayed(() -> launchMainActivity(), SHOW_SPLASH_DELAY);
                    return;
                }
            }
        }
        if (requestCode == ISplashView.SUMMITS_LIST_DATA_LOAD_REQUEST) {
            loadedSummitList = true;
            onDataLoading    = false;
            showSummitInfo();
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                Log.i(Constants.LOG_TAG, "SplashPresenter.onActivityResult: Summit Data Loaded!");
                if (interactor.isMemberLoggedIn()) {
                    new Handler().postDelayed(() -> launchMainActivity(), SHOW_SPLASH_DELAY);
                    return;
                }
            } else if (resultCode == SummitsListDataLoaderActivity.RESULT_OK_FIRE_SUMMIT_DATA_LOADING) {
                launchInitialDataLoadingActivity();
            }
        }
    }

    @Override
    public void loginClicked(View v) {
        disableDataUpdateService();
        launchMainActivity(true);
    }

    @Override
    public void guestClicked(View v) {
        enableDataUpdateService();
        launchMainActivity();
    }

}
