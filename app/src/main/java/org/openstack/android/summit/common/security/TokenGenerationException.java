package org.openstack.android.summit.common.security;

/**
 * Created by Claudio Redi on 12/9/2015.
 */
public class TokenGenerationException extends Exception {
    public TokenGenerationException(Throwable throwable){
        super(throwable);
    }

    public TokenGenerationException(String detailMessage, Throwable throwable){
        super(detailMessage, throwable);
    }

    public TokenGenerationException(String detailMessage){
        super(detailMessage);
    }
}