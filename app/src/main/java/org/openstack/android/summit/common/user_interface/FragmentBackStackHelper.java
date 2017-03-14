package org.openstack.android.summit.common.user_interface;

import android.support.v4.app.FragmentManager;

/**
 * Created by smarcet on 3/15/17.
 */

final public class FragmentBackStackHelper {

    public final static void clearAllBackStack(IBaseView context){
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        do{
            fragmentManager.popBackStackImmediate();
        }while(fragmentManager.getBackStackEntryCount() > 0);
    }
}
