package org.openstack.android.summit.common.security.oidc;

import java.util.Locale;

/**
 * Created by smarcet on 2/17/17.
 */

final public class NativeApplicationScopes {

    public final static String OpenIdConnect                = "openid";
    public final static String Offline                      = "offline_access";
    public final static String SummitRead                   = "%s/summits/read";
    public final static String SummitWrite                  = "%s/summits/write";
    public final static String ReadExternalOrders           = "%s/summits/read-external-orders";
    public final static String ConfirmExternalOrders        = "%s/summits/confirm-external-orders";
    public final static String ReadOwnMember                = "%s/me/read";
    public final static String OwmMemberAdd2Favorites       = "%s/me/summits/events/favorites/add";
    public final static String OwmMemberRemoveFromFavorites = "%s/me/summits/events/favorites/delete";


    public static String[] getScopes(String resourceServerBaseUrl){
        return new String[] {
                OpenIdConnect,
                Offline,
                String.format(Locale.US, SummitRead, resourceServerBaseUrl),
                String.format(Locale.US, SummitWrite, resourceServerBaseUrl),
                //String.format(Locale.US, ReadExternalOrders, resourceServerBaseUrl),
                //String.format(Locale.US, ConfirmExternalOrders, resourceServerBaseUrl),
                String.format(Locale.US, ReadOwnMember, resourceServerBaseUrl),
                String.format(Locale.US, OwmMemberAdd2Favorites, resourceServerBaseUrl),
                String.format(Locale.US, OwmMemberRemoveFromFavorites, resourceServerBaseUrl)
        };
    }

}
