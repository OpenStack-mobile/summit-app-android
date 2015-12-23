package org.openstack.android.summit.common.business_logic;

/**
 * Created by Claudio Redi on 12/22/2015.
 */
public abstract class InteractorAsyncOperationListener<T> implements IInteractorAsyncOperationListener<T> {
    @Override
    public void onSuceedWithData(T data) {}

    @Override
    public void onSucceed() {}

    @Override
    public void onError(String message) {}
}
