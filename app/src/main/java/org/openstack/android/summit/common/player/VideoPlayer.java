package org.openstack.android.summit.common.player;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.youtube.player.YouTubePlayer;
import org.openstack.android.summit.R;
import org.openstack.android.summit.common.DTOs.VideoDTO;
import org.openstack.android.summit.common.player.enums.PlayerOrientation;
import org.openstack.android.summit.common.player.enums.ThumbnailQuality;

/**
 * Created by sebastian on 8/18/2016.
 */
public class VideoPlayer extends RelativeLayout {

    SimpleDraweeView thumbnail;
    ImageButton playButton;

    private void initialize(Context context){
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.video_player, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        thumbnail  = (SimpleDraweeView) findViewById(R.id.thumbnail);
        playButton = (ImageButton)findViewById(R.id.play_button);
    }

    public VideoPlayer(Context context) {
        super(context);
        initialize(context);
    }

    public VideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public VideoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    @SuppressLint("InlinedApi")
    public VideoPlayer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(context);
    }

    public void loadVideo(final VideoDTO video){

        if(video.getYouTubeId() == null ) return;

        setVisibility(View.VISIBLE);
        thumbnail.setImageURI(YouTubeThumbnail.getUrlFromVideoId(video.getYouTubeId(), ThumbnailQuality.HIGH));

        final YouTubePlayer.PlayerStyle playerStyle = YouTubePlayer.PlayerStyle.DEFAULT;
        final PlayerOrientation orientation         = PlayerOrientation.AUTO;

        playButton.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {
                int[] location = new int[2];
                playButton.getLocationOnScreen(location);
                Toast toast = Toast.makeText(getContext(), video.getName(), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP|Gravity.LEFT, playButton.getRight()+5, location[1]-10);
                toast.show();
                return true;
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), YouTubePlayerActivity.class);
                intent.putExtra(YouTubePlayerActivity.EXTRA_VIDEO_ID, video.getYouTubeId());
                intent.putExtra(YouTubePlayerActivity.EXTRA_PLAYER_STYLE, playerStyle);
                intent.putExtra(YouTubePlayerActivity.EXTRA_ORIENTATION, orientation);
                intent.putExtra(YouTubePlayerActivity.EXTRA_SHOW_AUDIO_UI, true);
                intent.putExtra(YouTubePlayerActivity.EXTRA_HANDLE_ERROR, true);
                intent.putExtra(YouTubePlayerActivity.EXTRA_ANIM_ENTER, R.anim.fade_in);
                intent.putExtra(YouTubePlayerActivity.EXTRA_ANIM_EXIT, R.anim.fade_out);
                //intent.putExtra(YouTubePlayerActivity.EXTRA_ANIM_ENTER, R.anim.modal_close_enter);
                //intent.putExtra(YouTubePlayerActivity.EXTRA_ANIM_EXIT, R.anim.modal_close_exit);
                getContext().startActivity(intent);
            }
        });

    }
}
