package org.openstack.android.summit.common.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.support.v4.app.JobIntentService;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.R;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.business_logic.IProcessableUserActionManager;
import org.openstack.android.summit.common.network.IReachability;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.common.utils.RealmFactory;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * Created by smarcet on 2/7/18.
 */

public class UserActionsPostProcessService extends JobIntentService {

    public static final int JOB_ID  = 4000;
    @Inject
    IReachability reachability;

    @Inject
    ISecurityManager securityManager;

    @Inject
    IProcessableUserActionManager manager;

    public static Intent newIntent(Context context) {
        return new Intent(context, UserActionsPostProcessService.class);
    }

    @Override
    public void onCreate(){
        super.onCreate();
        ((OpenStackSummitApplication) getApplication()).getApplicationComponent().inject(this);
    }

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, UserActionsPostProcessService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(Intent intent) {
        try {
            if (!securityManager.isLoggedIn()) {
                return;
            }
            if (!reachability.isNetworkingAvailable(this)) {
                return;
            }
            // if we are on data ingestion running , skip it this
            if (SummitDataIngestionService.isRunning()) return;
            // process pending changes
            manager.processMyScheduleProcessableUserActions();
            manager.processMyFavoritesProcessableUserActions();
            manager.processMyFeedbackProcessableUserActions();
            manager.processMyRSVPProcessableUserActions();
        }
        catch (Exception ex){
            Crashlytics.logException(ex);
        }
        finally {
            RealmFactory.closeSession();
        }
    }

    public static void setServiceAlarm(Context context, boolean isOn) {

        Intent i                  = UserActionsPostProcessService.newIntent(context);
        PendingIntent pi          = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        if (isOn) {
            Log.d(Constants.LOG_TAG, "Starting Service UserActionsPostProcessService");
            long interval = TimeUnit.MILLISECONDS.convert(context.getResources().getInteger(R.integer.user_actions_post_process_service_interval), TimeUnit.SECONDS);
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 100, interval, pi);
            return;
        }

        Log.d(Constants.LOG_TAG, "Stopping Service UserActionsPostProcessService");
        alarmManager.cancel(pi);
        pi.cancel();
    }

    public static boolean isServiceAlarmOn(Context context) {
        Intent i         = UserActionsPostProcessService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }

    private static volatile HandlerThread handlerThread;
    private static volatile Handler serviceHandler;
    private static volatile Runnable runnableCode;

    public static void start(Context ctx){
        if(handlerThread != null && handlerThread.isAlive()) return;
        // An Android handler thread internally operates on a looper.
        handlerThread = new HandlerThread("UserActionsPostProcessService.HandlerThread");
        handlerThread.start();
        // An Android service handler is a handler running on a specific background thread.
        serviceHandler = new Handler(UserActionsPostProcessService.handlerThread.getLooper());
        long interval  = TimeUnit.MILLISECONDS.convert(ctx.getResources().getInteger(R.integer.user_actions_post_process_service_interval), TimeUnit.SECONDS);
        runnableCode   = new Runnable() {
            @Override
            public void run() {
                Log.i(Constants.LOG_TAG, String.format("Calling service UserActionsPostProcessService intent from thread %s", Thread.currentThread().getName()));

                UserActionsPostProcessService.enqueueWork(ctx, UserActionsPostProcessService.newIntent(ctx));
                serviceHandler.postDelayed(this, interval);
            }
        };
        serviceHandler.post(runnableCode);
    }

    public static boolean isRunning(){
        HandlerThread localHandlerThread = handlerThread;
        return  localHandlerThread != null && localHandlerThread.isAlive();
    }

    public static void stop(){
        HandlerThread localHandlerThread = handlerThread;
        Handler localServiceHandler      = serviceHandler;
        Runnable localRunnableCode       = runnableCode;
        if(localHandlerThread != null){
            Log.i(Constants.LOG_TAG, String.format("Stopping UserActionsPostProcessService from thread %s", Thread.currentThread().getName()));
            if(localServiceHandler != null && localRunnableCode != null ) {
                localServiceHandler.removeCallbacks(localRunnableCode);
                serviceHandler = null;
                runnableCode   = null;
            }
            localHandlerThread.quit();
            handlerThread = null;
        }
    }
}
