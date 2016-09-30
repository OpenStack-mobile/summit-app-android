package org.openstack.android.summit.common.push_notifications;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.BigTextStyle;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.parse.ParseAnalytics;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.R;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.data_access.IPushNotificationDataStore;
import org.openstack.android.summit.common.data_access.deserialization.IDeserializer;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.PushNotification;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.common.utils.DeepLinkInfo;
import org.openstack.android.summit.common.utils.IAppLinkRouter;
import org.openstack.android.summit.modules.push_notifications_inbox.business_logic.IPushNotificationsListInteractor;
import org.openstack.android.summit.modules.settings.business_logic.ISettingsInteractor;

import java.util.Locale;
import java.util.Random;

import javax.inject.Inject;

/**
 * Created by sebastian on 8/16/2016.
 */
public class CustomParsePushBroadcastReceiver extends ParsePushBroadcastReceiver {

    @Inject
    IDeserializer deserializer;

    @Inject
    IPushNotificationDataStore pushNotificationDataStore;

    @Inject
    ISecurityManager securityManager;

    @Inject
    IPushNotificationsListInteractor interactor;

    @Inject
    IAppLinkRouter appLinkRouter;

    @Inject
    ISettingsInteractor settings;

    @Override
    public void onReceive(Context context, Intent intent) {
        ((OpenStackSummitApplication) context.getApplicationContext()).getApplicationComponent().inject(this);
        boolean isBlockedNotifications = settings.getBlockAllNotifications();
        if (isBlockedNotifications) return;
        super.onReceive(context, intent);
    }

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
        String alert                 = pushData.optString("alert", "PushNotification received.");
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

        // The purpose of setDefaults(PushNotification.DEFAULT_ALL) is to inherit notification properties
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
        Integer notificationId = 0;
        try {
            String data         = intent.getStringExtra(KEY_PUSH_DATA);
            if(data != null && !data.isEmpty()) {
                JSONObject pushData = new JSONObject(data);
                notificationId = pushData.optInt("id", 0);

                if (notificationId > 0)
                    interactor.markAsOpen(notificationId);
            }

        } catch (JSONException e) {
            Log.e(Constants.LOG_TAG, "Unexpected JSONException when receiving push data: ", e);
        }

        Class<? extends Activity> cls = getActivity(context, intent);
        Intent activityIntent = new Intent(Intent.ACTION_VIEW, this.appLinkRouter.buildUriFor(DeepLinkInfo.NotificationsPath, notificationId.toString()));
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

        Intent openedIntent = new Intent(Constants.PUSH_NOTIFICATION_OPENED);
        LocalBroadcastManager.getInstance(context.getApplicationContext()).sendBroadcast(openedIntent);

        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        context.startActivity(activityIntent);
    }

    @Override
    protected void onPushDismiss(Context context, Intent intent) {
        Intent deletedIntent = new Intent(Constants.PUSH_NOTIFICATION_DELETED);
        LocalBroadcastManager.getInstance(context.getApplicationContext()).sendBroadcast(deletedIntent);
    }

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        String data = intent.getStringExtra(KEY_PUSH_DATA);

        if (data == null) {
            return;
        }

        JSONObject pushData = null;
        try {
            pushData = new JSONObject(data);
        } catch (JSONException e) {
            Log.e(Constants.LOG_TAG, "Unexpected JSONException when receiving push data: ", e);
        }

        // save
        try {
            // If the push data includes an action string, that broadcast intent is fired.
            String action = null;
            if (pushData != null) {
                action = pushData.optString("action", null);
            }
            if (action != null) {
                Bundle extras = intent.getExtras();
                Intent broadcastIntent = new Intent();
                broadcastIntent.putExtras(extras);
                broadcastIntent.setAction(action);
                broadcastIntent.setPackage(context.getPackageName());
                context.sendBroadcast(broadcastIntent);
            }

            Notification notification = getNotification(context, intent);

            PushNotification pushNotification = deserializer.deserialize(data, PushNotification.class);
            Member currentMember = securityManager.getCurrentMember();
            if (currentMember != null)
                pushNotification.setOwner(currentMember);

            pushNotificationDataStore.saveOrUpdate(pushNotification, null, PushNotification.class);

            if (notification != null) {
                // Fire off the notification
                NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


                try {
                    nm.notify(pushNotification.getId(), notification);
                } catch (SecurityException e) {
                    // Some phones throw an exception for unapproved vibration
                    notification.defaults = Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND;
                    nm.notify(pushNotification.getId(), notification);
                }
            }
            // send the notification
            Intent receivedIntent = new Intent(Constants.PUSH_NOTIFICATION_RECEIVED);
            LocalBroadcastManager.getInstance(context.getApplicationContext()).sendBroadcast(receivedIntent);
        } catch (Exception ex) {
            Log.w(Constants.LOG_TAG, ex);
        }
    }

}
