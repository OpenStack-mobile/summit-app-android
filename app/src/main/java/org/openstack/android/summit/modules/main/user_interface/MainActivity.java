package org.openstack.android.summit.modules.main.user_interface;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.openstack.android.summit.SummitDataLoadingActivity;
import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.R;
import org.openstack.android.summit.SummitsListDataLoaderActivity;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.network.IReachability;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.common.user_interface.BadgeCounterMenuItemDecorator;
import org.openstack.android.summit.common.utils.RealmFactory;
import org.openstack.android.summit.dagger.components.ApplicationComponent;
import org.openstack.android.summit.dagger.modules.ActivityModule;
import org.openstack.android.summit.modules.main.exceptions.MissingMemberException;

import javax.inject.Inject;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressPie;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity
        extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, IMainView {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @Inject
    IMainPresenter presenter;

    @Inject
    ISecurityManager securityManager;

    @Inject
    IReachability reachability;

    private ACProgressPie progressDialog;
    private Button loginButton;
    private TextView memberNameTextView;
    private SimpleDraweeView memberProfileImageView;
    private ActionBarDrawerToggle toggle;
    private boolean userClickedLogout;
    private int selectedMenuItemId;
    private NavigationView navigationView;
    private boolean onLoginProcess   = false;
    private boolean onDataLoading    = false;
    private boolean loadedSummitList = false;

    private void cancelLoginProcess(){
        Log.d(Constants.LOG_TAG, "MainActivity.cancelLoginProcess");
        this.onLoginProcess = false;
    }

    private void initiateLoginProcess(){
        Log.d(Constants.LOG_TAG, "MainActivity.initiateLoginProcess");
        this.onLoginProcess = true;
    }

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {

                if (intent.getAction() == Constants.WIPE_DATE_EVENT) {
                    Log.d(Constants.LOG_TAG, "WIPE_DATE_EVENT");
                    launchSummitListDataLoadingActivity();
                }

                if (intent.getAction().contains(Constants.PUSH_NOTIFICATION_RECEIVED)) {
                    Log.d(Constants.LOG_TAG, "PUSH_NOTIFICATION_RECEIVED");
                    presenter.updateNotificationCounter();
                    return;
                }

                if (intent.getAction().contains(Constants.PUSH_NOTIFICATION_DELETED)) {
                    Log.d(Constants.LOG_TAG, "PUSH_NOTIFICATION_DELETED");
                    presenter.updateNotificationCounter();
                    return;
                }

                if (intent.getAction().contains(Constants.PUSH_NOTIFICATION_OPENED)) {
                    Log.d(Constants.LOG_TAG, "PUSH_NOTIFICATION_OPENED");
                    presenter.updateNotificationCounter();
                    return;
                }

                if (intent.getAction().contains(Constants.START_LOG_IN_EVENT)) {
                    Log.d(Constants.LOG_TAG, "START_LOG_IN_EVENT");
                    showActivityIndicator();
                    initiateLoginProcess();
                    return;
                }

                if (intent.getAction().contains(Constants.LOG_IN_ERROR_EVENT)) {
                    Log.d(Constants.LOG_TAG, "LOG_IN_ERROR_EVENT");
                    hideActivityIndicator();
                    showErrorMessage(intent.getExtras().getString(Constants.LOG_IN_ERROR_MESSAGE, Constants.GENERIC_ERROR_MSG));
                    cancelLoginProcess();
                    presenter.enableDataUpdateService();
                    return;
                }

                if (intent.getAction().contains(Constants.LOGGED_IN_EVENT)) {
                    try {
                        Log.d(Constants.LOG_TAG, "LOGGED_IN_EVENT");
                        presenter.onLoggedIn();
                        //show my profile tab
                        navigationView.getMenu().findItem(R.id.nav_my_profile).setVisible(true);
                        // set events tab ...
                        navigationView.getMenu().findItem(R.id.nav_events).setChecked(true);
                        presenter.showEventsView();
                    } catch (MissingMemberException ex1) {
                        Crashlytics.logException(ex1);
                        Log.w(Constants.LOG_TAG, ex1.getMessage());
                        showErrorMessage(getResources().getString(R.string.login_error_message));
                    } finally {
                        presenter.enableDataUpdateService();
                        hideActivityIndicator();
                        cancelLoginProcess();
                    }
                    return;
                }

                if (intent.getAction().contains(Constants.LOGGED_OUT_EVENT)) {
                    try {
                        Log.d(Constants.LOG_TAG, "LOGGED_OUT_EVENT");
                        onLoggedOut();
                        if (!userClickedLogout) {
                            showInfoMessage(getResources().getString(R.string.session_expired_message));
                        }
                        userClickedLogout = false;
                        presenter.showEventsView();
                    }
                    finally {
                        if(intent.getBooleanExtra(Constants.EXTRA_ENABLE_DATA_UPDATES_AFTER_LOGOUT, false))
                            presenter.enableDataUpdateService();
                        cancelLoginProcess();
                    }
                    return;
                }

                if (intent.getAction().contains(Constants.LOG_IN_CANCELLED_EVENT)) {
                    Log.d(Constants.LOG_TAG, "LOG_IN_CANCELLED_EVENT");
                    hideActivityIndicator();
                    cancelLoginProcess();
                    return;
                }

            } catch (Exception ex) {
                Crashlytics.logException(new Exception(String.format("Action %s", intent.getAction()), ex));
            }
        }
    };

    private static final int DATA_LOAD_REQUEST = 1;  // The request code
    private static final int SUMMITS_LIST_DATA_LOAD_REQUEST =2;

    private void launchInitialDataLoadingActivity() {
        if (!onDataLoading) {
            onDataLoading = true;
            // disable data updates ...
            presenter.disableDataUpdateService();
            Intent intent = new Intent(MainActivity.this, SummitDataLoadingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            Log.i(Constants.LOG_TAG, "starting SummitDataLoadingActivity ...");
            startActivityForResult(intent, DATA_LOAD_REQUEST);
        }
    }

    private void launchSummitListDataLoadingActivity(){
        if(loadedSummitList) return;
        onDataLoading = true;
        // disable data updates ...
        presenter.disableDataUpdateService();

        Intent intent = new Intent(MainActivity.this, SummitsListDataLoaderActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        Log.i(Constants.LOG_TAG, "starting SummitsListDataLoaderActivity ...");
        startActivityForResult(intent, SUMMITS_LIST_DATA_LOAD_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == DATA_LOAD_REQUEST) {
            onDataLoading = false;
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Log.i(Constants.LOG_TAG, "MainActivity.onActivityResult: Summit Data Loaded!");
                //re enable data update service
                presenter.enableDataUpdateService();
                presenter.shouldShowMainView();
            }
        }
        if(requestCode == SUMMITS_LIST_DATA_LOAD_REQUEST){
            onDataLoading    = false;
            loadedSummitList = true;
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Log.i(Constants.LOG_TAG, "MainActivity.onActivityResult: Summit Data Loaded!");
                //re enable data update service
                presenter.enableDataUpdateService();
            }
            if(resultCode == SummitsListDataLoaderActivity.RESULT_OK_FIRE_SUMMIT_DATA_LOADING){
                launchInitialDataLoadingActivity();
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(Constants.LOG_TAG, "MainActivity.onCreate");
        super.onCreate(savedInstanceState);
        getApplicationComponent().inject(this);
        presenter.setView(this);
        // kill any data updates enabled
        presenter.disableDataUpdateService();
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

        presenter.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBarSetup();
        NavigationMenuSetup(savedInstanceState);
        if (savedInstanceState == null) {
            // first time
            Log.d(Constants.LOG_TAG, "MainActivity.onCreate - savedInstanceState == null");
            presenter.shouldShowMainView();
        }

        checkPlayServices();
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private void checkPlayServices() {
        GoogleApiAvailability googleApiAvailability =  GoogleApiAvailability.getInstance();

        int success = googleApiAvailability.isGooglePlayServicesAvailable(this);

        if(success != ConnectionResult.SUCCESS)
        {
            googleApiAvailability.makeGooglePlayServicesAvailable(this);
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        if (onLoginProcess) {
            Log.d(Constants.LOG_TAG, "MainActivity.onStart - its on logging process ...");
            cancelLoginProcess();
            showActivityIndicator();
            return;
        }
        securityManager.init();
        Log.d(Constants.LOG_TAG, "MainActivity.onStart - its on regular process ...");
    }

    @Override
    protected void onResume() {
        try {

            Log.d(Constants.LOG_TAG, "MainActivity.onResume");
            toggleMenuLogo(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);

            super.onResume();
            checkPlayServices();

            Intent intent = getIntent();
            if(intent != null
                    && intent.getBooleanExtra(Constants.START_EXTERNAL_LOGIN, false)
                    && !securityManager.isLoggedIn()
                    && !onLoginProcess){
                intent.removeExtra(Constants.START_EXTERNAL_LOGIN);
                this.loginButton.performClick();
                return;
            }

            presenter.onResume();
            setupNavigationIcons();

        } catch (Exception ex) {
            Crashlytics.logException(ex);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.onSaveInstanceState(outState);
        Log.d(Constants.LOG_TAG, "MainActivity.onSaveInstanceState");
        outState.putBoolean(Constants.ON_LOGGING_PROCESS, onLoginProcess);
        outState.putBoolean(Constants.ON_DATA_LOADING_PROCESS, onDataLoading);
        outState.putBoolean(Constants.LOADED_SUMMITS_LIST, loadedSummitList);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(Constants.LOG_TAG, "MainActivity.onRestoreInstanceState");
        onLoginProcess   = savedInstanceState.getBoolean(Constants.ON_LOGGING_PROCESS, false);
        onDataLoading    = savedInstanceState.getBoolean(Constants.ON_DATA_LOADING_PROCESS, false);
        loadedSummitList = savedInstanceState.getBoolean(Constants.LOADED_SUMMITS_LIST, false);
    }

    @Override
    protected void onPause() {
        Log.d(Constants.LOG_TAG, "MainActivity.onPause");
        super.onPause();
        presenter.onPause();
    }

    @Override
    public void onDestroy() {
        Log.d(Constants.LOG_TAG, "MainActivity.onDestroy");

        super.onDestroy();
        presenter.onDestroy();
        // unbind local broadcast receiver
        LocalBroadcastManager.getInstance(OpenStackSummitApplication.context).unregisterReceiver(messageReceiver);
        hideActivityIndicator();
        //close realm session for current thread
        RealmFactory.closeSession();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        presenter.onConfigurationChanged(newConfig);
        setupNavigationIcons();
    }

    public void toggleMenuLogo(boolean show) {
        ImageView footerLogo = (ImageView) findViewById(R.id.footer_logo);
        footerLogo.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void ActionBarSetup() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                presenter.onOpenedNavigationMenu();
            }
        };

        drawer.setDrawerListener(toggle);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);
        //calling sync state is necessary or else your hamburger icon wont show up
        toggle.syncState();

        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    onBackPressed();
                }
            }
        });

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
              setupNavigationIcons();
              if(getSupportFragmentManager().getBackStackEntryCount() > 0) {
                  int id      = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getId();
                  String name = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
              }
            }
        });
    }

    private void setupNavigationIcons() {
        toggle.setDrawerIndicatorEnabled(getSupportFragmentManager().getBackStackEntryCount() == 0);
        // Enable Up button only  if there are entries in the back stack ( back arrow )
        getSupportActionBar().setDisplayHomeAsUpEnabled(getSupportFragmentManager().getBackStackEntryCount() > 0);
        toggle.syncState();
    }

    private void NavigationMenuSetup(Bundle savedInstanceState) {
        try {
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            navigationView.getMenu().getItem(0).setChecked(true);
            LinearLayout headerView = (LinearLayout) navigationView.inflateHeaderView(R.layout.nav_header_main);
            loginButton             = (Button) headerView.findViewById(R.id.login_button);

            loginButton.setOnClickListener(v -> {
                showActivityIndicator();
                if (!reachability.isNetworkingAvailable(MainActivity.this)) {
                    hideActivityIndicator();
                    showErrorMessage(getResources().getString(R.string.login_disallowed_no_connectivity));
                    return;
                }

                presenter.disableDataUpdateService();

                if (!presenter.isSummitDataLoaded()) {
                    hideActivityIndicator();
                    showInfoMessage(getResources().getString(R.string.login_disallowed_no_data));
                    launchInitialDataLoadingActivity();
                    return;
                }

                // LOGIN
                if (!securityManager.isLoggedIn()) {
                    securityManager.login(MainActivity.this);
                    return;
                }

                // LOGOUT
                userClickedLogout = true;

                securityManager.logout(true);
                hideActivityIndicator();
            });

            memberNameTextView     = (TextView) headerView.findViewById(R.id.member_name_textview);
            memberProfileImageView = (SimpleDraweeView) headerView.findViewById(R.id.member_profile_pic_imageview);
            memberProfileImageView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (!reachability.isNetworkingAvailable(MainActivity.this)) {
                                showErrorMessage(getResources().getString(R.string.login_disallowed_no_connectivity));
                                return;
                            }

                            presenter.disableDataUpdateService();

                            if (!presenter.isSummitDataLoaded()) {
                                showInfoMessage(getResources().getString(R.string.login_disallowed_no_data));
                                launchInitialDataLoadingActivity();
                                return;
                            }

                            // LOGIN
                            if (!securityManager.isLoggedIn()) {
                                securityManager.login(MainActivity.this);
                                return;
                            }

                            // go to my summit ?
                            closeMenuDrawer();
                            presenter.showMyProfileView();
                        }
                    }
            );
            EditText searchText = (EditText) headerView.findViewById(R.id.nav_header_search_edittext);
            searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    String searchTerm = v.getText().toString();
                    if (actionId == EditorInfo.IME_ACTION_DONE && !searchTerm.isEmpty()) {
                        presenter.showSearchView(searchTerm);
                    }
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                    v.setText("");
                    return false;
                }
            });

            if (securityManager.isLoggedIn()) {

                presenter.onLoggedIn();

            }
        } catch (Exception ex) {
            Crashlytics.logException(ex);
        }
    }

    @Override
    public void onBackPressed() {
        try {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

            if (!drawer.isDrawerOpen(GravityCompat.START) && getFragmentManager().getBackStackEntryCount() == 0) {
                // set events tab ...
                super.onBackPressed();
            }

            int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
            // default checked events ...
            int menuItemId     = R.id.nav_events;
            if(backStackCount > 0){

                String currentEntryName = getSupportFragmentManager().getBackStackEntryAt(backStackCount - 1).getName();

                if(currentEntryName != null && currentEntryName.equals("nav_venues")){
                    menuItemId = R.id.nav_venues;
                }
                if(currentEntryName != null && currentEntryName.equals("nav_about")){
                    menuItemId = R.id.nav_about;
                }
                if(currentEntryName != null && currentEntryName.equals("nav_notifications")){
                    menuItemId = R.id.nav_notifications;
                }
                if(currentEntryName != null && currentEntryName.equals("nav_my_profile")){
                    menuItemId = R.id.nav_my_profile;
                }
                if(currentEntryName != null && currentEntryName.equals("nav_settings")){
                    menuItemId = R.id.nav_settings;
                }
                if(currentEntryName != null && currentEntryName.equals("nav_speakers")){
                    menuItemId = R.id.nav_speakers;
                }
            }
            navigationView.getMenu().findItem(menuItemId).setChecked(true);

            if (backStackCount == 1) {
                return;
            }

            drawer.closeDrawer(GravityCompat.START);
            getFragmentManager().popBackStack();
        }
        catch (Exception ex){
            Log.e(Constants.LOG_TAG, ex.getMessage(), ex);
            Crashlytics.logException(ex);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        selectedMenuItemId = item.getItemId();

        if (selectedMenuItemId == R.id.nav_events) {
            presenter.showEventsView();
        } else if (selectedMenuItemId == R.id.nav_speakers) {
            presenter.showSpeakerListView();
        } else if (selectedMenuItemId == R.id.nav_venues) {
            presenter.showVenuesView();
        } else if (selectedMenuItemId == R.id.nav_my_profile) {
            presenter.showMyProfileView();
        } else if (selectedMenuItemId == R.id.nav_notifications) {
            presenter.showNotificationView();
        } else if (selectedMenuItemId == R.id.nav_settings) {
            presenter.showSettingsView();
        } else if (selectedMenuItemId == R.id.nav_about) {
            presenter.showAboutView();
        }

        closeMenuDrawer();
        return true;
    }

    private void closeMenuDrawer(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public ApplicationComponent getApplicationComponent() {
        return ((OpenStackSummitApplication) getApplication()).getApplicationComponent();
    }

    public ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }

    public void setLoginButtonText(String text) {
        loginButton.setText(text);
    }

    @Override
    public void setMemberName(String text) {
        memberNameTextView.setText(text);
    }

    @Override
    public void setProfilePic(Uri uri) {
        memberProfileImageView.setImageURI(uri);
    }

    public void toggleMyProfileMenuItem(boolean show) {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().findItem(R.id.nav_my_profile).setVisible(show);
    }

    private void onLoggedOut() {
        Log.i(Constants.LOG_TAG, "doing log out");
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().findItem(R.id.nav_my_profile).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_events).setChecked(true);
        presenter.onLoggedOut();
    }

    private void onError(String message) {
        showErrorMessage(message);
        hideActivityIndicator();
    }

    public void setMenuItemChecked(int menuItemId) {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView == null) return;
        MenuItem menuItem = navigationView.getMenu().findItem(menuItemId);
        if (menuItem == null) return;
        menuItem.setChecked(true);
    }

    public void showErrorMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MainActivity context = MainActivity.this;
                if(!context.isFinishing()) {
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText(message)
                            .show();
                }
            }
        });
    }

    @Override
    public void showInfoMessage(String message) {
        showInfoMessage(message, "info");
    }

    @Override
    public void showInfoMessage(final String message, final String title) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MainActivity context = MainActivity.this;
                if(!context.isFinishing()) {
                    new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(title)
                            .setContentText(message)
                            .show();
                }

            }
        });
    }

    @Override
    public void toggleMenu(boolean show) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (show) {
            drawer.openDrawer(GravityCompat.START);
        } else {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void updateNotificationCounter(Long value) {
        BadgeCounterMenuItemDecorator badge = new BadgeCounterMenuItemDecorator
                (
                        navigationView.getMenu().findItem(R.id.nav_notifications),
                        R.id.txt_counter
                );
        if (value > 0) {
            badge.updateCounter(value.toString());
            return;
        }
        badge.hideCounter();
    }

    @Override
    public void setTitle(String title) {
        setTitle(title);
    }

    @Override
    public void showActivityIndicator(int delay) {
        showActivityIndicator();
    }

    public void showActivityIndicator() {

        runOnUiThread(() -> {
            if (progressDialog != null) {
                hideActivityIndicator();
            }
            Log.d(Constants.LOG_TAG, "MainActivity.showActivityIndicator");
            progressDialog = new ACProgressPie.Builder(MainActivity.this)
                    .ringColor(Color.WHITE)
                    .pieColor(Color.WHITE)
                    .updateType(ACProgressConstant.PIE_AUTO_UPDATE)
                    .build();
            progressDialog.setCancelable(false);
            progressDialog.show();
        });
    }

    public void hideActivityIndicator() {

        runOnUiThread(() -> {
            if (progressDialog != null) {
                Log.d(Constants.LOG_TAG, "MainActivity.hideActivityIndicator");
                progressDialog.dismiss();
                progressDialog = null;
            }
        });

    }
}