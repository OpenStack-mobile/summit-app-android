package org.openstack.android.summit.common.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Claudio Redi on 2/8/2016.
 */
public interface IReachability {
    ConnectivityManager getConnectivityManager(Context context);

    NetworkInfo getActiveNetworkInfo(Context context);

    boolean isNetworkingAvailable(Context context);
}
