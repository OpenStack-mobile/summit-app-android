package org.openstack.android.summit.common.user_interface;

import android.os.Bundle;

import org.openstack.android.summit.common.business_logic.IBaseInteractor;

/**
 * Created by Claudio Redi on 1/11/2016.
 */
public class BasePresenter<V extends IBaseView, I extends IBaseInteractor, W> implements IBasePresenter<V> {
    protected V view;
    protected I interactor;
    protected W wireframe;

    public BasePresenter(I interactor, W wireframe) {
        this.interactor = interactor;
        this.wireframe = wireframe;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {

    }

    @Override
    public void setView(V view) {
        this.view = view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {

    }
}
