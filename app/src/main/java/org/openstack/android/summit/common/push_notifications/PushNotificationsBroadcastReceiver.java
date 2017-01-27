package org.openstack.android.summit.common.push_notifications;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;

import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.services.PushNotificationReceiverService;
import org.openstack.android.summit.common.utils.DeepLinkInfo;
import org.openstack.android.summit.common.utils.IAppLinkRouter;
import org.openstack.android.summit.modules.push_notifications_inbox.business_logic.IPushNotificationsListInteractor;
import org.openstack.android.summit.modules.settings.business_logic.ISettingsInteractor;

import javax.inject.Inject;

/**
 * Created by smarcet on 1/24/17.
 */

public class PushNotificationsBroadcastReceiver extends BroadcastReceiver {


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
        switch(intent.getAction()){
            case PushNotificationReceiverService.ACTION_PUSH_DELETE:
                onPushNotificationDismiss(context, intent);
                break;
            case PushNotificationReceiverService.ACTION_PUSH_OPEN:
                onPushNotificationOpen(context, intent);
                break;
            case PushNotificationReceiverService.ACTION_PUSH_RECEIVE:
                break;
        }
    }

    private void onPushNotificationDismiss(Context context, Intent intent) {
        Intent deletedIntent = new Intent(Constants.PUSH_NOTIFICATION_DELETED);
        LocalBroadcastManager.getInstance(context.getApplicationContext()).sendBroadcast(deletedIntent);
    }

    private void onPushNotificationOpen(Context context, Intent intent){
        if(intent.getExtras() == null ) return;

        Integer notificationId      = intent.getIntExtra(PushNotificationReceiverService.KEY_PUSH_NOTIFICATION_ID, 0);

        if (notificationId > 0)
            interactor.markAsOpen(notificationId);

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

    protected Class<? extends Activity> getActivity(Context context, Intent intent) {
        String packageName = context.getPackageName();
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (launchIntent == null) {
            return null;
        }
        String className = launchIntent.getComponent().getClassName();
        Class<? extends Activity> cls = null;
        try {
            cls = (Class <? extends Activity>)Class.forName(className);
        } catch (ClassNotFoundException e) {
            // do nothing
        }
        return cls;
    }
}
