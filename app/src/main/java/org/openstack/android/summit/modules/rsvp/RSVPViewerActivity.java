package org.openstack.android.summit.modules.rsvp;

import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.HttpAuthHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.openstack.android.summit.BuildConfig;
import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.R;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.security.IConfigurationParamsManager;
import org.openstack.android.summit.dagger.components.ApplicationComponent;

import javax.inject.Inject;

/**
 * Created by sebastian on 9/5/2016.
 */
public class RSVPViewerActivity extends Activity {

    @Inject
    IConfigurationParamsManager configurationParamsManager;

    public ApplicationComponent getApplicationComponent() {
        return ((OpenStackSummitApplication) getApplication()).getApplicationComponent();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        getApplicationComponent().inject(this);
        // Initialise the WebView
        WebView webView = (WebView) findViewById(R.id.WebView);
        webView.getSettings().setJavaScriptEnabled(true);
        CookieManager cookieManager = CookieManager.getInstance();
        // persists the oookies ...
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(webView, true);
        }
        cookieManager.setAcceptCookie(true);

        Uri url = getIntent().getData();

        // set the basic auth
        if (BuildConfig.FLAVOR.contains(Constants.FLAVOR_BETA) || BuildConfig.FLAVOR.contains(Constants.FLAVOR_DEV)) {
            webView.setWebViewClient(new MyWebViewClient());
        }
        Uri.Builder builder = url.buildUpon();
        webView.loadUrl(builder.appendQueryParameter("mobile_app", "1").build().toString());
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
            handler.proceed(
                    configurationParamsManager.findConfigParamBy(Constants.BASIC_AUTH_USER),
                    configurationParamsManager.findConfigParamBy(Constants.BASIC_AUTH_PASS)
            );
        }
    }
}
