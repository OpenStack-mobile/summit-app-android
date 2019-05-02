package org.openstack.android.summit.common.player.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import androidx.annotation.NonNull;

import java.util.List;

/**
 * Created by sebastian on 8/17/2016.
 */
public class YouTubeApp {

    private YouTubeApp() {}

    public static void startVideo(@NonNull Context context, @NonNull String videoId) {
        Uri video_uri = Uri.parse(YouTubeUrlParser.getVideoUrl(videoId));
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(
                intent,
                PackageManager.MATCH_DEFAULT_ONLY);

        if (list.isEmpty())
            intent = new Intent(Intent.ACTION_VIEW, video_uri);

        context.startActivity(intent);
    }
}
