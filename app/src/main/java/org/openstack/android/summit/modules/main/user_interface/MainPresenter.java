package org.openstack.android.summit.modules.main.user_interface;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

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
    public MainPresenter(IMainInteractor interactor, IMainWireframe wireframe) {
        super(interactor, wireframe);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        trustEveryone();

        if (savedInstanceState == null) {
            showEventsView();
        }
    }

    /* TODO: This should be gone before going live!!!!*/
    private void trustEveryone() {
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

    public void showEventsView() {
        wireframe.showEventsView(view);
    }

    public void showMyProfileView() {
        wireframe.showMyProfileView(view);
    }

    public void showSpeakerListView() {
        wireframe.showSpeakerListView(view);
    }

    public void showSearchView(String searchTerm) {
        wireframe.showSearchView(searchTerm, view);
    }

    public void showVenuesView() {
        wireframe.showVenuesView(view);
    }

    @Override
    public void onLoggedOut() {
        Intent intent = new Intent(Constants.LOGGED_OUT_EVENT);
        // You can also include some extra data.
        LocalBroadcastManager.getInstance(OpenStackSummitApplication.context).sendBroadcast(intent);

        view.setMemberName("");
        view.setLoginButtonText(view.getResources().getText(R.string.log_in).toString());
        view.toggleMyProfileMenuItem(false);
        view.setProfilePic(null);
        interactor.subscribeAnonymousToPushNotifications();
    }

    @Override
    public void onLoggedIn() {
        Intent intent = new Intent(Constants.LOGGED_IN_EVENT);
        // You can also include some extra data.
        LocalBroadcastManager.getInstance(OpenStackSummitApplication.context).sendBroadcast(intent);
        String currentMemberName = interactor.getCurrentMemberName();
        Uri currentMemberProfilePicUri = interactor.getCurrentMemberProfilePictureUri();
        view.setMemberName(currentMemberName);
        view.setLoginButtonText(view.getResources().getText(R.string.log_out).toString());
        view.toggleMyProfileMenuItem(true);
        view.setProfilePic(currentMemberProfilePicUri);
        interactor.subscribeLoggedInMemberToPushNotifications();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        view.toggleMenuLogo(newConfig.orientation != Configuration.ORIENTATION_LANDSCAPE);
    }
}
