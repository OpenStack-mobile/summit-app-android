package org.openstack.android.summit.common.user_interface;

import android.view.Menu;

import java.lang.reflect.Method;

/**
 * Created by smarcet on 2/27/17.
 */

public final class MenuHelper {

    public static void setShowIcons(Menu menu){
        if(menu.getClass().getSimpleName().equals("MenuBuilder")){
            try{
                Method m = menu.getClass().getDeclaredMethod(
                        "setOptionalIconsVisible", Boolean.TYPE);
                m.setAccessible(true);
                m.invoke(menu, true);
            }
            catch(Exception e){
            }
        }
    }
}
