package org.openstack.android.summit.modules.main.user_interface;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.facebook.drawee.view.SimpleDraweeView;

import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.R;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.user_interface.BadgeCounterMenuItemDecorator;
import org.openstack.android.summit.common.user_interface.BaseActivity;
import org.openstack.android.summit.common.utils.KeyboardHelper;
import org.openstack.android.summit.dagger.components.ApplicationComponent;

import javax.inject.Inject;


public class MainActivity
        extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, IMainView {

    @Inject
    IMainPresenter presenter;

    Button loginButton;
    TextView memberNameTextView;
    SimpleDraweeView memberProfileImageView;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    EditText searchText;

    private int selectedMenuItemId;

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
        setContentView(R.layout.activity_main);
        ActionBarSetup();
        NavigationMenuSetup(savedInstanceState);
        presenter.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart(){
        super.onStart();
        presenter.onStart();
    }

    @Override
    protected void onResume() {
        try {
            super.onResume();
            Log.d(Constants.LOG_TAG, "MainActivity.onResume");
            setupNavigationIcons();
            presenter.onResume();
        } catch (Exception ex) {
            Crashlytics.logException(ex);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.onSaveInstanceState(outState);
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
    }

    @Override
    public void onStop() {
        Log.d(Constants.LOG_TAG, "MainActivity.onStop");
        super.onStop();
        presenter.onStop();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setupNavigationIcons();
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
            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                // when drawer is closing hide keyboard and clear the search textbox
                KeyboardHelper.hideKeyboard(MainActivity.this);
                if(searchText != null) searchText.setText("");
            }
        };

        drawer.addDrawerListener(toggle);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);
        //calling sync state is necessary or else your hamburger icon wont show up
        toggle.syncState();

        toggle.setToolbarNavigationClickListener(v -> {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                onBackPressed();
            }
        });

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
          setupNavigationIcons();
          if(getSupportFragmentManager().getBackStackEntryCount() > 0) {
              int id      = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getId();
              String name = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
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
            navigationView          = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            navigationView.getMenu().getItem(0).setChecked(true);
            LinearLayout headerView = (LinearLayout) navigationView.inflateHeaderView(R.layout.nav_header_main);
            loginButton             = (Button) headerView.findViewById(R.id.login_button);

            loginButton.setOnClickListener(v ->  presenter.onClickLoginButton() );

            memberNameTextView     = (TextView) headerView.findViewById(R.id.member_name_textview);

            memberProfileImageView = (SimpleDraweeView) headerView.findViewById(R.id.member_profile_pic_imageview);
            memberProfileImageView.setOnClickListener(v -> presenter.onClickMemberProfilePic() );

            searchText = (EditText) headerView.findViewById(R.id.nav_header_search_edittext);
            searchText.setOnEditorActionListener((v, actionId, event) -> {
                String searchTerm = v.getText().toString();
                if (actionId == EditorInfo.IME_ACTION_DONE && !searchTerm.isEmpty()) {
                    presenter.showSearchView(searchTerm);
                }
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                v.setText("");
                return false;
            });
        } catch (Exception ex) {
            Crashlytics.logException(ex);
        }
    }

    @Override
    public void onBackPressed() {
        try {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

            hideKeyboard();

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
                if(currentEntryName != null && currentEntryName.equals("nav_speakers")){
                    menuItemId = R.id.nav_speakers;
                }
            }
            navigationView.getMenu().findItem(menuItemId).setChecked(true);

            if (backStackCount == 1) {
                return;
            }

            drawer.closeDrawer(GravityCompat.START);
            getFragmentManager().popBackStackImmediate();
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
        } else if (selectedMenuItemId == R.id.nav_about) {
            presenter.showAboutView();
        }
        closeMenuDrawer();
        return true;
    }

    @Override
    public void closeMenuDrawer(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public ApplicationComponent getApplicationComponent() {
        return ((OpenStackSummitApplication) getApplication()).getApplicationComponent();
    }

    public void setLoginButtonText(String text) {
        if(loginButton == null) return;
        loginButton.setText(text);
    }

    @Override
    public void setMemberName(String text) {
        if(memberNameTextView == null) return;
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
    public void setNavigationViewLogOutState() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().findItem(R.id.nav_my_profile).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_events).setChecked(true);
    }

    @Override
    public void setMenuItemChecked(int menuItemId) {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView == null) return;
        MenuItem menuItem = navigationView.getMenu().findItem(menuItemId);
        if (menuItem == null) return;
        menuItem.setChecked(true);
    }

    @Override
    public void setMenuItemVisible(int menuItemId, boolean visible) {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView == null) return;
        MenuItem menuItem = navigationView.getMenu().findItem(menuItemId);
        if (menuItem == null) return;
        menuItem.setVisible(visible);
    }

    public void showErrorMessage(final String message) {
        runOnUiThread(() -> {
            MainActivity context = MainActivity.this;
            if(!context.isFinishing()) {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);

                builder.setTitle(R.string.generic_error_title)
                       .setMessage(R.string.generic_error_message)
                       .setPositiveButton(R.string.generic_error_message_ok,  (dialog, id) -> dialog.dismiss() )
                       .create()
                       .show();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.onActivityResult(requestCode, resultCode, data);
    }

}