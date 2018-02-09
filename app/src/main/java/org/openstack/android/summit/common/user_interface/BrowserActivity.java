package org.openstack.android.summit.common.user_interface;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.BuildConfig;
import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.R;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.security.IConfigurationParamsManager;
import org.openstack.android.summit.dagger.components.ApplicationComponent;
import org.openstack.android.summit.modules.main.user_interface.MainActivity;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by smarcet on 3/23/17.
 */

public class BrowserActivity extends BaseActivity {

    @Inject
    IConfigurationParamsManager configurationParamsManager;

    public ApplicationComponent getApplicationComponent() {
        return ((OpenStackSummitApplication) getApplication()).getApplicationComponent();
    }

    @BindView(R.id.WebView)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            getApplicationComponent().inject(this);
            setContentView(R.layout.rsvp_viewer);
            unbinder = ButterKnife.bind(this);

            Uri url = getIntent().getData();

            if (url == null) {

                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);

                builder.setTitle(R.string.generic_error_title)
                       .setMessage(R.string.generic_error_message)
                       .setPositiveButton(R.string.generic_error_message_ok,  (dialog, id) -> dialog.dismiss() )
                       .create()
                       .show();

                return;
            }
            // Initialise the WebView

            webView.setWebViewClient(new BrowserActivity.CustomWebViewClient(this));
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setDefaultTextEncodingName("utf-8");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                WebView.setWebContentsDebuggingEnabled(true);
            }

            CookieManager cookieManager = CookieManager.getInstance();

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
                cookieManager.setAcceptThirdPartyCookies(webView, true);
                webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
                webView.getSettings().setAllowFileAccessFromFileURLs(true);
                cookieManager.flush();
            }

            cookieManager.setAcceptCookie(true);
            Uri.Builder builder = url.buildUpon();
            String link = builder.build().toString();
            Log.d(Constants.LOG_TAG, " opening url " + link);
            webView.loadUrl(link);

        } catch (Exception ex) {
            Crashlytics.logException(ex);
            Log.e(Constants.LOG_TAG, ex.getMessage());
        }
    }

    private static class CustomWebViewClient extends WebViewClient {

        private WeakReference<BrowserActivity> activity;

        public CustomWebViewClient(BrowserActivity activity) {
            this.activity = new WeakReference<>(activity);
        }

        @Override
        public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
            // set the basic auth
            if (BuildConfig.FLAVOR.contains(Constants.FLAVOR_BETA) || BuildConfig.FLAVOR.contains(Constants.FLAVOR_DEV)) {
                handler.proceed(
                        activity.get().configurationParamsManager.findConfigParamBy(Constants.BASIC_AUTH_USER),
                        activity.get().configurationParamsManager.findConfigParamBy(Constants.BASIC_AUTH_PASS)
                );
            }
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {

        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }

        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(activity.get());
            builder.setMessage(R.string.notification_error_ssl_cert_invalid);
            builder.setPositiveButton("continue", (dialog, which) -> handler.proceed());
            builder.setNegativeButton("cancel", (dialog, which) -> handler.cancel());
            final AlertDialog dialog = builder.create();
            dialog.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            WebView webView = (WebView) activity.get().findViewById(R.id.WebView);
            webView.setVisibility(View.VISIBLE);
            activity.get().hideActivityIndicator();
        }

        @Override
        public void onPageStarted(WebView view, String urlString, Bitmap favicon) {
            super.onPageStarted(view, urlString, favicon);
            WebView webView = (WebView) activity.get().findViewById(R.id.WebView);
            webView.setVisibility(View.INVISIBLE);
            activity.get().showActivityIndicator();
        }

        @Override
        @SuppressWarnings("deprecation")
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            final Uri uri = Uri.parse(url);
            return handleUri(uri);
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            final Uri uri = request.getUrl();
            return handleUri(uri);
        }

        private boolean handleUri(final Uri uri) {
            final String scheme = uri.getScheme();
            final String host   = uri.getHost();
            if( scheme.startsWith("http:") || scheme.startsWith("https:") ) {
                return false;
            }
            if(!scheme.startsWith("org.openstack.android.summit")) return false;
            Log.i(Constants.LOG_TAG, String.format("firing new intent for url %s", uri.toString()));
            // Otherwise allow the OS to handle it
            Intent intent = new Intent(Intent.ACTION_VIEW, uri, activity.get(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.get().startActivity(intent);
            return true;
        }

    }
}
