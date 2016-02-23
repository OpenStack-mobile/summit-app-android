package org.openstack.android.summit.common.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * A utility class for determining network availability.
 */
public class Reachability implements IReachability {

    /**
     * A convienence method for retreiving the ConnectivityManager in a given
     * context.
     *
     * @param context
     * @return the ConnectivityManager instance for the given context.
     */
    @Override
    public ConnectivityManager getConnectivityManager(Context context) {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    /**
     * A convienence method for retreiving the {@link NetworkInfo} for the
     * currently active network.
     *
     * @param context
     * @return a {@link NetworkInfo} instance for the currently active network.
     */
    @Override
    public NetworkInfo getActiveNetworkInfo(Context context) {
        return getConnectivityManager(context).getActiveNetworkInfo();
    }

    /**
     * A convienence method for determining if networking is available for the
     * currently active network connection.
     *
     * @param context
     * @return true if and only if the currently active network is available and
     *         connected.
     */
    public boolean isNetworkingAvailable(Context context) {
        NetworkInfo info = getActiveNetworkInfo(context);
        return info != null && info.isAvailable() && info.isConnected();
    }

}