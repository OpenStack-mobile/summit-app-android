package org.openstack.android.summit.common.business_logic;

import android.content.Context;

/**
 * Created by Claudio Redi on 2/10/2016.
 */
public interface IBaseInteractor {
    void stopPolling();

    void startPollingIfNotPollingAlready();

    boolean isNetworkingAvailable();
}
