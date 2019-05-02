package org.openstack.android.summit.common.user_interface;

import androidx.fragment.app.FragmentManager;
import com.crashlytics.android.Crashlytics;

/**
 * Created by smarcet on 3/15/17.
 */

final public class FragmentBackStackHelper {

    public final static void clearAllBackStack(IBaseView context){
        try {
            FragmentManager fragmentManager = context.getSupportFragmentManager();
            do {
                fragmentManager.popBackStackImmediate();
            } while (fragmentManager.getBackStackEntryCount() > 0);
        }
        catch (Exception ex){
            Crashlytics.logException(ex);
        }
    }
}
