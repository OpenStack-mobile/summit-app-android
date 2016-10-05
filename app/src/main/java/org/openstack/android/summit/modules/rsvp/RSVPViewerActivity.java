package org.openstack.android.summit.modules.rsvp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.BuildConfig;
import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.R;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.security.IConfigurationParamsManager;
import org.openstack.android.summit.dagger.components.ApplicationComponent;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import cn.pedant.SweetAlert.SweetAlertDialog;

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
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.rsvp_viewer);
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

            if (url == null) {
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText(getResources().getString(R.string.generic_error_message))
                        .show();
                return;
            }

            webView.setWebViewClient(new CustomWebViewClient(this));
            Uri.Builder builder = url.buildUpon();
            String link = builder.appendQueryParameter("mobile_app", "1").build().toString();
            Log.d(Constants.LOG_TAG, " opening RSVP " + link);
            webView.loadUrl(link);
        } catch (Exception ex) {
            Crashlytics.logException(ex);
            Log.e(Constants.LOG_TAG, ex.getMessage());
        }
    }

    private static class CustomWebViewClient extends WebViewClient {

        private WeakReference<RSVPViewerActivity> activity;

        public CustomWebViewClient(RSVPViewerActivity activity) {
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
            super.onReceivedHttpError(view, request, errorResponse);
            WebView webView = (WebView) activity.get().findViewById(R.id.WebView);
            ProgressBar progressBar = (ProgressBar) activity.get().findViewById(R.id.ProgressBar);
            webView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(activity.get());
            builder.setMessage(R.string.notification_error_ssl_cert_invalid);
            builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.proceed();
                }
            });
            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.cancel();
                }
            });
            final AlertDialog dialog = builder.create();
            dialog.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            WebView webView = (WebView) activity.get().findViewById(R.id.WebView);
            ProgressBar progressBar = (ProgressBar) activity.get().findViewById(R.id.ProgressBar);
            webView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onPageStarted(WebView view, String urlString, Bitmap favicon) {
            super.onPageStarted(view, urlString, favicon);
            WebView webView = (WebView) activity.get().findViewById(R.id.WebView);
            ProgressBar progressBar = (ProgressBar) activity.get().findViewById(R.id.ProgressBar);
            webView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

    }
}
