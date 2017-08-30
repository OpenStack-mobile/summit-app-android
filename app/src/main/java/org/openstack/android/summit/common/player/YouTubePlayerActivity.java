package org.openstack.android.summit.common.player;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.player.enums.PlayerOrientation;
import org.openstack.android.summit.common.player.utils.AudioUtil;
import org.openstack.android.summit.common.player.utils.StatusBarUtil;
import org.openstack.android.summit.common.player.utils.YouTubeApp;
import org.openstack.android.summit.common.security.IConfigurationParamsManager;

import java.security.InvalidParameterException;

import javax.inject.Inject;

public class YouTubePlayerActivity extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener,
        YouTubePlayer.OnFullscreenListener,
        YouTubePlayer.PlayerStateChangeListener {

    private static final int RECOVERY_DIALOG_REQUEST = 1;
    public static final String EXTRA_VIDEO_ID      = "video_id";
    public static final String EXTRA_PLAYER_STYLE  = "player_style";
    public static final String EXTRA_ORIENTATION   = "orientation";
    public static final String EXTRA_SHOW_AUDIO_UI = "show_audio_ui";
    public static final String EXTRA_HANDLE_ERROR  = "handle_error";
    public static final String EXTRA_ANIM_ENTER    = "anim_enter";
    public static final String EXTRA_ANIM_EXIT     = "anim_exit";

    private String googleApiKey;
    private String videoId;
    private YouTubePlayer.PlayerStyle playerStyle;
    private PlayerOrientation orientation;
    private boolean showAudioUi;
    private boolean handleError;
    private int animEnter;
    private int animExit;
    private YouTubePlayerView playerView;
    private YouTubePlayer player;

    @Inject
    IConfigurationParamsManager configurationParamsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((OpenStackSummitApplication) getApplication()).getApplicationComponent().inject(this);

        initialize(savedInstanceState);

        playerView = new YouTubePlayerView(this);
        playerView.initialize(googleApiKey, this);

        addContentView
                (
                        playerView,
                        new FrameLayout.LayoutParams
                                (
                                        FrameLayout.LayoutParams.MATCH_PARENT,
                                        FrameLayout.LayoutParams.MATCH_PARENT
                                )
                );

        playerView.setBackgroundResource(android.R.color.black);

        StatusBarUtil.hide(this);
    }

    private void initialize(Bundle savedInstanceState) {

        try {
            googleApiKey = configurationParamsManager.findConfigParamBy("common.player.YouTubePlayerActivity.API_KEY");
            if (googleApiKey == null)
                throw new InvalidParameterException("Google API key must not be null. Set your api key as meta data in AndroidManifest.xml file.");

            if (savedInstanceState != null) {

                videoId = savedInstanceState.getString(EXTRA_VIDEO_ID);

                if (videoId == null)
                    throw new InvalidParameterException("Video ID must not be null");

                showAudioUi = savedInstanceState.getBoolean(EXTRA_SHOW_AUDIO_UI);
                handleError = savedInstanceState.getBoolean(EXTRA_ANIM_EXIT);
                animEnter   = savedInstanceState.getInt(EXTRA_ANIM_ENTER);
                animExit    = savedInstanceState.getInt(EXTRA_ANIM_EXIT);
                playerStyle = (YouTubePlayer.PlayerStyle) savedInstanceState.getSerializable(EXTRA_PLAYER_STYLE);
                orientation = (PlayerOrientation) savedInstanceState.getSerializable(EXTRA_ORIENTATION);

                return;
            }
            // get from current intent
            videoId = getIntent().getStringExtra(EXTRA_VIDEO_ID);

            if (videoId == null)
                throw new InvalidParameterException("Video ID must not be null");

            playerStyle = (YouTubePlayer.PlayerStyle) getIntent().getSerializableExtra(EXTRA_PLAYER_STYLE);

            if (playerStyle == null)
                playerStyle = YouTubePlayer.PlayerStyle.DEFAULT;

            orientation = (PlayerOrientation) getIntent().getSerializableExtra(EXTRA_ORIENTATION);
            if (orientation == null)
                orientation = PlayerOrientation.AUTO;

            showAudioUi = getIntent().getBooleanExtra(EXTRA_SHOW_AUDIO_UI, true);
            handleError = getIntent().getBooleanExtra(EXTRA_HANDLE_ERROR, true);
            animEnter = getIntent().getIntExtra(EXTRA_ANIM_ENTER, 0);
            animExit = getIntent().getIntExtra(EXTRA_ANIM_EXIT, 0);

        } catch (Exception ex) {
            Crashlytics.logException(ex);
            Log.e(Constants.LOG_TAG, ex.getMessage());
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_VIDEO_ID, videoId);
        outState.putSerializable(EXTRA_PLAYER_STYLE, playerStyle);
        outState.putSerializable(EXTRA_ORIENTATION, orientation);
        outState.putBoolean(EXTRA_SHOW_AUDIO_UI, showAudioUi);
        outState.putBoolean(EXTRA_ANIM_EXIT, handleError);
        outState.putInt(EXTRA_ANIM_ENTER, animEnter);
        outState.putInt(EXTRA_ANIM_EXIT, animExit);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer player,
                                        boolean wasRestored) {
        this.player = player;
        player.setOnFullscreenListener(this);
        player.setPlayerStateChangeListener(this);

        switch (orientation) {
            case AUTO:
                player.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION
                        | YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI
                        | YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE
                        | YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
                break;
            case AUTO_START_WITH_LANDSCAPE:
                player.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION
                        | YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI
                        | YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE
                        | YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
                player.setFullscreen(true);
                break;
            case ONLY_LANDSCAPE:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                player.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI
                        | YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
                player.setFullscreen(true);
                break;
            case ONLY_PORTRAIT:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                player.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI
                        | YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
                player.setFullscreen(true);
                break;
        }

        switch (playerStyle) {
            case CHROMELESS:
                player.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
                break;
            case MINIMAL:
                player.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
                break;
            case DEFAULT:
            default:
                player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                break;
        }

        if (!wasRestored)
            player.loadVideo(videoId);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(
                    "There was an error initializing the YouTubePlayer (%1$s)",
                    errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            playerView.initialize(googleApiKey, this);
        }
    }

    // YouTubePlayer.OnFullscreenListener
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        switch (orientation) {
            case AUTO:
            case AUTO_START_WITH_LANDSCAPE:
                if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    if (player != null)
                        player.setFullscreen(true);
                } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT && player != null) {
                    player.setFullscreen(false);
                }
                break;
            case ONLY_LANDSCAPE:
            case ONLY_PORTRAIT:
                break;
        }
    }

    @SuppressLint("InlinedApi")
    private static final int PORTRAIT_ORIENTATION = Build.VERSION.SDK_INT < 9
            ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            : ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;

    @SuppressLint("InlinedApi")
    private static final int LANDSCAPE_ORIENTATION = Build.VERSION.SDK_INT < 9
            ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            : ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;

    @Override
    public void onFullscreen(boolean fullScreen) {
        switch (orientation) {
            case AUTO:
            case AUTO_START_WITH_LANDSCAPE:
                if (fullScreen)
                    setRequestedOrientation(LANDSCAPE_ORIENTATION);
                else
                    setRequestedOrientation(PORTRAIT_ORIENTATION);
                break;
            case ONLY_LANDSCAPE:
            case ONLY_PORTRAIT:
                break;
        }
    }

    // YouTubePlayer.PlayerStateChangeListener
    @Override
    public void onError(YouTubePlayer.ErrorReason reason) {
        Log.e("onError", "onError : " + reason.name());
        if (handleError && YouTubePlayer.ErrorReason.NOT_PLAYABLE.equals(reason))
            YouTubeApp.startVideo(this, videoId);
    }

    @Override
    public void onAdStarted() {
    }

    @Override
    public void onLoaded(String videoId) {
    }

    @Override
    public void onLoading() {
    }

    @Override
    public void onVideoEnded() {
    }

    @Override
    public void onVideoStarted() {
        StatusBarUtil.hide(this);
    }

    // Audio Managing
    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            AudioUtil.adjustMusicVolume(getApplicationContext(), true, showAudioUi);
            StatusBarUtil.hide(this);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            AudioUtil.adjustMusicVolume(getApplicationContext(), false, showAudioUi);
            StatusBarUtil.hide(this);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // Animation
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (animEnter != 0 && animExit != 0)
            overridePendingTransition(animEnter, animExit);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // https://stackoverflow.com/questions/44379747/youtube-android-player-api-throws-badparcelableexception-classnotfoundexception
        try {
            super.onRestoreInstanceState(savedInstanceState);
        }
        catch(Exception ex){
            Crashlytics.logException(ex);
        }
    }
}

