package org.openstack.android.summit;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.security.*;
import org.openstack.android.summit.dagger.components.ApplicationComponent;
import org.openstack.android.summit.dagger.modules.ActivityModule;
import org.openstack.android.summit.modules.events.IEventsWireframe;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.ScheduledFuture;

import javax.inject.Inject;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ISecurityManagerListener {

    @Inject
    IEventsWireframe eventsWireframe;

    @Inject
    ISecurityManager securityManager;

    private ScheduledFuture<?> activityIndicatorTask;
    private ACProgressFlower progressDialog;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        LinearLayout headerView = (LinearLayout)navigationView.inflateHeaderView(R.layout.nav_header_main);
        loginButton = (Button)headerView.findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!securityManager.isLoggedIn()) {
                    showActivityIndicator();
                    securityManager.login(MainActivity.this);
                }
                else {
                    securityManager.logout();
                }
            }
        });

        getApplicationComponent().inject(this);
        eventsWireframe.presentEventsView(this);

        trustEveryone();

        securityManager.setDelegate(this);

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                toggle.setDrawerIndicatorEnabled(getSupportFragmentManager().getBackStackEntryCount() == 0);
                getSupportActionBar().setDisplayHomeAsUpEnabled(getSupportFragmentManager().getBackStackEntryCount() > 0);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (!drawer.isDrawerOpen(GravityCompat.START) && getFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        }

        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            return;
        }

        drawer.closeDrawer(GravityCompat.START);
        getFragmentManager().popBackStack();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        /*if (id == R.id.nav_camara) {
            menuItem = item;
            if (!securityManager.isLoggedIn()) {
                showActivityIndicator();
                securityManager.login(this);
            }
            else {
                securityManager.logout();
            }
        } else if (id == R.id.nav_gallery) {
            //generalScheduleWireframe.presentGeneralScheduleView(this);
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public ApplicationComponent getApplicationComponent() {
        return ((OpenStackSummitApplication)getApplication()).getApplicationComponent();
    }

    public ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }

    @Override
    public void onLoggedIn() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(3).setVisible(true);

        loginButton.setText(getResources().getText(R.string.log_out));

        Intent intent = new Intent(Constants.LOGGED_IN_EVENT);
        // You can also include some extra data.
        LocalBroadcastManager.getInstance(OpenStackSummitApplication.context).sendBroadcast(intent);
        hideActivityIndicator();

    }

    @Override
    public void onLoggedOut() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(3).setVisible(false);

        loginButton.setText(getResources().getText(R.string.log_in));

        Intent intent = new Intent(Constants.LOGGED_OUT_EVENT);
        // You can also include some extra data.
        LocalBroadcastManager.getInstance(OpenStackSummitApplication.context).sendBroadcast(intent);
    }

    @Override
    public void onError(String message) {

    }

    public void showActivityIndicator() {
         progressDialog = new ACProgressFlower.Builder(MainActivity.this)
                 .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                 .themeColor(Color.WHITE)
                 .text("Please wait...")
                 .fadeColor(Color.DKGRAY).build();
         progressDialog.show();
    }

    public void hideActivityIndicator() {
        if (progressDialog != null) {
            progressDialog.hide();
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

}