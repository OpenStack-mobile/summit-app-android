package org.openstack.android.summit.common.business_logic;

/**
 * Created by Claudio Redi on 11/17/2015.
 */
public interface IInteractorAsyncOperationListener<T> {
    void onSucceedWithData(T data);
    void onSucceed();
    void onError(String message);
}
