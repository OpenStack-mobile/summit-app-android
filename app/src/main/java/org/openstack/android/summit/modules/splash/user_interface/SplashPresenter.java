package org.openstack.android.summit.modules.splash.user_interface;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.openstack.android.summit.SummitsListDataLoaderActivity;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.SummitDTO;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.notifications.IPushNotification;
import org.openstack.android.summit.common.entities.notifications.IPushNotificationFactory;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.common.services.DataUpdatesService;
import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.common.utils.DeepLinkInfo;
import org.openstack.android.summit.common.utils.IAppLinkRouter;
import org.openstack.android.summit.common.utils.RealmFactory;
import org.openstack.android.summit.modules.push_notifications_inbox.business_logic.IPushNotificationInteractor;
import org.openstack.android.summit.modules.push_notifications_inbox.business_logic.IPushNotificationsListInteractor;
import org.openstack.android.summit.modules.splash.ISplashWireframe;
import org.openstack.android.summit.modules.splash.business_logic.ISplashInteractor;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by smarcet on 2/6/17.
 */

public class SplashPresenter extends BasePresenter<ISplashView, ISplashInteractor, ISplashWireframe>
        implements ISplashPresenter {

    ISummitSelector summitSelector;

    IPushNotificationFactory pushNotificationFactory;

    IPushNotificationInteractor pushNotificationInteractor;

    IPushNotificationsListInteractor pushNotificationsListInteractor;

    IAppLinkRouter appLinkRouter;

    ISecurityManager securityManager;


    public SplashPresenter
    (
        ISplashInteractor interactor,
        ISplashWireframe wireframe,
        IPushNotificationInteractor pushNotificationInteractor,
        IPushNotificationsListInteractor pushNotificationsListInteractor,
        IPushNotificationFactory pushNotificationFactory,
        ISecurityManager securityManager,
        IAppLinkRouter appLinkRouter,
        ISummitSelector summitSelector
    )
    {
        super(interactor, wireframe);
        this.pushNotificationInteractor = pushNotificationInteractor;
        this.pushNotificationsListInteractor = pushNotificationsListInteractor;
        this.pushNotificationFactory = pushNotificationFactory;
        this.securityManager = securityManager;
        this.appLinkRouter = appLinkRouter;
        this.summitSelector = summitSelector;
    }

    private Boolean onDataLoading              = false;
    private Boolean loadedSummitList           = false;
    private static final int SHOW_SPLASH_DELAY = 2000;

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

        if(interactor.isDataLoaded()){
            Intent intent       = view.getIntent();

            if (intent != null && intent.getExtras() != null && intent.getAction().equals(Constants.ACTION_OPEN_OPENSTACK_PUSH_NOTIFICATION_FROM_SYSTRAY)) {
                Log.i(Constants.LOG_TAG, "fired ACTION_OPEN_OPENSTACK_PUSH_NOTIFICATION_FROM_SYSTRAY");
                // we got an notification from systray ( app background)
                Bundle extras = intent.getExtras();
                // move the extras to payload ...
                Map<String, String> payload = new HashMap<>();
                for(String key : extras.keySet()) {
                    Object value = extras.get(key);
                    payload.put(key, value.toString());
                }
                intent.getExtras().clear();

                try {
                    IPushNotification pushNotification = pushNotificationFactory.build(payload);
                    Member currentMember               = securityManager.getCurrentMember();

                    if (currentMember != null)
                        pushNotification.setOwner(currentMember);

                    pushNotificationInteractor.save(pushNotification);
                    Integer pushNotificationId = pushNotification.getId();
                    pushNotificationsListInteractor.markAsOpen(pushNotificationId);

                    wireframe.showNotification(view, appLinkRouter.buildUriFor(DeepLinkInfo.NotificationsPath, pushNotificationId.toString()));
                    return;
                }
                catch (Exception ex){
                    Log.w(Constants.LOG_TAG, ex.getMessage());
                }

            }
            // [END handle_dat

        }
        view.setLoginButtonVisibility(!interactor.isMemberLoggedIn());
        view.setGuestButtonVisibility(!interactor.isMemberLoggedIn());

        launchSummitListDataLoadingActivity();
    }


    public void showSummitInfo() {
        SummitDTO summit = interactor.getActiveSummit();
        if (summit != null) {
            view.setSummitName(summit.getName());
            view.setSummitDates(summit.getDatesLabel());
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
    public void onResume() {
        super.onResume();
        showSummitInfo();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //close realm session for current thread
        RealmFactory.closeSession();
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
