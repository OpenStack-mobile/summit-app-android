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
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.R;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.network.IReachability;
import org.openstack.android.summit.common.security.*;
import org.openstack.android.summit.dagger.components.ApplicationComponent;
import org.openstack.android.summit.dagger.modules.ActivityModule;

import javax.inject.Inject;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity
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

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction() == Constants.LOGGED_OUT_EVENT && !userClickedLogout) {
                showInfoMessage("Your login session expired");
                onLoggedOut();
            }
            userClickedLogout = false;
        }
    };

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

        LocalBroadcastManager.getInstance(OpenStackSummitApplication.context).registerReceiver(messageReceiver, intentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideActivityIndicator();
    }

    @Override
    public void onDestroy() {
        // Unregister since the activity is about to be closed.
        // This is somewhat like [[NSNotificationCenter defaultCenter] removeObserver:name:object:]
        LocalBroadcastManager.getInstance(OpenStackSummitApplication.context).unregisterReceiver(messageReceiver);
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        presenter.onConfigurationChanged(newConfig);
        setupNavigationIcons();
    }

    public void toggleMenuLogo(boolean show) {
        ImageView footerLogo = (ImageView)findViewById(R.id.footer_logo);
        footerLogo.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void ActionBarSetup() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(getSupportFragmentManager().getBackStackEntryCount() > 0);
        toggle.syncState();
    }

    private void NavigationMenuSetup(Bundle savedInstanceState) {

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        LinearLayout headerView = (LinearLayout)navigationView.inflateHeaderView(R.layout.nav_header_main);
        loginButton = (Button)headerView.findViewById(R.id.login_button);
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

        memberNameTextView = (TextView)headerView.findViewById(R.id.member_name_textview);
        memberProfileImageView = (SimpleDraweeView)headerView.findViewById(R.id.member_profile_pic_imageview);

        EditText searchText = (EditText)headerView.findViewById(R.id.nav_header_search_edittext);
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
        } else if (selectedMenuItemId == R.id.nav_about) {
            presenter.showAboutView();
        }

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
        navigationView.getMenu().getItem(3).setVisible(show);
    }

    @Override
    public void onLoggedIn() {
        presenter.onLoggedIn();

        hideActivityIndicator();
    }

    @Override
    public void onLoggedOut() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(3).setVisible(false);
        if (selectedMenuItemId == R.id.nav_my_profile) {
            navigationView.getMenu().getItem(0).setChecked(true);
        }

        presenter.onLoggedOut();
    }

    @Override
    public void onError(String message) {
        showErrorMessage(message);
        hideActivityIndicator();
    }

    public void showErrorMessage(String message) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Oops...")
                .setContentText(message)
                .show();
    }

    @Override
    public void showInfoMessage(String message) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Info")
                .setContentText(message)
                .show();
    }

    @Override
    public void toggleMenu(boolean show) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (show) {
            drawer.openDrawer(GravityCompat.START);
        }
        else {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void setTitle(String title) {
        setTitle(title);
    }

    @Override
    public void showActivityIndicator(int delay) {

    }

    public void showActivityIndicator() {
         progressDialog = new ACProgressFlower.Builder(MainActivity.this)
                 .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                 .themeColor(Color.WHITE)
                 .text(getResources().getString(R.string.please_wait))
                 .fadeColor(Color.DKGRAY).build();
         progressDialog.show();
    }

    public void hideActivityIndicator() {
        if (progressDialog != null) {
            progressDialog.hide();
        }
    }
}