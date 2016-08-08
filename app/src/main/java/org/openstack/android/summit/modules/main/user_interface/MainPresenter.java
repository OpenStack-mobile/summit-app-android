package org.openstack.android.summit.modules.main.user_interface;

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
import org.json.JSONObject;
import org.openstack.android.summit.BuildConfig;
import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.R;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.data_updates.DataUpdatesService;
import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.common.utils.DeepLinkInfo;
import org.openstack.android.summit.common.utils.IAppLinkRouter;
import org.openstack.android.summit.modules.main.IMainWireframe;
import org.openstack.android.summit.modules.main.business_logic.IMainInteractor;

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

    private boolean onSaveInstanceExecuted = false;
    private IAppLinkRouter appLinkRouter;

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (onSaveInstanceExecuted) {
                return;
            }

            try {
                if (interactor.isLoggedInAndConfirmedAttendee() || intent.getAction() == Constants.LOGGED_OUT_EVENT) {
                    wireframe.showEventsView(view);
                }
                else {
                    showMyProfileView();
                }
            } catch (Exception ex) {
                Crashlytics.logException(new Exception(String.format("Error opening fragment on login/logout notification. onSaveInstanceExecuted = %b ", onSaveInstanceExecuted), ex));
            }
        }
    };

    public MainPresenter(IMainInteractor interactor, IMainWireframe wireframe, IAppLinkRouter appLinkRouter) {
        super(interactor, wireframe);
        this.appLinkRouter = appLinkRouter;
    }

    @Override
    public void onResume() {
        super.onResume();
        DataUpdatesService.setServiceAlarm((Context)view, true);
    }

    @Override
    public void onPause() {
      super.onPause();
      if(DataUpdatesService.isServiceAlarmOn((Context)view)){
          DataUpdatesService.setServiceAlarm((Context)view, false);
      }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showMessageIfTriggeredByNotification();
        trustEveryone();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.LOGGED_IN_EVENT);
        intentFilter.addAction(Constants.LOGGED_OUT_EVENT);
        LocalBroadcastManager.getInstance(OpenStackSummitApplication.context).registerReceiver(messageReceiver, intentFilter);

        if (savedInstanceState == null) {
            showEventsView();
        }

        checkDeepLinks();
    }

    private boolean checkDeepLinks(){
        Intent intent = view.getIntent();

        if(intent != null){
            String action = intent.getAction();
            Uri url       = intent.getData();

            if(action == Intent.ACTION_VIEW && url != null){

                if(this.appLinkRouter.isDeepLink(url)){
                    // do routing ...
                    DeepLinkInfo deepLinkInfo = appLinkRouter.buildDeepLinkInfo(url);
                    if(deepLinkInfo.getAction() == DeepLinkInfo.ActionViewEvent){
                        if(!deepLinkInfo.hasParam()) return false;
                        this.wireframe.showEventDetail(deepLinkInfo.getParamAsInt(), this.view);
                        return true;
                    }
                    if(deepLinkInfo.getAction() == DeepLinkInfo.ActionViewSpeaker){
                        if(!deepLinkInfo.hasParam()) return false;
                        this.wireframe.showSpeakerProfile(deepLinkInfo.getParamAsInt(), this.view);
                        return true;
                    }
                    if(deepLinkInfo.getAction() == DeepLinkInfo.ActionViewSchedule){
                        return true;
                    }
                    if(deepLinkInfo.getAction() == DeepLinkInfo.ActionViewLocation){
                        if(!deepLinkInfo.hasParam()) return false;
                        return true;
                    }
                }
                // get the app link metadata
                AppLinkNavigation.navigateInBackground((MainActivity)view, url);
            }
        }
        return false;
    }

    private void showMessageIfTriggeredByNotification() {
        Intent intent = view.getIntent();
        if (intent != null) {
            if (intent.getExtras() != null) {
                try {
                    String parseData = intent.getExtras().getString("com.parse.Data");
                    if (parseData != null) {
                        JSONObject json     = new JSONObject(parseData);
                        view.showInfoMessage(json.getString("alert"), json.getString("title"));
                    }
                } catch (Exception e) {
                    Crashlytics.logException(e);
                    Log.e(Constants.LOG_TAG, e.getMessage(), e);
                    view.showErrorMessage(Constants.GENERIC_ERROR_MSG);
                }
            }
        }
    }

    public void showEventsView() {
        wireframe.showEventsView(view);
    }

    @Override
    public void showEventView(int eventId){
        wireframe.showEventDetail(eventId, view);
    }

    public void showMyProfileView() {
        if(!interactor.isDataLoaded()) {
            view.showInfoMessage(view.getResources().getString(R.string.no_summit_data_available));
            return;
        }

        if (interactor.isLoggedInAndConfirmedAttendee()) {
            wireframe.showMyProfileView(view);
        }
        else {
            wireframe.showMemberOrderConfirmView(view);
        }
    }

    public void showSpeakerListView() {
        if(!interactor.isDataLoaded()) {
            view.showInfoMessage(view.getResources().getString(R.string.no_summit_data_available));
            return;
        }

        wireframe.showSpeakerListView(view);
    }

    public void showSearchView(String searchTerm) {
        if(!interactor.isDataLoaded()) {
            view.showInfoMessage(view.getResources().getString(R.string.no_summit_data_available));
            return;
        }

        wireframe.showSearchView(searchTerm, view);
    }

    public void showAboutView() {
        if(!interactor.isDataLoaded()) {
            view.showInfoMessage(view.getResources().getString(R.string.no_summit_data_available));
            return;
        }

        wireframe.showAboutView(view);
    }

    public void showVenuesView() {
        if(!interactor.isDataLoaded()) {
            view.showInfoMessage(view.getResources().getString(R.string.no_summit_data_available));
            return;
        }

        wireframe.showVenuesView(view);
    }

    @Override
    public void onLoggedOut() {
        view.setMemberName("");
        view.setLoginButtonText(view.getResources().getText(R.string.log_in).toString());
        view.toggleMyProfileMenuItem(false);
        view.setProfilePic(null);
        interactor.subscribeAnonymousToPushNotifications();
    }

    @Override
    public void onLoggedIn() {
        String currentMemberName = interactor.getCurrentMemberName();
        Uri currentMemberProfilePicUri = interactor.getCurrentMemberProfilePictureUri();
        view.setMemberName(currentMemberName);
        view.setLoginButtonText(view.getResources().getText(R.string.log_out).toString());
        view.toggleMyProfileMenuItem(true);
        view.setProfilePic(currentMemberProfilePicUri);
        view.toggleMenu(false);
        if (interactor.isLoggedInAndConfirmedAttendee()) {
            interactor.subscribeLoggedInMemberToPushNotifications();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        onSaveInstanceExecuted = true;
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(OpenStackSummitApplication.context).unregisterReceiver(messageReceiver);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        view.toggleMenuLogo(newConfig.orientation != Configuration.ORIENTATION_LANDSCAPE);
    }

    @Override
    public boolean isSummitDataLoaded() {
        return interactor.isDataLoaded();
    }

    private void trustEveryone() {
        if (!BuildConfig.DEBUG) {
            return;
        }

        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new X509TrustManager(){
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {}
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {}
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }}}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(
                    context.getSocketFactory());
        } catch (Exception e) { // should never happen
            e.printStackTrace();
        }
    }
}
