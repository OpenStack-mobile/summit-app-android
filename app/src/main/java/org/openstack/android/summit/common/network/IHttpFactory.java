package org.openstack.android.summit.common.network;

import org.openstack.android.summit.common.security.ITokenManager;

/**
 * Created by Claudio Redi on 12/15/2015.
 */
public interface IHttpFactory {
    IHttp create(ITokenManager tokenManager);
}
