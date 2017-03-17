package org.openstack.android.summit.modules.main.user_interface;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.openstack.android.summit.BuildConfig;
import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.R;
import org.openstack.android.summit.SummitDataLoadingActivity;
import org.openstack.android.summit.SummitsListDataLoaderActivity;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.MemberDTO;
import org.openstack.android.summit.common.ISession;
import org.openstack.android.summit.common.devices.huawei.HuaweiHelper;
import org.openstack.android.summit.common.network.IReachability;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.common.services.DataUpdatesService;
import org.openstack.android.summit.common.user_interface.BasePresenter;
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

                if (intent.getAction().contains(Constants.LOG_IN_ERROR_EVENT)) {
                    Log.d(Constants.LOG_TAG, "MainPresenter.LOG_IN_ERROR_EVENT");
                    view.hideActivityIndicator();
                    view.showErrorMessage(intent.getExtras().getString(Constants.LOG_IN_ERROR_MESSAGE, Constants.GENERIC_ERROR_MSG));
                    userLoginState = UserLoginState.None;
                    view.setMenuItemVisible(R.id.nav_my_profile, false);
                    enableDataUpdateService();
                    return;
                }

                if (intent.getAction().contains(Constants.LOGGED_IN_EVENT)) {
                    try {
                        Log.d(Constants.LOG_TAG, "MainPresenter.LOGGED_IN_EVENT");
                        onLoggedIn();
                        // show my profile tab ...
                        view.setMenuItemChecked(R.id.nav_my_profile);
                        view.setMenuItemVisible(R.id.nav_my_profile, true);
                        showMyProfileView();
                    } catch (MissingMemberException ex1) {
                        Crashlytics.logException(ex1);
                        Log.w(Constants.LOG_TAG, ex1.getMessage());
                        view.showErrorMessage(view.getResources().getString(R.string.login_error_message));
                    } finally {
                        enableDataUpdateService();
                        view.hideActivityIndicator();
                        userLoginState = UserLoginState.None;
                    }
                    return;
                }

                if (intent.getAction().contains(Constants.LOGGED_OUT_EVENT)) {
                    try {
                        Log.d(Constants.LOG_TAG, "LOGGED_OUT_EVENT");
                        onLoggedOut();
                        if (userLoginButtonInteraction.equals(UserLoginButtonInteraction.None)) {
                            view.showInfoMessage(view.getResources().getString(R.string.session_expired_message));
                        }
                        userLoginButtonInteraction = UserLoginButtonInteraction.None;
                        showEventsView();
                    }
                    finally {
                        if(intent.getBooleanExtra(Constants.EXTRA_ENABLE_DATA_UPDATES_AFTER_LOGOUT, false))
                            enableDataUpdateService();
                        userLoginState = UserLoginState.None;
                    }
                    return;
                }

                if (intent.getAction().contains(Constants.LOG_IN_CANCELLED_EVENT)) {
                    Log.d(Constants.LOG_TAG, "LOG_IN_CANCELLED_EVENT");
                    view.hideActivityIndicator();
                    userLoginState = UserLoginState.None;
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

        private UserLoginButtonInteraction(int value) {
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

        private UserLoginState(int value) {
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

    public enum InitialView{
        None,
        Events,
    }

    private ISecurityManager securityManager;
    private IReachability reachability;
    private IAppLinkRouter appLinkRouter;
    private ISession session;
    private UserLoginButtonInteraction userLoginButtonInteraction = UserLoginButtonInteraction.None;
    private UserLoginState userLoginState                         = UserLoginState.None;
    private InitialView    initialView                            = InitialView.Events;

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

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private void checkPlayServices() {
        GoogleApiAvailability googleApiAvailability =  GoogleApiAvailability.getInstance();

        int success = googleApiAvailability.isGooglePlayServicesAvailable((Activity)view);

        if(success != ConnectionResult.SUCCESS)
        {
            googleApiAvailability.makeGooglePlayServicesAvailable((Activity)view);
        }
    }

    private void updateNotificationCounter() {
        view.updateNotificationCounter(this.interactor.getNotReadNotificationsCount());
    }

    @Override
    public void showSettingsView() {
        wireframe.showSettingsView(view);
    }

    private boolean onDataLoading    = false;
    private boolean loadedSummitList = false;


    private void launchInitialDataLoadingActivity() {
        if (!onDataLoading) {
            onDataLoading = true;
            // disable data updates ...
            disableDataUpdateService();
            Intent intent = new Intent((Activity) view, SummitDataLoadingActivity.class);
            Log.i(Constants.LOG_TAG, "starting SummitDataLoadingActivity ...");
            view.startActivityForResult(intent, IMainView.DATA_LOAD_REQUEST);
        }
    }

    private void launchSummitListDataLoadingActivity(){
        if(loadedSummitList) return;
        onDataLoading = true;
        // disable data updates ...
        disableDataUpdateService();

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
            view.showErrorMessage(view.getResources().getString(R.string.login_disallowed_no_connectivity));
            return;
        }

        disableDataUpdateService();

        if (!interactor.isDataLoaded()) {
            view.hideActivityIndicator();
            view.showInfoMessage(view.getResources().getString(R.string.login_disallowed_no_data));
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
                enableDataUpdateService();
                this.showEventsView();
            }
        }
        if(requestCode == IMainView.SUMMITS_LIST_DATA_LOAD_REQUEST){
            onDataLoading    = false;
            loadedSummitList = true;
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                Log.i(Constants.LOG_TAG, "MainPresenter.onActivityResult: Summit Data Loaded!");
                //re enable data update service
                enableDataUpdateService();
            }
            if(resultCode == SummitsListDataLoaderActivity.RESULT_OK_FIRE_SUMMIT_DATA_LOADING){
                this.launchInitialDataLoadingActivity();
            }
        }
    }

    @Override
    public void onClickLoginButton() {
        userLoginButtonInteraction = UserLoginButtonInteraction.ClickLogIn;

        view.showActivityIndicator();

        if (!reachability.isNetworkingAvailable(view.getApplicationContext())) {
            view.hideActivityIndicator();
            view.showErrorMessage(view.getResources().getString(R.string.login_disallowed_no_connectivity));
            return;
        }

        disableDataUpdateService();

        if (!interactor.isDataLoaded()) {
            view.hideActivityIndicator();
            view.showInfoMessage(view.getResources().getString(R.string.login_disallowed_no_data));
            //launchSummitListDataLoadingActivity();
            return;
        }

        // LOGIN
        if (!securityManager.isLoggedIn()) {
            securityManager.login((Activity)view);
            return;
        }

        // LOGOUT
        userLoginButtonInteraction = UserLoginButtonInteraction.ClickLogOut;

        securityManager.logout(true);
        view.hideActivityIndicator();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // kill any data updates enabled
        disableDataUpdateService();
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
        LocalBroadcastManager.getInstance(OpenStackSummitApplication.context).registerReceiver(messageReceiver, intentFilter);

        HuaweiHelper.check((Activity) view);

        if (interactor.isDataLoaded()) {
            enableDataUpdateService();
        }

        if (savedInstanceState != null) {
            onDataLoading    = savedInstanceState.getBoolean(Constants.ON_DATA_LOADING_PROCESS, false);
            loadedSummitList = savedInstanceState.getBoolean(Constants.LOADED_SUMMITS_LIST, false);
            userLoginState   = UserLoginState.getValue(savedInstanceState.getInt(Constants.USER_LOG_IN_STATE, UserLoginState.None.getValue()));

            if (userLoginState.equals(UserLoginState.Started)) {
                initialView = InitialView.None;
            }
        }

        checkPlayServices();

        if (securityManager.isLoggedIn()) {
            try {
                onLoggedIn();
            }
            catch(MissingMemberException ex1){
                Crashlytics.logException(ex1);
                Log.w(Constants.LOG_TAG, ex1.getMessage());
                view.showErrorMessage(view.getResources().getString(R.string.login_error_message));
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
        checkDeepLinks();
        interactor.subscribeToPushNotifications();
        view.toggleMyProfileMenuItem(interactor.isMemberLoggedIn());

        if(initialView.equals(InitialView.Events)) {
            showEventsView();
            initialView = InitialView.None;
        }

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
                        this.wireframe.showEventDetail(deepLinkInfo.getParamAsInt(), this.view);
                        return true;
                    }
                    if (deepLinkInfo.getAction() == DeepLinkInfo.ActionViewSpeaker) {
                        if (!deepLinkInfo.hasParam()) return false;
                        view.setMenuItemChecked(R.id.nav_speakers);
                        this.wireframe.showSpeakerProfile(deepLinkInfo.getParamAsInt(), this.view);
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
            view.showInfoMessage(view.getResources().getString(R.string.no_summit_data_available));
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

    public void showMyProfileView() {
        if (!interactor.isDataLoaded()) {
            view.showInfoMessage(view.getResources().getString(R.string.no_summit_data_available));
            return;
        }

        if (interactor.isMemberLoggedIn()) {
            wireframe.showMyProfileView(view);
            return;
        }

        view.showInfoMessage(view.getResources().getString(R.string.no_logged_in_user));
    }

    public void showSpeakerListView() {
        if (!interactor.isDataLoaded()) {
            view.showInfoMessage(view.getResources().getString(R.string.no_summit_data_available));
            return;
        }

        wireframe.showSpeakerListView(view);
    }

    public void showSearchView(String searchTerm) {
        if (!interactor.isDataLoaded()) {
            view.showInfoMessage(view.getResources().getString(R.string.no_summit_data_available));
            return;
        }

        wireframe.showSearchView(searchTerm, view);
    }

    public void showAboutView() {
        if (!interactor.isDataLoaded()) {
            view.showInfoMessage(view.getResources().getString(R.string.no_summit_data_available));
            return;
        }

        wireframe.showAboutView(view);
    }

    public void showVenuesView() {
        if (!interactor.isDataLoaded()) {
            view.showInfoMessage(view.getResources().getString(R.string.no_summit_data_available));
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //stop data updates services
        disableDataUpdateService();
        // unbind local broadcast receiver
        LocalBroadcastManager.getInstance(OpenStackSummitApplication.context).unregisterReceiver(messageReceiver);
        view.hideActivityIndicator();
        //close realm session for current thread
        RealmFactory.closeSession();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        view.toggleMenuLogo(newConfig.orientation != Configuration.ORIENTATION_LANDSCAPE);
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
