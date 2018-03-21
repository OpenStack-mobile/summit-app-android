package org.openstack.android.summit.modules.main.user_interface;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.Task;

import org.openstack.android.summit.BuildConfig;
import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.R;
import org.openstack.android.summit.SummitDataLoadingActivity;
import org.openstack.android.summit.SummitsListDataLoaderActivity;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.EventDetailDTO;
import org.openstack.android.summit.common.DTOs.MemberDTO;
import org.openstack.android.summit.common.ISession;
import org.openstack.android.summit.common.network.IReachability;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.common.services.DataUpdatesService;
import org.openstack.android.summit.common.services.UserActionsPostProcessService;
import org.openstack.android.summit.common.user_interface.AlertsBuilder;
import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.common.user_interface.BrowserActivity;
import org.openstack.android.summit.common.utils.DeepLinkInfo;
import org.openstack.android.summit.common.utils.IAppLinkRouter;
import org.openstack.android.summit.common.utils.RealmFactory;
import org.openstack.android.summit.modules.main.IMainWireframe;
import org.openstack.android.summit.modules.main.business_logic.IMainInteractor;
import org.openstack.android.summit.modules.main.exceptions.MissingMemberException;
import org.openstack.android.summit.modules.rsvp.RSVPViewerActivity;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import bolts.AppLinkNavigation;

/**
 * Created by Claudio Redi on 2/12/2016.
 */
