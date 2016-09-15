package org.openstack.android.summit.common.user_interface;

import android.os.Bundle;

/**
 * Created by Claudio Redi on 1/13/2016.
 */
public interface IBasePresenter <T extends IBaseView> {

    void setView(T view);

    void onSaveInstanceState(Bundle outState);

    void onRestoreInstanceState(Bundle savedInstanceState);

    void onCreate(Bundle savedInstanceState);

    void onCreateView(Bundle savedInstanceState);

    void onResume();

    void onPause();

    void onDestroy();
}
