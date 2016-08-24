package org.openstack.android.summit.modules.main.user_interface;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
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

import com.facebook.drawee.view.SimpleDraweeView;

import org.openstack.android.summit.BuildConfig;
import org.openstack.android.summit.InitialDataLoadingActivity;
import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.R;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.network.IReachability;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.common.security.ISecurityManagerListener;
import org.openstack.android.summit.common.user_interface.BadgeCounterMenuItemDecorator;
import org.openstack.android.summit.dagger.components.ApplicationComponent;
import org.openstack.android.summit.dagger.modules.ActivityModule;

import javax.inject.Inject;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity
        extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ISecurityManagerListener, IMainView {

    @Inject
    IMainPresenter presenter;

    @Inject
    // TODO: this should be moved to interactor. It's necessary to know how to deal with the Activiy parameter on login
            ISecurityManager securityManager;

    @Inject
    IReachability reachability;

    private ACProgressFlower progressDialog;
    private Button loginButton;
    private TextView memberNameTextView;
    private SimpleDraweeView memberProfileImageView;
    private ActionBarDrawerToggle toggle;
    private boolean userClickedLogout;
    private int selectedMenuItemId;
    private NavigationView navigationView;

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == Constants.LOGGED_OUT_EVENT && !userClickedLogout) {
                showInfoMessage("Your login session expired");
                onLoggedOut();
            }

            if (intent.getAction() == Constants.WIPE_DATE_EVENT) {
                launchInitialDataLoadingActivity();
            }
            userClickedLogout = false;
        }
    };

    static final int DATA_LOAD_REQUEST = 1;  // The request code
    static private boolean runningDataLoading = false;
    private final Object dataLoadingActivityLock = new Object();

    private void launchInitialDataLoadingActivity() {
        synchronized (dataLoadingActivityLock) {
            if (runningDataLoading) return;
            if (!presenter.isSummitDataLoaded()) {
                runningDataLoading = true;
                Intent intent = new Intent(MainActivity.this, InitialDataLoadingActivity.class);
                Log.i(Constants.LOG_TAG, "starting InitialDataLoadingActivity ...");
                startActivityForResult(intent, DATA_LOAD_REQUEST);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getApplicationComponent().inject(this);
        presenter.setView(this);
        presenter.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        securityManager.init();
        ActionBarSetup();
        NavigationMenuSetup(savedInstanceState);

        securityManager.setDelegate(this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.LOGGED_OUT_EVENT);

        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction(Constants.WIPE_DATE_EVENT);

        LocalBroadcastManager.getInstance(OpenStackSummitApplication.context).registerReceiver(messageReceiver, intentFilter);
        LocalBroadcastManager.getInstance(OpenStackSummitApplication.context).registerReceiver(messageReceiver, intentFilter2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == DATA_LOAD_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                synchronized (dataLoadingActivityLock) {
                    runningDataLoading = false;
                }
                Log.i(Constants.LOG_TAG, "Summit Data Loaded!");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
        hideActivityIndicator();
        launchInitialDataLoadingActivity();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    public void onDestroy() {
        // Unregister since the activity is about to be closed.
        // This is somewhat like [[NSNotificationCenter defaultCenter] removeObserver:name:object:]
        LocalBroadcastManager.getInstance(OpenStackSummitApplication.context).unregisterReceiver(messageReceiver);
        super.onDestroy();
        hideActivityIndicator();
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
        //calling sync state is neccesary or else your hamburger icon wont show up
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

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        LinearLayout headerView = (LinearLayout) navigationView.inflateHeaderView(R.layout.nav_header_main);
        loginButton = (Button) headerView.findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!reachability.isNetworkingAvailable(MainActivity.this)) {
                    showErrorMessage(getResources().getString(R.string.login_disallowed_no_connectivity));
                    return;
                }

                if (!presenter.isSummitDataLoaded()) {
                    showInfoMessage(getResources().getString(R.string.login_disallowed_no_data));
                    return;
                }

                if (!securityManager.isLoggedIn()) {
                    showActivityIndicator();
                    securityManager.login(MainActivity.this);
                } else {
                    userClickedLogout = true;
                    securityManager.logout();
                }
            }
        });

        memberNameTextView     = (TextView) headerView.findViewById(R.id.member_name_textview);
        memberProfileImageView = (SimpleDraweeView) headerView.findViewById(R.id.member_profile_pic_imageview);

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
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        presenter.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState, outPersistentState);
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
        } else if (selectedMenuItemId == R.id.nav_about) {
            presenter.showAboutView();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    @Override
    public void onStartedLoginProcess() {
        showActivityIndicator();
    }

    @Override
    public void onLoggedIn() {
        presenter.onLoggedIn();
        navigationView.getMenu().findItem(R.id.nav_my_profile).setVisible(true);
        navigationView.getMenu().findItem(R.id.nav_my_profile).setChecked(true);
        hideActivityIndicator();
    }

    @Override
    public void onLoggedOut() {
        Log.i(Constants.LOG_TAG, "doing log out");
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().findItem(R.id.nav_my_profile).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_events).setChecked(true);
        presenter.onLoggedOut();
    }


    public void setMenuItemChecked(int menuItemId){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if(navigationView == null) return;
        MenuItem menuItem = navigationView.getMenu().findItem(menuItemId);
        if(menuItem == null) return;
        menuItem.setChecked(true);
    }

    @Override
    public void onError(String message) {
        showErrorMessage(message);
        hideActivityIndicator();
    }

    public void showErrorMessage(String message) {
        if (BuildConfig.DEBUG) {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText(message)
                    .show();
        } else {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("There was a problem performing this operation.")
                    .show();
        }
    }

    @Override
    public void showInfoMessage(String message) {
        showInfoMessage(message, "info");
    }

    @Override
    public void showInfoMessage(String message, String title) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .show();
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

    }

    public void showActivityIndicator() {
        if (progressDialog != null) {
            hideActivityIndicator();
        }
        progressDialog = new ACProgressFlower.Builder(MainActivity.this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text(getResources().getString(R.string.please_wait))
                .fadeColor(Color.DKGRAY).build();
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void hideActivityIndicator() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}