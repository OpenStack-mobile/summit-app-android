package org.openstack.android.summit.common.data_access.data_polling;

/**
 * Created by Claudio Redi on 3/28/2016.
 */
public class DataUpdateException extends Exception {
    public DataUpdateException(Throwable throwable){
        super(throwable);
    }

    public DataUpdateException(String detailMessage, Throwable throwable){
        super(detailMessage, throwable);
    }

    public DataUpdateException(String detailMessage){
        super(detailMessage);
    }
}
