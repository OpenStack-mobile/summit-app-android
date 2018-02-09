package org.openstack.android.summit.modules.splash.user_interface;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.user_interface.BaseActivity;
import org.openstack.android.summit.modules.push_notifications_inbox.business_logic.ISettingsInteractor;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by smarcet on 2/3/17.
 */

public class SplashActivity extends BaseActivity implements ISplashView {

    @Inject
    ISplashPresenter presenter;

    @Inject
    ISettingsInteractor settings;

    @BindView(R.id.btn_splash_login)
    Button loginButton;

    @BindView(R.id.btn_splash_guest)
    Button guestButton;

    @BindView(R.id.splash_summit_info_container)
    LinearLayout summitInfoContainer;

    @BindView(R.id.splash_summit_dates)
    TextView summitDates;

    @BindView(R.id.splash_summit_name)
    TextView summitName;

    @BindView(R.id.splash_logo)
    ImageView splashLogo;

    @BindView(R.id.splash_main_container)
    LinearLayout splashMainContainer;

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getApplicationComponent().inject(this);

        setContentView(R.layout.activity_splash);

        unbinder = ButterKnife.bind(this);

        StartAnimations();
        presenter.setView(this);
        presenter.onCreate(savedInstanceState);

        loginButton.setOnClickListener(v -> presenter.loginClicked(v));

        guestButton.setOnClickListener(v -> presenter.guestClicked(v));

    }

    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();

        if(splashMainContainer == null) return;

        splashMainContainer.clearAnimation();
        splashMainContainer.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();

        if(splashLogo != null) {
            splashLogo.clearAnimation();
            splashLogo.startAnimation(anim);
        }
    }

    @Override
    protected void onResume() {
        try {
            Log.d(Constants.LOG_TAG, "SplashActivity.onResume");
            super.onResume();
            presenter.onResume();

        } catch (Exception ex) {
            Crashlytics.logException(ex);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        presenter.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void setLoginButtonVisibility(boolean visible) {
        if(loginButton == null) return;
        loginButton.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setGuestButtonVisibility(boolean visible) {
        if(guestButton == null) return;
        guestButton.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setSummitDates(String summitDates) {
        if(this.summitDates == null) return;
        this.summitDates.setText(summitDates);
    }

    @Override
    public void setSummitName(String summitName) {
        if(this.summitName == null) return;
        this.summitName.setText(summitName);
    }

    @Override
    public void setSummitInfoContainerVisibility(boolean visible) {
        if(summitInfoContainer == null) return;
        summitInfoContainer.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }
}
