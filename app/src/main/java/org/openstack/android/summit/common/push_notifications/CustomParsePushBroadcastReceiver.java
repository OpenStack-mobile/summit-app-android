package org.openstack.android.summit.common.push_notifications;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
import com.parse.ParseAnalytics;
import com.parse.ParsePushBroadcastReceiver;
import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.R;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.utils.AppLinkRouter;
import org.openstack.android.summit.common.utils.DeepLinkInfo;
import java.util.Locale;
import java.util.Random;
import android.support.v4.app.NotificationCompat.BigTextStyle;

/**
 * Created by sebastian on 8/16/2016.
 */
public class CustomParsePushBroadcastReceiver extends ParsePushBroadcastReceiver {

    final static String GROUP_KEY_OPENSTACK_NOTIFICATIONS = "openstack_notifications";

    private JSONObject getPushData(Intent intent) {
        try {
            return new JSONObject(intent.getStringExtra(KEY_PUSH_DATA));
        } catch (JSONException e) {
            Log.e(Constants.LOG_TAG, "Unexpected JSONException when receiving push data: ", e);
            return null;
        }
    }

    @Override
    protected Notification getNotification(Context context, Intent intent) {
        JSONObject pushData = getPushData(intent);

        if (pushData == null || (!pushData.has("alert") && !pushData.has("title"))) {
            return null;
        }

        String title                 = pushData.optString("title", "OpenStack");
        String alert                 = pushData.optString("alert", "Notification received.");
        String tickerText            = String.format(Locale.getDefault(), "%s: %s", title, alert);
        Bundle extras                = intent.getExtras();
        Random random                = new Random();
        int contentIntentRequestCode = random.nextInt();
        int deleteIntentRequestCode  = random.nextInt();

        // Security consideration: To protect the app from tampering, we require that intent filters
        // not be exported. To protect the app from information leaks, we restrict the packages which
        // may intercept the push intents.
        String packageName = context.getPackageName();

        Intent contentIntent = new Intent(ParsePushBroadcastReceiver.ACTION_PUSH_OPEN);
        contentIntent.putExtras(extras);
        contentIntent.setPackage(packageName);

        Intent deleteIntent = new Intent(ParsePushBroadcastReceiver.ACTION_PUSH_DELETE);
        deleteIntent.putExtras(extras);
        deleteIntent.setPackage(packageName);

        PendingIntent pContentIntent = PendingIntent.getBroadcast(context, contentIntentRequestCode,
                contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pDeleteIntent = PendingIntent.getBroadcast(context, deleteIntentRequestCode,
                deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // The purpose of setDefaults(Notification.DEFAULT_ALL) is to inherit notification properties
        // from system defaults
        Builder builder    = new Builder(context);
        BigTextStyle style = new BigTextStyle();
        style.setBigContentTitle(title).bigText(alert);

        builder.setContentTitle(title)
                .setContentText(alert)
                .setTicker(tickerText)
                .setSmallIcon(this.getSmallIconId(context, intent))
                .setContentIntent(pContentIntent)
                .setDeleteIntent(pDeleteIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setGroup(GROUP_KEY_OPENSTACK_NOTIFICATIONS)
                .setStyle(style);

        // Large icon appears on the left of the notification
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));

        return builder.build();
    }

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        // Send a Parse Analytics "push opened" event
        ParseAnalytics.trackAppOpenedInBackground(intent);

        String uriString = null;
        try {
            JSONObject pushData = new JSONObject(intent.getStringExtra(KEY_PUSH_DATA));
            int eventId = pushData.optInt("event_id", 0);
            if(eventId > 0){
                uriString = String.format("%s://%s/%s", AppLinkRouter.DeepLinkHost, DeepLinkInfo.EventsPath, eventId);
            }
        } catch (JSONException e) {
            Log.e(Constants.LOG_TAG, "Unexpected JSONException when receiving push data: ", e);
        }

        Class<? extends Activity> cls = getActivity(context, intent);
        Intent activityIntent = (uriString != null)?new Intent(Intent.ACTION_VIEW, Uri.parse(uriString)): new Intent(context, cls);
        activityIntent.putExtras(intent.getExtras());

        /*
          In order to remove dependency on android-support-library-v4
          The reason why we differentiate between versions instead of just using context.startActivity
          for all devices is because in API 11 the recommended conventions for app navigation using
          the back key changed.
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(cls);
            stackBuilder.addNextIntent(activityIntent);
            stackBuilder.startActivities();
            return;
        }

        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(activityIntent);
    }

    @Override
    protected void onPushDismiss(Context context, Intent intent) {
        // do nothing
    }

}
