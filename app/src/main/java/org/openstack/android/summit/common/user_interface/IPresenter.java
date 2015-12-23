package org.openstack.android.summit.common.user_interface;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import org.openstack.android.summit.modules.events.user_interface.EventsFragment;

/**
 * Created by claudio on 11/3/2015.
 */
public interface IPresenter<T extends BaseFragment> {
    void setView(T view);

    /*
    * Method that control the lifecycle of the view. It should be called in the view's
    * (Activity or Fragment) onCreate() method.
    */
    void onCreate(Bundle savedInstanceState);

     /*
     * Method that control the lifecycle of the view. It should be called in the view's
     * (Activity or Fragment) onResume() method.
     */
    void resume();

    /**
     * Method that control the lifecycle of the view. It should be called in the view's
     * (Activity or Fragment) onPause() method.
     */
    void pause();

    /**
     * Method that control the lifecycle of the view. It should be called in the view's
     * (Activity or Fragment) onDestroy() method.
     */
    void destroy();
}
