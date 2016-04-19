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
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.BuildConfig;
import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.R;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.MemberDTO;
import org.openstack.android.summit.common.user_interface.BasePresenter;
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

/**
 * Created by Claudio Redi on 2/12/2016.
 */
public class MainPresenter extends BasePresenter<IMainView, IMainInteractor, IMainWireframe> implements IMainPresenter {
    private boolean onSaveInstanceExecuted = false;

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

    public MainPresenter(IMainInteractor interactor, IMainWireframe wireframe) {
        super(interactor, wireframe);
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
    }

    private void showMessageIfTriggeredByNotification() {
        Intent intent = view.getIntent();
        if (intent != null) {
            if (intent.getExtras() != null) {
                try {
                    String parseData = intent.getExtras().getString("com.parse.Data");
                    if (parseData != null) {
                        JSONObject json = new JSONObject(parseData);
                        String notificationText = json.getString("alert");
                        view.showInfoMessage(notificationText);
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
