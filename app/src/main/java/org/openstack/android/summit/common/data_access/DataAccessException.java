package org.openstack.android.summit.common.data_access;

/**
 * Created by sebastian on 8/25/2016.
 */
public class DataAccessException extends Exception {
    public DataAccessException(Throwable throwable){
        super(throwable);
    }

    public DataAccessException(String detailMessage, Throwable throwable){
        super(detailMessage, throwable);
    }

    public DataAccessException(String detailMessage){
        super(detailMessage);
    }
}

