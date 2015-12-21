package org.openstack.android.summit.common.network;

import org.openstack.android.summit.common.security.ITokenManager;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 12/15/2015.
 */
public class HttpFactory implements IHttpFactory {
    @Inject
    public HttpFactory() {

    }

    @Override
    public IHttp create(ITokenManager tokenManager) {
        return new Http(tokenManager);
    }
}
