package org.openstack.android.summit.common.network;

/**
 * Created by Claudio Redi on 12/8/2015.
 */
public interface HttpTaskListener {
    void onSucceed(String data);
    void onError(Throwable error);
}