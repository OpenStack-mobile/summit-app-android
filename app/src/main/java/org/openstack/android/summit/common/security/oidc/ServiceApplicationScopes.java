package org.openstack.android.summit.common.security.oidc;

import java.util.Locale;

/**
 * Created by smarcet on 2/17/17.
 */

final public class ServiceApplicationScopes {

    public final static String SummitRead = "%s/summits/read";

    public static String[] getScopes(String resourceServerBaseUrl){
        return new String[] {
                String.format(Locale.US, SummitRead, resourceServerBaseUrl),
        };
    }
}
