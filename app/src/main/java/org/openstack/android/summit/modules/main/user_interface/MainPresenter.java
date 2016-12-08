package org.openstack.android.summit.modules.main.user_interface;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import org.openstack.android.summit.BuildConfig;
import org.openstack.android.summit.R;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.MemberDTO;
import org.openstack.android.summit.common.services.DataUpdatesService;
import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.common.utils.DeepLinkInfo;
import org.openstack.android.summit.common.utils.IAppLinkRouter;
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
public class MainPresenter extends BasePresenter<IMainView, IMainInteractor, IMainWireframe> implements IMainPresenter {

    private IAppLinkRouter appLinkRouter;

    public MainPresenter(IMainInteractor interactor, IMainWireframe wireframe, IAppLinkRouter appLinkRouter) {
        super(interactor, wireframe);
        this.appLinkRouter = appLinkRouter;
        if (BuildConfig.FLAVOR.contains(Constants.FLAVOR_BETA) || BuildConfig.FLAVOR.contains(Constants.FLAVOR_DEV)) {
            trustEveryone();
        }
    }

    @Override
    public void updateNotificationCounter() {
        view.updateNotificationCounter(this.interactor.getNotReadNotificationsCount());
    }

    private boolean shouldShowMainView = false;

    @Override
    public void shouldShowMainView(){
        this.shouldShowMainView = true;
    }

    @Override
    public void showSettingsView() {
        wireframe.showSettingsView(view);
    }

    @Override
    public void onResume() {
        Log.d(Constants.LOG_TAG, "MainPresenter.onResume");
        super.onResume();
        if(shouldShowMainView){
            Log.d(Constants.LOG_TAG, "MainPresenter.onResume - showEventsView");
            shouldShowMainView = false;
            showEventsView();
        }
        updateNotificationCounter();
        checkDeepLinks();
        interactor.subscribeToPushNotifications();
        view.toggleMyProfileMenuItem(interactor.isMemberLogged());
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*PackageInfo pInfo = null;

        try {
            pInfo = view.getApplicationContext().getPackageManager().getPackageInfo(view.getApplicationContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            pInfo = null;
        }
        // check current build against storad build
        if(pInfo != null){
            int currentBuildNumber   = pInfo.versionCode;
            int installedBuildNumber = interactor.getInstalledBuildNumber();
            if(installedBuildNumber < currentBuildNumber){
                Log.d(Constants.LOG_TAG, String.format("MainPresenter.onCreate: old version %d - new version %d", installedBuildNumber, currentBuildNumber));
                interactor.setInstalledBuildNumber(currentBuildNumber);
                // if we are updating version and data is already loaded cleaning it ...
                if(interactor.isDataLoaded()) {
                    Log.d(Constants.LOG_TAG, "MainPresenter.onCreate: upgrading data storage");
                    this.disableDataUpdateService();
                    interactor.upgradeStorage();
                    return;
                }
            }
        }*/
        // normal flow, if data is loaded, enabled the data update services
        if(interactor.isDataLoaded()){
            enableDataUpdateService();
        }
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

        if (interactor.isLoggedInAndConfirmedAttendee()) {
            wireframe.showMyProfileView(view);
            return;
        }

        wireframe.showMemberOrderConfirmView(view);
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
        interactor.subscribeToPushNotifications();
    }

    @Override
    public void onLoggedIn() throws MissingMemberException {
        MemberDTO member = interactor.getCurrentMember();
        if(member == null) throw new MissingMemberException();
        view.setMemberName(member.getFullName());
        view.setLoginButtonText(view.getResources().getText(R.string.log_out).toString());
        view.setProfilePic(Uri.parse(member.getPictureUrl()));
        view.toggleMenu(false);
        interactor.subscribeToPushNotifications();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //stop data updates services
        disableDataUpdateService();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        view.toggleMenuLogo(newConfig.orientation != Configuration.ORIENTATION_LANDSCAPE);
    }

    @Override
    public boolean isSummitDataLoaded() {
        return interactor.isDataLoaded();
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
