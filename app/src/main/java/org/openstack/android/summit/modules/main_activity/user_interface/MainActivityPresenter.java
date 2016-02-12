package org.openstack.android.summit.modules.main_activity.user_interface;

import android.os.Bundle;

import org.openstack.android.summit.common.security.ISecurityManagerListener;
import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.common.user_interface.IBaseView;
import org.openstack.android.summit.modules.main_activity.IMainActivityWireframe;
import org.openstack.android.summit.modules.main_activity.business_logic.IMainActivityInteractor;

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
public class MainActivityPresenter extends BasePresenter<IMainView, IMainActivityInteractor, IMainActivityWireframe> implements IMainActivityPresenter {
    public MainActivityPresenter(IMainActivityInteractor interactor, IMainActivityWireframe wireframe) {
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

}
