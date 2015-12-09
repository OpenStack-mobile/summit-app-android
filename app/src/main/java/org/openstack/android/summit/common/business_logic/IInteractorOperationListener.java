package org.openstack.android.summit.common.business_logic;

/**
 * Created by Claudio Redi on 11/17/2015.
 */
public interface IInteractorOperationListener<T> {
    void onSuceedWithData(T data);
    void onSucceed();
    void onError(String message);
}
