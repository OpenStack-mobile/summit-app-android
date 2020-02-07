package org.openstack.android.summit.common.services;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.R;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.notifications.IPushNotification;
import org.openstack.android.summit.common.entities.notifications.IPushNotificationFactory;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.common.utils.RealmFactory;
import org.openstack.android.summit.modules.push_notifications_inbox.business_logic.IPushNotificationInteractor;
import org.openstack.android.summit.modules.push_notifications_inbox.business_logic.ISettingsInteractor;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import javax.inject.Inject;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;

public class PushNotificationReceiverService extends FirebaseMessagingService {

    /** The name of the Intent fired when a notification has been opened. */
    public static final String ACTION_PUSH_OPEN         = "org.openstack.android.summit.push_notification.intent.OPEN";
    /** The name of the Intent fired when a notification has been dismissed. */
    public static final String ACTION_PUSH_DELETE       = "org.openstack.android.summit.push_notification.intent.DELETE";

    public static final String ACTION_PUSH_RECEIVE      = "org.openstack.android.summit.push_notification.intent.RECEIVE";

    public static final String KEY_PUSH_NOTIFICATION_ID = "org.openstack.android.summit.push_notification.intent.PUSH_NOTIFICATION_ID";

    public static final String CHANNEL_ID               = "org.openstack.android.summit.push_notification.CHANNEL";

    @Inject
    ISecurityManager securityManager;

    @Inject
    ISettingsInteractor settings;

    @Inject
    IPushNotificationFactory factory;

    @Inject
    IPushNotificationInteractor interactor;

    final static String GROUP_KEY_OPENSTACK_NOTIFICATIONS = "openstack_notifications";

    public PushNotificationReceiverService(){
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ((OpenStackSummitApplication)getApplication()).getApplicationComponent().inject(this);
    }

    private int getSmallIconId(Context context){
        return R.drawable.push_icon;
    }

    private Notification buildNotification(Map<String, String> data){

        String title                 = data.get("title");
        String body                  = data.get("body");
        String tickerText            = String.format(Locale.US, "%s: %s", title, body);
        Random random                = new Random();
        int contentIntentRequestCode = random.nextInt();
        int deleteIntentRequestCode  = random.nextInt();
        String packageName           = this.getPackageName();
        Uri defaultSoundUri          = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent contentIntent = new Intent(ACTION_PUSH_OPEN);
        contentIntent.setPackage(packageName);
        contentIntent.putExtra(KEY_PUSH_NOTIFICATION_ID, Integer.parseInt(data.get("id")));

        Intent deleteIntent = new Intent(ACTION_PUSH_DELETE);
        deleteIntent.setPackage(packageName);
        deleteIntent.putExtra(KEY_PUSH_NOTIFICATION_ID, Integer.parseInt(data.get("id")));

        PendingIntent pContentIntent = PendingIntent.getBroadcast(this, contentIntentRequestCode,
                contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        PendingIntent pDeleteIntent = PendingIntent.getBroadcast(this, deleteIntentRequestCode,
                deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder    = new NotificationCompat.Builder(this);
        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
        style.setBigContentTitle(title).bigText(body);

        builder.setContentTitle(title)
                .setContentText(body)
                .setTicker(tickerText)
                .setSmallIcon(getSmallIconId(this))
                .setContentIntent(pContentIntent)
                .setDeleteIntent(pDeleteIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setGroup(GROUP_KEY_OPENSTACK_NOTIFICATIONS)
                .setSound(defaultSoundUri)
                .setStyle(style)
                .setChannelId(CHANNEL_ID);

        // Large icon appears on the left of the notification
        builder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher));

        return builder.build();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            boolean isBlockedNotifications = settings.getBlockAllNotifications();
            if (isBlockedNotifications) return;
            final Map<String,String> payload = remoteMessage.getData();
            String title                     = payload.get("title");
            if(title == null){
                payload.put("title", this.getResources().getString(R.string.push_notification_default_title));
            }
            super.onMessageReceived(remoteMessage);

            Notification notification = buildNotification(payload);

            if (notification != null) {
                // Fire off the notification
                NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    CharSequence channelName = getString(R.string.fcm_channel_name);

                    String channelDesc = getString(R.string.fcm_channel_description);

                    NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH);

                    // Configure the notification channel.
                    mChannel.setDescription(channelDesc);

                    mChannel.enableLights(true);

                    // Sets the notification light color for notifications posted to this
                    // channel, if the device supports this feature.
                    mChannel.setLightColor(Color.RED);

                    mChannel.enableVibration(true);
                    mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

                    nm.createNotificationChannel(mChannel);
                }
                // build the push notification
                IPushNotification pushNotification = factory.build(payload);
                Member currentMember               = securityManager.getCurrentMember();
                if (currentMember != null)
                    pushNotification.setOwner(currentMember);
                // and save it
                    pushNotification = interactor.save(pushNotification);
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
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(receivedIntent);
        }
        catch (Exception ex) {
            Log.w(Constants.LOG_TAG, ex);
        }
        finally {
            RealmFactory.closeSession();
        }
    }
}