public class MainPresenter
        extends BasePresenter<IMainView, IMainInteractor, IMainWireframe>
        implements IMainPresenter {

    private static final String SKIP_PROTECTED_APPS_MESSAGE = "SKIP_PROTECTED_APPS_MESSAGE";

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {

                if (intent.getAction() == Constants.WIPE_DATE_EVENT) {
                    Log.d(Constants.LOG_TAG, "MainPresenter.WIPE_DATE_EVENT");
                    loadedSummitList = onDataLoading = false;
                    launchSummitListDataLoadingActivity();
                }

                if (intent.getAction().contains(Constants.PUSH_NOTIFICATION_RECEIVED)) {
                    Log.d(Constants.LOG_TAG, "MainPresenter.PUSH_NOTIFICATION_RECEIVED");
                    updateNotificationCounter();
                    return;
                }

                if (intent.getAction().contains(Constants.PUSH_NOTIFICATION_DELETED)) {
                    Log.d(Constants.LOG_TAG, "MainPresenter.PUSH_NOTIFICATION_DELETED");
                    updateNotificationCounter();
                    return;
                }

                if (intent.getAction().contains(Constants.PUSH_NOTIFICATION_OPENED)) {
                    Log.d(Constants.LOG_TAG, "MainPresenter.PUSH_NOTIFICATION_OPENED");
                    updateNotificationCounter();
                    return;
                }

                if (intent.getAction().contains(Constants.START_LOG_IN_EVENT)) {
                    Log.d(Constants.LOG_TAG, "MainPresenter.START_LOG_IN_EVENT");
                    view.showActivityIndicator();
                    userLoginState = UserLoginState.Started;
                    return;
                }

                if (intent.getAction().contains(Constants.DO_EXTERNAL_LOG_IN_EVENT)) {
                    Log.d(Constants.LOG_TAG, "MainPresenter.DO_EXTERNAL_LOG_IN_EVENT");
                    initialView            = InitialView.None;
                    initiatedExternalLogin = true;
                    onClickLoginButton();
                    return;
                }

                if (intent.getAction().contains(Constants.DO_EXTERNAL_REDEEM_ORDER_EVENT)) {
                    Log.d(Constants.LOG_TAG, "MainPresenter.DO_EXTERNAL_REDEEM_ORDER_EVENT");
                    initialView                  = InitialView.None;
                    initiatedExternalRedeemOrder = true;
                    wireframe.showMemberOrderConfirmationView(view);
                    return;
                }

                if (intent.getAction().contains(Constants.LOG_IN_ERROR_EVENT)) {
                    Log.d(Constants.LOG_TAG, "MainPresenter.LOG_IN_ERROR_EVENT");
                    view.hideActivityIndicator();
                    AlertDialog dialog = AlertsBuilder.buildGenericError(view.getFragmentActivity());
                    if(dialog != null) dialog.show();
                    userLoginState             = UserLoginState.None;
                    userLoginButtonInteraction = UserLoginButtonInteraction.None;
                    view.setMenuItemVisible(R.id.nav_my_profile, false);
                    enabledBackgroundServices();
                    return;
                }

                if (intent.getAction().contains(Constants.LOGGED_IN_EVENT)) {
                    try {
                        Log.d(Constants.LOG_TAG, "MainPresenter.LOGGED_IN_EVENT");
                        onLoggedIn();
                        view.setMenuItemVisible(R.id.nav_my_profile, true);
                        if(!initiatedExternalLogin && !initiatedExternalRedeemOrder){
                            if(interactor.isMemberLoggedInAndConfirmedAttendee()){
                                // if we are confirmed attendee start on main view
                                view.setMenuItemChecked(R.id.nav_events);
                                showEventsView();
                            }
                            else {
                                // if we are not confirmed attendee show my profile
                                view.setMenuItemChecked(R.id.nav_my_profile);
                                showMyProfileView(Constants.MY_PROFILE_TAB_PROFILE);
                            }
                        }
                        if(initiatedExternalRedeemOrder){
                            // just do back
                            wireframe.back(view);
                        }
                    } catch (MissingMemberException ex1) {
                        Crashlytics.logException(ex1);
                        Log.w(Constants.LOG_TAG, ex1.getMessage());
                        AlertDialog dialog = AlertsBuilder.buildError(view.getFragmentActivity(), R.string.login_error_message);
                        if(dialog != null) dialog.show();
                    } finally {
                        enabledBackgroundServices();
                        view.hideActivityIndicator();
                        userLoginButtonInteraction   = UserLoginButtonInteraction.None;
                        userLoginState               = UserLoginState.None;
                        initiatedExternalLogin       = false;
                        initiatedExternalRedeemOrder = false;
                    }
                    return;
                }

                if (intent.getAction().contains(Constants.LOGGED_OUT_EVENT)) {
                    try {
                        Log.d(Constants.LOG_TAG, "LOGGED_OUT_EVENT");
                        onLoggedOut();
                        if (userLoginButtonInteraction.equals(UserLoginButtonInteraction.None)) {

                            AlertDialog dialog = AlertsBuilder.buildAlert(view.getFragmentActivity(),R.string.generic_info_title, R.string.session_expired_message);
                            if(dialog != null) dialog.show();
                        }

                        showEventsView();
                    }
                    finally {
                        if(intent.getBooleanExtra(Constants.EXTRA_ENABLE_DATA_UPDATES_AFTER_LOGOUT, false))
                            enabledBackgroundServices();
                        userLoginState             = UserLoginState.None;
                        userLoginButtonInteraction = UserLoginButtonInteraction.None;
                    }
                    return;
                }

                if (intent.getAction().contains(Constants.LOG_IN_CANCELLED_EVENT)) {
                    Log.d(Constants.LOG_TAG, "LOG_IN_CANCELLED_EVENT");
                    view.hideActivityIndicator();
                    userLoginState             = UserLoginState.None;
                    userLoginButtonInteraction = UserLoginButtonInteraction.None;
                    return;
                }

            } catch (Exception ex) {
                Crashlytics.logException(new Exception(String.format("Action %s", intent.getAction()), ex));
            }
        }
    };

    public enum UserLoginButtonInteraction {
        None(0),
        ClickLogIn(1),
        ClickLogOut(2);

        private final int value;

        UserLoginButtonInteraction(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static UserLoginButtonInteraction getValue(int id) {
            UserLoginButtonInteraction[] As = UserLoginButtonInteraction.values();
            for (int i = 0; i < As.length; i++) {
                if (As[i].getValue() == id)
                    return As[i];
            }
            return UserLoginButtonInteraction.None;
        }
    }

    public enum UserLoginState {
        None(0),
        Started(1),
        Cancelled(2),
        Error(3);

        private final int value;

        UserLoginState(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static UserLoginState getValue(int id)
        {
            UserLoginState[] As = UserLoginState.values();
            for(int i = 0; i < As.length; i++)
            {
                if(As[i].getValue() == id)
                    return As[i];
            }
            return UserLoginState.None;
        }
    }

    public enum InitialView {
        None(0),
        Events(1);

        private final int value;

        InitialView(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static InitialView getValue(int id)
        {
            InitialView[] As = InitialView.values();
            for(int i = 0; i < As.length; i++)
            {
                if(As[i].getValue() == id)
                    return As[i];
            }
            return InitialView.None;
        }
    }

    private ISecurityManager securityManager;
    private IReachability reachability;
    private IAppLinkRouter appLinkRouter;
    private ISession session;
    private UserLoginButtonInteraction userLoginButtonInteraction = UserLoginButtonInteraction.None;
    private UserLoginState userLoginState                         = UserLoginState.None;
    private InitialView    initialView                            = InitialView.Events;
    private Boolean        initiatedExternalLogin                 = false;
    private Boolean        initiatedExternalRedeemOrder           = false;

    public MainPresenter
    (
        IMainInteractor interactor,
        IMainWireframe wireframe,
        IAppLinkRouter appLinkRouter,
        ISecurityManager securityManager,
        IReachability reachability,
        ISession session
    )
    {
        super(interactor, wireframe);

        this.appLinkRouter   = appLinkRouter;
        this.session         = session;
        this.securityManager = securityManager;
        this.reachability    = reachability;

        if (BuildConfig.FLAVOR.contains(Constants.FLAVOR_BETA) || BuildConfig.FLAVOR.contains(Constants.FLAVOR_DEV)) {
            trustEveryone();
        }
    }

    private Task<Void> googlePlayServicesTask = null;

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private void checkPlayServices() {
        try {
            GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();

            int success = googleApiAvailability.isGooglePlayServicesAvailable((Activity) view);

            if (success != ConnectionResult.SUCCESS && googlePlayServicesTask != null) {
                googlePlayServicesTask = googleApiAvailability.makeGooglePlayServicesAvailable((Activity) view);
            }
        }
        catch (Exception ex){
            Log.e(Constants.LOG_TAG, ex.getMessage());
        }
    }

    private void updateNotificationCounter() {
        view.updateNotificationCounter(this.interactor.getNotReadNotificationsCount());
    }

    private boolean onDataLoading    = false;
    private boolean loadedSummitList = false;

    private void launchInitialDataLoadingActivity() {
        if (!onDataLoading) {
            onDataLoading = true;
            // disable background services ...
            disableBackgroundServices();
            Intent intent = new Intent((Activity) view, SummitDataLoadingActivity.class);
            Log.i(Constants.LOG_TAG, "starting SummitDataLoadingActivity ...");
            view.startActivityForResult(intent, IMainView.DATA_LOAD_REQUEST);
        }
    }

    private void launchSummitListDataLoadingActivity(){
        if(loadedSummitList) return;
        onDataLoading = true;
        // disable background services ...
        disableBackgroundServices();
        Intent intent = new Intent((Activity) view, SummitsListDataLoaderActivity.class);
        Log.i(Constants.LOG_TAG, "starting SummitsListDataLoaderActivity ...");
        view.startActivityForResult(intent, IMainView.SUMMITS_LIST_DATA_LOAD_REQUEST);
    }

    @Override
    public void onClickMemberProfilePic(){

        userLoginButtonInteraction = UserLoginButtonInteraction.ClickLogIn;

        view.showActivityIndicator();

        if (!reachability.isNetworkingAvailable(view.getApplicationContext())) {
            view.hideActivityIndicator();
            AlertDialog dialog = AlertsBuilder.buildError(view.getFragmentActivity(), R.string.login_disallowed_no_connectivity);
            if(dialog != null) dialog.show();
            return;
        }

        disableBackgroundServices();

        if (!interactor.isDataLoaded()) {
            view.hideActivityIndicator();
            AlertDialog dialog = AlertsBuilder.buildError(view.getFragmentActivity(), R.string.login_disallowed_no_data);
            if(dialog != null) dialog.show();
            //launchSummitListDataLoadingActivity();
            return;
        }

        // LOGIN
        if (!securityManager.isLoggedIn()) {
            securityManager.login((Activity)view);
            return;
        }

        // go to my profile
        view.hideActivityIndicator();
        view.closeMenuDrawer();
        showMyProfileView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == IMainView.DATA_LOAD_REQUEST) {

            onDataLoading = false;
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                Log.i(Constants.LOG_TAG, "MainPresenter.onActivityResult: Summit Data Loaded!");
                enabledBackgroundServices();
                this.showEventsView();
            }
        }
        if(requestCode == IMainView.SUMMITS_LIST_DATA_LOAD_REQUEST){
            onDataLoading    = false;
            loadedSummitList = true;
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                Log.i(Constants.LOG_TAG, "MainPresenter.onActivityResult: Summit Data Loaded!");
                //re enable background services
                enabledBackgroundServices();
            }
            if(resultCode == SummitsListDataLoaderActivity.RESULT_OK_FIRE_SUMMIT_DATA_LOADING){
                this.launchInitialDataLoadingActivity();
            }
        }
    }

    @Override
    public void onClickLoginButton() {
        try {
            userLoginButtonInteraction = UserLoginButtonInteraction.ClickLogIn;
            boolean isUserLogged       = securityManager.isLoggedIn();
            view.showActivityIndicator();

            if (!isUserLogged && !reachability.isNetworkingAvailable(view.getApplicationContext())) {
                view.hideActivityIndicator();
                AlertDialog dialog =  AlertsBuilder.buildError(view.getFragmentActivity(), R.string.login_disallowed_no_connectivity);
                if(dialog != null) dialog.show();
                return;
            }

            if (!isUserLogged && !interactor.isDataLoaded()) {
                view.hideActivityIndicator();
                AlertDialog dialog = AlertsBuilder.buildError(view.getFragmentActivity(), R.string.login_disallowed_no_data);
                if(dialog != null) dialog.show();
                //launchSummitListDataLoadingActivity();
                return;
            }

            disableBackgroundServices();
            // LOGIN
            if (!isUserLogged) {
                securityManager.login((Activity) view);
                return;
            }

            // LOGOUT
            userLoginButtonInteraction = UserLoginButtonInteraction.ClickLogOut;
            securityManager.logout(true);
        }
        catch (Exception ex){
            Crashlytics.logException(ex);
        }
        finally {
            view.hideActivityIndicator();
        }
    }

    /**
     *  @link https://developer.android.com/training/monitoring-device-state/doze-standby.html
     */
    private void skipDoze(){
        try {
            // battery optimization code
            final Context ctx                = view.getApplicationContext();
            final SharedPreferences settings = ctx.getSharedPreferences("ProtectedApps", Context.MODE_PRIVATE);
            boolean skipMessage              = settings.getBoolean(SKIP_PROTECTED_APPS_MESSAGE, false);

            if (!skipMessage) {
                final SharedPreferences.Editor editor = settings.edit();
                PowerManager pm = (PowerManager) ctx.getSystemService(Context.POWER_SERVICE);
                String packageName = view.getApplicationContext().getPackageName();
                boolean isIgnoringBatteryOptimizations = true;
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    isIgnoringBatteryOptimizations = pm.isIgnoringBatteryOptimizations(packageName);
                }

                if (!isIgnoringBatteryOptimizations) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(Uri.parse("package:" + packageName));

                    editor.putBoolean(SKIP_PROTECTED_APPS_MESSAGE, true);
                    editor.apply();

                    view.startActivity(intent);
                }
            }
        }
        catch (Exception ex){
            Crashlytics.logException(ex);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // kill any background services enabled ...
        disableBackgroundServices();
        // bind local broadcast receiver
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.START_LOG_IN_EVENT);
        intentFilter.addAction(Constants.LOG_IN_CANCELLED_EVENT);
        intentFilter.addAction(Constants.LOG_IN_ERROR_EVENT);
        intentFilter.addAction(Constants.LOGGED_IN_EVENT);
        intentFilter.addAction(Constants.LOGGED_OUT_EVENT);
        intentFilter.addAction(Constants.PUSH_NOTIFICATION_RECEIVED);
        intentFilter.addAction(Constants.PUSH_NOTIFICATION_DELETED);
        intentFilter.addAction(Constants.PUSH_NOTIFICATION_OPENED);
        intentFilter.addAction(Constants.WIPE_DATE_EVENT);
        intentFilter.addAction(Constants.DO_EXTERNAL_LOG_IN_EVENT);
        intentFilter.addAction(Constants.DO_EXTERNAL_REDEEM_ORDER_EVENT);

        LocalBroadcastManager.getInstance(OpenStackSummitApplication.context).registerReceiver(messageReceiver, intentFilter);

        if (interactor.isDataLoaded()) {
            enabledBackgroundServices();
        }

        if (savedInstanceState != null) {
            onDataLoading                = savedInstanceState.getBoolean(Constants.ON_DATA_LOADING_PROCESS, false);
            loadedSummitList             = savedInstanceState.getBoolean(Constants.LOADED_SUMMITS_LIST, false);
            userLoginState               = UserLoginState.getValue(savedInstanceState.getInt(Constants.USER_LOG_IN_STATE, UserLoginState.None.getValue()));
            initiatedExternalLogin       = savedInstanceState.getBoolean(Constants.INIT_EXTERNAL_LOGIN, false);
            initiatedExternalRedeemOrder = savedInstanceState.getBoolean(Constants.INIT_EXTERNAL_REDEEM_ORDER, false);

            if (userLoginState.equals(UserLoginState.Started) || initiatedExternalLogin || initiatedExternalRedeemOrder) {
                initialView = InitialView.None;
            }
        }

        checkPlayServices();
        skipDoze();
        if (securityManager.isLoggedIn()) {
            try {
                onLoggedIn();
            }
            catch(MissingMemberException ex1){
                Crashlytics.logException(ex1);
                Log.w(Constants.LOG_TAG, ex1.getMessage());
                AlertDialog dialog = AlertsBuilder.buildError(view.getFragmentActivity(), R.string.login_error_message);
                if(dialog != null) dialog.show();
            }
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        if (!this.userLoginState.equals(UserLoginState.None)) {
            Log.d(Constants.LOG_TAG, "MainPresenter.onStart : its on logging flow ...");
            view.showActivityIndicator();
            return;
        }
        Log.d(Constants.LOG_TAG, "MainPresenter.onStart: its on regular flow ...");
        securityManager.init();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(Constants.LOG_TAG, "MainPresenter.onResume");
        checkPlayServices();
        updateNotificationCounter();

        interactor.subscribeToPushNotifications();
        view.toggleMyProfileMenuItem(interactor.isMemberLoggedIn());

        if(initialView.equals(InitialView.Events)) {
            showEventsView();
            initialView = InitialView.None;
        }

        checkDeepLinks();

        Intent intent = view.getIntent();
        if(intent != null
                && intent.getBooleanExtra(Constants.START_EXTERNAL_LOGIN, false)
                && !securityManager.isLoggedIn()
                && this.userLoginState.equals(UserLoginState.None)){
            intent.removeExtra(Constants.START_EXTERNAL_LOGIN);
            view.toggleMenu(true);
            onClickLoginButton();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private boolean checkDeepLinks() {
        Intent intent = view.getIntent();
        if (intent != null) {
            String action = intent.getAction();
            Uri url = intent.getData();
            if (action == Intent.ACTION_VIEW && url != null) {
                // mark as processed
                view.setIntent(null);
                Log.d(Constants.LOG_TAG, "processing deep link url " + url.toString());
                if (this.appLinkRouter.isDeepLink(url)) {
                    // do routing ...
                    Log.d(Constants.LOG_TAG, "deep link url " + url.toString());
                    DeepLinkInfo deepLinkInfo = appLinkRouter.buildDeepLinkInfo(url);
                    if (deepLinkInfo.getAction() == DeepLinkInfo.ActionViewEvent) {
                        if (!deepLinkInfo.hasParam()) return false;
                        view.setMenuItemChecked(R.id.nav_events);
                        int eventId          = deepLinkInfo.getParamAsInt();
                        EventDetailDTO event = this.interactor.getEventById(eventId);
                        int day              = event.getStartDate().getDayOfMonth();
                        this.wireframe.showEventDetail(deepLinkInfo.getParamAsInt(), day, this.view);
                        return true;
                    }
                    if (deepLinkInfo.getAction() == DeepLinkInfo.ActionViewSpeaker) {
                        if (!deepLinkInfo.hasParam()) return false;
                        view.setMenuItemChecked(R.id.nav_speakers);
                        this.wireframe.showSpeakerProfile(deepLinkInfo.getParamAsInt(), this.view);
                        return true;
                    }
                    if (deepLinkInfo.getAction() == DeepLinkInfo.ActionViewLevel) {
                        if (!deepLinkInfo.hasParam()) return false;
                        view.setMenuItemChecked(R.id.nav_events);
                        String level = deepLinkInfo.getParam();
                        this.wireframe.showEventsViewByLevel(level, this.view);
                        return true;
                    }
                    if (deepLinkInfo.getAction() == DeepLinkInfo.ActionViewSearch) {
                        if (!deepLinkInfo.hasParam()) return false;
                        view.setMenuItemChecked(R.id.nav_events);
                        String search = deepLinkInfo.getParam();
                        try{
                            search =  java.net.URLDecoder.decode(search, "UTF-8");
                        }
                        catch (Exception ex){

                        }
                        this.wireframe.showSearchView(search, this.view);
                        return true;
                    }
                    if (deepLinkInfo.getAction() == DeepLinkInfo.ActionViewTrack) {
                        if (!deepLinkInfo.hasParam()) return false;
                        view.setMenuItemChecked(R.id.nav_events);
                        int trackId = deepLinkInfo.getParamAsInt();
                        this.wireframe.showEventsViewByTrack(trackId, this.view);
                        return true;
                    }
                    if (deepLinkInfo.getAction() == DeepLinkInfo.ActionViewNotification) {
                        if (!deepLinkInfo.hasParam()) return false;
                        view.setMenuItemChecked(R.id.nav_notifications);
                        this.wireframe.showPushNotification(deepLinkInfo.getParamAsInt(), this.view);
                        return true;
                    }
                    if (deepLinkInfo.getAction() == DeepLinkInfo.ActionViewSchedule) {
                        view.setMenuItemChecked(R.id.nav_events);
                        return true;
                    }
                    if (deepLinkInfo.getAction() == DeepLinkInfo.ActionViewLocation) {
                        if (!deepLinkInfo.hasParam()) return false;
                        return true;
                    }
                }
                // get the app link metadata
                //before check if we are trying to see a custom rsvp
                if (this.appLinkRouter.isCustomRSVPLink(url)) {
                    Log.d(Constants.LOG_TAG, "opening custom RSVP template ...");
                    // match! rsvp browser
                    Intent i = new Intent((Context) view, RSVPViewerActivity.class);
                    i.setData(url);
                    view.startActivity(i);
                    return false;
                }
                if(this.appLinkRouter.isRawMainSchedule(url)){
                    Log.d(Constants.LOG_TAG, "opening RAW main schedule ...");
                    Intent i = new Intent((Context) view, BrowserActivity.class);
                    i.setData(url);
                    view.startActivity(i);
                    return false;
                }
                Log.d(Constants.LOG_TAG, "do app link url navigation to " + url.toString());
                AppLinkNavigation.navigateInBackground((MainActivity) view, url);
            }
        }
        return false;
    }

    public void showEventsView() {
        wireframe.showEventsView(view);
    }

    @Override
    public void showEventView(int eventId) {
        wireframe.showEventDetail(eventId, view);
    }

    @Override
    public void showNotificationView() {
        if (!interactor.isDataLoaded()) {
            AlertDialog dialog = AlertsBuilder.buildError(view.getFragmentActivity(), R.string.no_summit_data_available);
            if(dialog != null) dialog.show();
            return;
        }
        wireframe.showNotificationsListView(view);
    }

    @Override
    public void enableDataUpdateService() {
        if (!DataUpdatesService.isServiceAlarmOn((Context) view)) {
            DataUpdatesService.setServiceAlarm((Context) view, true);
        }
    }

    @Override
    public void disableDataUpdateService() {
        if (DataUpdatesService.isServiceAlarmOn((Context) view)) {
            DataUpdatesService.setServiceAlarm((Context) view, false);
        }
    }

    @Override
    public void enableUserActionsPostProcessService() {
        /*if (!UserActionsPostProcessService.isServiceAlarmOn((Context) view)) {
            UserActionsPostProcessService.setServiceAlarm((Context) view, true);
        }
        */
        if(!UserActionsPostProcessService.isRunning()) {
            UserActionsPostProcessService.start(view.getApplicationContext());
        }
    }

    @Override
    public void disableUserActionsPostProcessService() {
        /*if (UserActionsPostProcessService.isServiceAlarmOn((Context) view)) {
            UserActionsPostProcessService.setServiceAlarm((Context) view, false);
        }*/
        if(UserActionsPostProcessService.isRunning()) {
            UserActionsPostProcessService.stop();
        }
    }

    @Override
    public void enabledBackgroundServices() {
        enableDataUpdateService();
        enableUserActionsPostProcessService();
    }

    @Override
    public void disableBackgroundServices() {
        disableUserActionsPostProcessService();
        disableDataUpdateService();
    }

    @Override
    public void showMyProfileView(){
        showMyProfileView(null);
    }


    private void  showMyProfileView(String defaultTabTitle) {
        if (!interactor.isDataLoaded()) {
            AlertDialog dialog = AlertsBuilder.buildError(view.getFragmentActivity(), R.string.no_summit_data_available);
            if(dialog != null) dialog.show();
            return;
        }

        if (interactor.isMemberLoggedIn()) {
            wireframe.showMyProfileView(view, defaultTabTitle);
            return;
        }

        AlertDialog dialog = AlertsBuilder.buildAlert(view.getFragmentActivity(),R.string.generic_info_title, R.string.no_logged_in_user);
        if(dialog != null) dialog.show();
    }

    public void showSpeakerListView() {
        if (!interactor.isDataLoaded()) {
            AlertDialog dialog = AlertsBuilder.buildError(view.getFragmentActivity(), R.string.no_summit_data_available);
            if(dialog != null) dialog.show();
            return;
        }

        wireframe.showSpeakerListView(view);
    }

    public void showSearchView(String searchTerm) {
        if (!interactor.isDataLoaded()) {
            AlertDialog dialog = AlertsBuilder.buildError(view.getFragmentActivity(), R.string.no_summit_data_available);
            if(dialog != null) dialog.show();
            return;
        }

        wireframe.showSearchView(searchTerm, view);
    }

    public void showAboutView() {
        if (!interactor.isDataLoaded()) {
            AlertDialog dialog = AlertsBuilder.buildError(view.getFragmentActivity(), R.string.no_summit_data_available);
            if(dialog != null) dialog.show();
            return;
        }

        wireframe.showAboutView(view);
    }

    public void showVenuesView() {
        if (!interactor.isDataLoaded()) {
            AlertDialog dialog = AlertsBuilder.buildError(view.getFragmentActivity(), R.string.no_summit_data_available);
            if(dialog != null) dialog.show();
            return;
        }
        wireframe.showVenuesView(view);
    }

    @Override
    public void onLoggedOut() {
        view.setMemberName("");
        view.setLoginButtonText(view.getResources().getText(R.string.log_in).toString());
        view.setProfilePic(null);
        interactor.unSubscribeToPushNotifications();
        interactor.subscribeToPushNotifications();
        session.setInt(Constants.WILL_ATTEND, 0);
        updateNotificationCounter();
        view.setNavigationViewLogOutState();
    }

    @Override
    public void onLoggedIn() throws MissingMemberException {
        MemberDTO member = interactor.getCurrentMember();
        if (member == null)
        {
            Log.w(Constants.LOG_TAG, "MainPresenter.onLoggedIn : member is null !!!");
            throw new MissingMemberException();
        }

        view.setMemberName(member.getFullName());
        view.setLoginButtonText(view.getResources().getText(R.string.log_out).toString());
        view.setProfilePic(Uri.parse(member.getPictureUrl()));
        view.toggleMenu(false);
        interactor.unSubscribeToPushNotifications();
        interactor.subscribeToPushNotifications();
        updateNotificationCounter();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.USER_LOG_IN_STATE, userLoginState.getValue());
        outState.putBoolean(Constants.ON_DATA_LOADING_PROCESS, onDataLoading);
        outState.putBoolean(Constants.LOADED_SUMMITS_LIST, loadedSummitList);
        outState.putBoolean(Constants.INIT_EXTERNAL_LOGIN, initiatedExternalLogin);
        outState.putBoolean(Constants.INIT_EXTERNAL_REDEEM_ORDER, initiatedExternalRedeemOrder);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //stop background services ...
        disableBackgroundServices();
        // unbind local broadcast receiver
        LocalBroadcastManager.getInstance(OpenStackSummitApplication.context).unregisterReceiver(messageReceiver);
        view.hideActivityIndicator();
        //close realm session for current thread
        RealmFactory.closeSession();
    }

    @Override
    public void onOpenedNavigationMenu() {

    }

    private void trustEveryone() {

        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(
                    context.getSocketFactory());
        } catch (Exception e) { // should never happen
            e.printStackTrace();
        }
    }
}
