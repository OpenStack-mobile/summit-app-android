package org.openstack.android.summit.common.network;

/**
 * Created by Claudio Redi on 3/8/2016.
 */
public class AuthorizationException extends Exception {
    public AuthorizationException(Throwable throwable){
        super(throwable);
    }

    public AuthorizationException(String detailMessage, Throwable throwable){
        super(detailMessage, throwable);
    }

    public AuthorizationException(String detailMessage){
        super(detailMessage);
    }
}
